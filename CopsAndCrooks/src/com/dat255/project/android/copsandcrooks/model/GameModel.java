package com.dat255.project.android.copsandcrooks.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;
/**
 * The game model in cops & crooks.
 * 
 * Note that all instances in the domainmodel can only be instantiated via the modelfactory outside of the module.
 * The functionality of the domainmodel instances are limited via public API (mostly interfaces), to only give the view and control
 * the access they need and secure the state of the model instances.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public final class GameModel implements IObservable{
	
	private static final long serialVersionUID = 88415277521336454L;

	private final String id;
	private final List<Player> players;
	private final List<PoliceStationTile> policeStationTiles;
	private final List<HideoutTile> hideoutTiles;
	private final List<TravelAgencyTile> travelAgencyTiles;
	private Player currentPlayer;
	private final Player playerClient;
	private final PropertyChangeSupport pcs;
	private final Dice dice;

	private boolean isChangingPlayer;
	private float changePlayerTimer;
	private float changePlayerDelay;
	private int turnID;

	// Added only because you need to be able to get them when you load a hosted game
	private final AbstractWalkableTile[][] walkable;
	private final Collection<MetroLine> metroLines;

	private GameState state;
	private Turn currentTurn;
	private LinkedList<Turn> replayTurns;

	public static final String PROPERTY_GAMESTATE = "GameState";

	private final String gameName;
	

	public enum GameState {
		REPLAY_RECEIVED,
		REPLAYING,
		PLAYING,
		WAITING,
		ENDED,
	}

	GameModel(final IMediator mediator, final Player playerClient, final List<Player> players, final AbstractWalkableTile[][] tiles, 
			  final Collection<MetroLine> metroLines, final Dice dice, String gameName, String id) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		if (players == null || players.isEmpty())
			throw new IllegalArgumentException("Players not allowed to be null or empty");
		if (playerClient == null || !players.contains(playerClient))
			throw new IllegalArgumentException("The player of this instance not allowed to be null and has to be in the players list");
		if (tiles == null)
			throw new IllegalArgumentException("Tiles not allowed to be null");

		this.gameName = gameName;
		this.playerClient = playerClient;
		this.players = players;
		this.walkable = tiles;
		this.metroLines = metroLines;
		this.dice = dice;
		this.id = id;
		this.pcs = new PropertyChangeSupport(this);
		mediator.registerGameModel(this);
		
		for (Player player : players) {
			if (player.getPlayerRole() == Role.Cop) {
				this.currentPlayer = player;
			}
		}

		policeStationTiles = new ArrayList<PoliceStationTile>();
		hideoutTiles = new ArrayList<HideoutTile>();
		travelAgencyTiles = new ArrayList<TravelAgencyTile>();

		// Extract police station tiles
		for (IWalkableTile[] tileArray : tiles) {
			for (IWalkableTile tile : tileArray) {
				if (tile instanceof PoliceStationTile) {
					policeStationTiles.add((PoliceStationTile)tile);
				} else if (tile instanceof HideoutTile) {
					hideoutTiles.add((HideoutTile)tile);
				} else if (tile instanceof TravelAgencyTile) {
					travelAgencyTiles.add((TravelAgencyTile)tile);
				}
			}
		}
		

		if (isLocalPlayersTurn()) {
			this.state = GameState.PLAYING;
			if (this.currentTurn == null) {
				this.currentTurn = new Turn();
				AbstractPawn pawn = this.currentPlayer.getCurrentPawn();
				this.currentTurn.setPawnID(pawn.getID());
				if (pawn.getCurrentTile() != null) {
					this.currentTurn.setEndTile(pawn.getCurrentTile());
				}
				this.currentTurn.setTurnID(turnID);
				this.currentPlayer.updateState();
			}
		} else {
			this.state = GameState.WAITING;
		}
	}

	/**
	 * Starts the game
	 */
	public void startGame(){
		if (currentPlayer != null) {
			AbstractPawn pawn = currentPlayer.getCurrentPawn();
			if (pawn.isActivePawn()) {
				// Just to trigger camera focus.
				pawn.setIsActivePawn(true);
			}
			// Resume related
			if (pawn.isMoving() || (state == GameState.REPLAYING)) {
				return;
			}
			if (currentPlayer == playerClient) {
				
				if (currentPlayer.isGoingByDice() || currentPlayer.isGoingByMetro()) {
					currentPlayer.updatePossiblePaths();
					return;
				}
				if (pawn instanceof Crook) {
					Crook crook = (Crook)pawn;
					// When on hideout and the crook has not selected any option, we show the table again.
					if (crook.isInHideout() && !isChangingPlayer) {
						HideoutTile hideout = (HideoutTile)crook.getCurrentTile();
						hideout.interact(crook);
						return;
					}
				}
			}
		}
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	/**
	 * Updates the game
	 * @param deltaTime - a timer
	 */
	public void update(float deltaTime) {
		if (isChangingPlayer) {
			changePlayerTimer += deltaTime;
			if (changePlayerTimer >= changePlayerDelay) {
				changePlayer();
				isChangingPlayer = false;
				changePlayerTimer = 0;
			}
		}
	}
	/**
	 * Adds turn to replay
	 * @param turns - a list of turns to be replayed 
	 */
	public void addReplayTurns(LinkedList<Turn> turns) {
		if(turns==null || turns.size()==0)
			return;

		this.replayTurns = turns;
		state = GameState.REPLAY_RECEIVED;
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	/**
	 * Replays what has happened.
	 */
	public void replay() {
		state = GameState.REPLAYING;
		if(!replayTurns.isEmpty())
			currentPlayer.updateState();
			replay(replayTurns.removeFirst());
	}

	private void replay(Turn turn) {
		this.currentTurn = turn;
		AbstractPawn pawn = findPawnByID(turn.getPawnID());
		if (turn.didEscapeFromPrison() && pawn instanceof Crook) {
			Crook crook = (Crook)pawn;
			crook.setWanted(true);
		}
		IWalkableTile end = walkable[turn.getEndTilePos().x][turn.getEndTilePos().y];
		if (pawn != null) {
			switch (turn.getMoveType()) {
			case METRO:
				if (end instanceof MetroStopTile) {
					pawn.moveByTram((MetroStopTile)end);
					nextPlayer(Values.DELAY_CHANGE_PLAYER_MOVE_BY_METRO);
				}
				break;
			case WALK:
				List<Point> pathWalked = turn.getPathWalked();
				TilePath tilePathWalked = new TilePath();
				for(Point point : pathWalked)
					tilePathWalked.addTileLast(getWalkabletiles()[point.x][point.y]);
				pawn.move(tilePathWalked);
				break;
			case NONE:
				nextPlayer(Values.DELAY_CHANGE_PLAYER_STANDARD);
				break;
			}
		}
	}

	private AbstractPawn findPawnByID(int pawnID) {
		for (Player player : players) {
			for (AbstractPawn pawn : player.getPawns()) {
				if (pawn.getID() == pawnID) {
					return pawn;
				}
			}
		}
		return null;
	}

	void nextPlayer(float delay){
		if (delay > 0) {
			isChangingPlayer = true;
			changePlayerDelay = delay;
			changePlayerTimer = 0;
		} else {
			
			changePlayer();
		}
		incrementTurnID();
	}

	private void changePlayer() {
		if(checkIfGameEnded()) {
			return;
		}
		currentPlayer.getCurrentPawn().setIsActivePawn(false);
		int i = players.indexOf(currentPlayer);
		do{
			currentPlayer = players.get((++i) % players.size());
			// When all the list of players have been looped,
			// and all players are inactive (all crooks have escaped),
			// currentPlayer is the same as before (police player).
			// The game should end then.
		}while (!currentPlayer.isActive());
		if (playerClient == currentPlayer) {
			state = GameState.PLAYING;
			currentPlayer.updateState();
			this.currentTurn = new Turn();
			AbstractPawn pawn = currentPlayer.getCurrentPawn();
			currentTurn.setPawnID(pawn.getID());
			if (pawn.getCurrentTile() != null) {
				currentTurn.setEndTile(pawn.getCurrentTile());
			}
			currentTurn.setTurnID(turnID);
			if (!currentPlayer.isActive()) {
				nextPlayer(Values.DELAY_CHANGE_PLAYER_STANDARD);
				state = GameState.WAITING;
				pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
				return;
			}
			pcs.firePropertyChange(PROPERTY_GAMESTATE, null, currentPlayer);
		} else if (state == GameState.REPLAYING && replayTurns.size() != 0) {
			currentPlayer.updateState();
			replay(replayTurns.removeFirst());
			if (checkIfGameEnded()) {
				return;
			}
		} else {
			state = GameState.WAITING;
			pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
		}
	}
	
	private boolean checkIfGameEnded() {
		int numberOfActivePlayers = 0;
		for (Player player : players) {
			if (player.isActive()) {
				numberOfActivePlayers++;
			}
		}
		if (numberOfActivePlayers < 2) {
			this.currentTurn = new Turn();
			AbstractPawn pawn = currentPlayer.getCurrentPawn();
			currentTurn.setPawnID(pawn.getID());
			if (pawn.getCurrentTile() != null) {
				currentTurn.setEndTile(pawn.getCurrentTile());
			}
			currentTurn.setTurnID(turnID);
			GameClient.getInstance().sendTurn(currentTurn);
			endGame();
			return true;
		}
		return false;
	}

	private boolean isLocalPlayersTurn() {
		return currentPlayer == playerClient;
	}
	/**
	 * Returns the current game state
	 * @return the current game state
	 */
	public GameState getGameState() {
		return state;
	}
	/**
	 * Returns the current turn
	 * @return the current turn
	 */
	public Turn getCurrentTurn() {
		return currentTurn;
	}

	private void endGame(){
		state = GameState.ENDED;
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	
	/**
	 * Returns the current player
	 * @return the current player
	 */
	public IPlayer getCurrentPlayer(){
		return currentPlayer;
	}
	/**
	 * Returns the player associated with a certain client
	 * @return the player associated with a certain client
	 */
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
		// Should always be room in police house.
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

	@Override
	public void removeObserver(PropertyChangeListener l) {
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
	/**
	 * Returns all the players
	 * @return all the players
	 */
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

	AbstractWalkableTile[][] getWalkabletiles(){
		return walkable.clone();
	}

	Collection<MetroLine> getTramLines() {
		return Collections.unmodifiableCollection(metroLines);
	}
	/**
	 * Returns all of the hideout tiles
	 * @return all of the hideout tiles
	 */
	public Collection<HideoutTile> getHideouts() {
		return Collections.unmodifiableCollection(hideoutTiles);
	}

	boolean isCurrentPlayerOwnerOfPawn(AbstractPawn movable) {
		return currentPlayer != null && currentPlayer.getPawns().contains(movable);
	}

	private void incrementTurnID(){
		turnID += 1;
	}
	/**
	 * Returns the ID of the turn
	 * @return the ID of the turn
	 */
	public int getTurnID(){
		return turnID;
	}
	/**
	 * Returns the name of the player
	 * @return the name of player
	 */
	public String getName() {
		return gameName +"";
	}


	/**
	 * 
	 * @return - A String that is the id for this model
	 */
	public String getID() {
		return id;
	}
	/**
	 * Returns the player that has the pawn.
	 * @param pawn - the pawn to find the owner to.
	 * @return the player that has the pawn
	 */
	public IPlayer getPlayerFor(IPawn pawn) {
		for (IPlayer player : players) {
			if (player.getPawns().contains(pawn)) {
				return player;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if game has ended, false otherwise.
	 * @return true if game has ended, false otherwise.
	 */
	public boolean gameEnded() {
		return state == GameState.ENDED;
	}

	public Dice getDice() {
		return dice;
	}

	void addCashToTravelAgency(int cash) {
		for (TravelAgencyTile tile : travelAgencyTiles) {
			tile.addCash(cash);
		}
	}
}