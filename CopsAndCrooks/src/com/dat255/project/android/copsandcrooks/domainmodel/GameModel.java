package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.PoliceStationTile;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.TravelAgencyTile;

public class GameModel implements IObservable  {
	
	private List<Player> players;
	private List<PoliceStationTile> policeStationTiles;
	private Player currentPlayer;
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public static final String PROPERTY_NEW_TURN_CROOK = "ACrooksTurn";
	public static final String PROPERTY_NEW_TURN_POLICE = "APoliceTurn";
	public static final String PROPERTY_NEW_ROUND = "NewRound";
	
	private TravelAgencyTile travelAgency;


	public GameModel(IMediator mediator, List<Player> players, IWalkableTile[][] tiles) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		if (players == null || players.isEmpty())
			throw new IllegalArgumentException("Players not allowed to be null or empty");
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");
		
		this.players = players;
		currentPlayer = players.get(0);
		
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
		
		currentPlayer = players.get(0);
	}
	
	public void nextPlayer(){
		Player oldPlayer = currentPlayer;
		int i;
		for (i = 0; i < players.size(); i++){
			if(players.equals(currentPlayer)){
				break;
			}
		}
		if(i < players.size()-1){
			currentPlayer= players.get(i+1);
			pcs.firePropertyChange(PROPERTY_NEW_TURN_CROOK, oldPlayer, currentPlayer);
		}else{
			currentPlayer= players.get(0);
			pcs.firePropertyChange(PROPERTY_NEW_TURN_POLICE, oldPlayer, currentPlayer);
			pcs.firePropertyChange(PROPERTY_NEW_ROUND, oldPlayer, currentPlayer);
		}
	}
	
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	

	void moveToEmptyPoliceStationTile(IMovable movable) {
		PoliceStationTile policeStationTile = findEmptyPoliceStationTile();
		movable.setCurrentTile(policeStationTile);
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

	void notifyWhatICollidedWith(IMovable movable) {
		if (movable == null) {
			throw new IllegalArgumentException("Movable not allowed to be null");
		}
		for (Player player : players) {
			for (IMovable pawn : player.getPawns()) {
				if (pawn.getCurrentTile() == movable.getCurrentTile() && pawn != movable) {
					movable.collisionAfterMove(pawn);
				}
			}
		}
	}

	void findPlayerAndAddCash(int cash, IMovable movable) {
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

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}
	public List<Player> getPlayers(){
		return this.players;
	}
}
