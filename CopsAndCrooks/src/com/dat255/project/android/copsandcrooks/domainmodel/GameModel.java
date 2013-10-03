package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

public final class GameModel implements IObservable  {

	private final List<Player> players;
	private final List<PoliceStationTile> policeStationTiles;
	private final List<HideoutTile> hideoutTiles;
	private Player currentPlayer;
	private final Player playerClient;
	private final PropertyChangeSupport pcs;
	private final Dice dice;

	// Added only because of you need to be able to get them when you load a hosted game
	private final IWalkableTile[][] walkable;
	private final Collection<TramLine> tramLines;
	
	public static final String PROPERTY_CURRENT_PLAYER = "CurrentPlayer";
	public static final String PROPERTY_GAME_ENDED = "GameEnded";
	
	private class ChangePlayerTask extends Task {

		@Override
		public void run () {
			currentPlayer.getCurrentPawn().setIsActivePawn(false);
			changePlayer();
			//changePlayerTimer.stop();
			this.cancel();
		}
	}

	public GameModel(final IMediator mediator, final Player playerClient, final List<Player> players, final IWalkableTile[][] tiles, Collection<TramLine> tramLines) {
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
		this.dice = new Dice(mediator);
		this.walkable = tiles;
		this.tramLines = tramLines;
		mediator.registerGameModel(this);

		policeStationTiles = new ArrayList<PoliceStationTile>();
		hideoutTiles = new ArrayList<HideoutTile>();

		// Extract police station tiles
		for (IWalkableTile[] tileArray : tiles) {
			for (IWalkableTile tile : tileArray) {
				if (tile instanceof PoliceStationTile) {
					policeStationTiles.add((PoliceStationTile)tile);
				} else if (tile instanceof HideoutTile){
					hideoutTiles.add((HideoutTile)tile);
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

	void nextPlayer(float delay){
		if (delay > 0) {
			Timer.schedule(new ChangePlayerTask(), delay);
		} else {
			changePlayer();
		}
	}

	private void changePlayer() {
		Player _currentPlayer = currentPlayer;
		int i = players.indexOf(currentPlayer);
		do{
			currentPlayer = players.get((i + 1) % players.size());
			// When all the list of players have been looped,
			// and all players are inactive (all crooks have escaped),
			// currentPlayer is the same as before (police player).
			// The game should end then.
			if(currentPlayer==_currentPlayer) {
				endGame();
				return;
			}
		}while (!currentPlayer.isActive());
		currentPlayer.updateState();
		if (currentPlayer.isActive()) {
			pcs.firePropertyChange(PROPERTY_CURRENT_PLAYER, null, currentPlayer);
		} else {
			nextPlayer(3f);
		}
	}

	private void endGame(){
		pcs.firePropertyChange(PROPERTY_GAME_ENDED, null, currentPlayer);
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
		if (currentPlayer.getPlayerRole() == Role.Cop) {
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
	
	public Dice getDice(){
		return dice;
	}
	
	public IWalkableTile[][] getWalkabletiles(){
		return walkable;
	}

	public Collection<TramLine> getTramLines() {
		return Collections.unmodifiableCollection(tramLines);
	}

	public Collection<HideoutTile> getHideouts() {
		return Collections.unmodifiableCollection(hideoutTiles);
	}

	public boolean isCurrentPlayerOwnerOfPawn(AbstractPawn movable) {
		return currentPlayer != null && currentPlayer.getPawns().contains(movable);
	}
}