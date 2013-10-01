package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

public final class GameModel implements IObservable  {

	private final List<Player> players;
	private final List<PoliceStationTile> policeStationTiles;
	private Player currentPlayer;
	private final Player playerClient;
	private final PropertyChangeSupport pcs;


	public static final String PROPERTY_CURRENT_PLAYER = "CurrentPlayer";
	


	public GameModel(final IMediator mediator, final Player playerClient, final List<Player> players, final IWalkableTile[][] tiles) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		if (players == null || players.isEmpty())
			throw new IllegalArgumentException("Players not allowed to be null or empty");
		if (playerClient == null || !players.contains(playerClient))
			throw new IllegalArgumentException("The player of this instance not allowed to be null and has to be in the players list");
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");

		this.playerClient = playerClient;
		this.players = players;
		
		mediator.registerGameModel(this);

		policeStationTiles = new ArrayList<PoliceStationTile>();
		

		// Extract police station tiles
		for (IWalkableTile[] tileArray : tiles) {
			for (IWalkableTile tile : tileArray) {
				if (tile instanceof PoliceStationTile) {
					policeStationTiles.add((PoliceStationTile)tile);
				} 
			}
		}
		pcs = new PropertyChangeSupport(this);
	}

	public void startGame(){
		this.currentPlayer = players.get(0);
		currentPlayer.updateState();
		pcs.firePropertyChange(PROPERTY_CURRENT_PLAYER, null, currentPlayer);
	}

	void nextPlayer(){
		int i = players.indexOf(currentPlayer);
		currentPlayer = players.get((i + 1) % players.size());
		currentPlayer.updateState();
		pcs.firePropertyChange(PROPERTY_CURRENT_PLAYER, null, currentPlayer);
	}

	public IPlayer getCurrentPlayer(){
		return currentPlayer;
	}
	
	public IPlayer getPlayerClient(){
		return playerClient;
	}

	void moveToEmptyPoliceStationTile(AbstractPawn movable) {
		PoliceStationTile policeStationTile = findEmptyPoliceStationTile();
		movable.setCurrentTile(policeStationTile);
		if(movable instanceof Crook)
			policeStationTile.interact(movable);
	}

	private PoliceStationTile findEmptyPoliceStationTile() {
		for (PoliceStationTile tile : policeStationTiles) {
			if (!tile.isOccupied()) {
				return tile;
			}
		}
		// Should always be room in policehouse.
		assert false;
		return null;
	}

	void notifyWhatICollidedWith(AbstractPawn movable) {
		if (movable == null) {
			throw new IllegalArgumentException("Movable not allowed to be null");
		}
		for (Player player : players) {
			for (AbstractPawn pawn : player.getPawns()) {
				if (pawn.getCurrentTile() == movable.getCurrentTile() && pawn != movable) {
					movable.collisionAfterMove(pawn);
				}
			}
		}
	}

	void findPlayerAndAddCash(int cash, AbstractPawn movable) {
		for (Player player : players) {
			if (player.getPawns().contains(movable)) {
				player.getWallet().incrementCash(cash);
			}
		}
	}

	@Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	void hinderGetAway(IntelligenceAgencyTile intelligenceAgencyTile) {
		if (intelligenceAgencyTile == null) {
			throw new IllegalArgumentException("Intelligence Agency not allowed to be null");
		}
		intelligenceAgencyTile.hinderGetAway(players);
	}

	void pawnSelected(AbstractPawn pawn) {
		if (currentPlayer.getPlayerRole() == Role.Police) {
			currentPlayer.setCurrentPawn(pawn);
		}
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public Collection<? extends IPlayer> getPlayers(){
		return Collections.unmodifiableCollection(this.players);
	}

	boolean checkIfWantedCrookAt(IWalkableTile tile) {
		for (Player player : players) {
			for (AbstractPawn pawn : player.getPawns()) {
				if (pawn instanceof Crook && pawn.getCurrentTile() == tile) {
					Crook crook = (Crook)pawn;
					return crook.isWanted();
				}
			}
		}
		return false;
	}
}