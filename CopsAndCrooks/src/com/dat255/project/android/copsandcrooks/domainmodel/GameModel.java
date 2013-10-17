package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Point;

public final class GameModel implements IObservable, Serializable{

	private final int id;
	private final List<Player> players;
	private final List<PoliceStationTile> policeStationTiles;
	private final List<HideoutTile> hideoutTiles;
	private Player currentPlayer;
	private final Player playerClient;
	private final PropertyChangeSupport pcs;
	
	private boolean isChangingPlayer;
	private float changePlayerTimer;
	private float changePlayerDelay;
	private int turnID = 0;
	
	private Dice dice;
	
	// Added only because you need to be able to get them when you load a hosted game
	private final AbstractWalkableTile[][] walkable;
	private final Collection<TramLine> tramLines;
	
	private GameState state;
	private Turn currentTurn;
	private LinkedList<Turn> replayTurns;
	
	public static final String PROPERTY_GAME_ENDED = "GameEnded";
	public static final String PROPERTY_GAMESTATE = "GameState";
	
	private final String gameName;
	
	public enum GameState {
		Replay,
		Playing,
		Waiting,
	}
	
	GameModel(final IMediator mediator, final Player playerClient, final Player currentPlayer, final List<Player> players, final AbstractWalkableTile[][] tiles, Collection<TramLine> tramLines, String gameName, int id, int turnID, int diceResult) {
		this(mediator, playerClient, currentPlayer ,players, tiles, tramLines, gameName, id, turnID);
		for(Player player: this.players){
			if(player.getID().equals(this.currentPlayer)){
				break;
			}
		}
		
	}

	GameModel(final IMediator mediator, final Player playerClient, final Player currentPlayer, final List<Player> players, final AbstractWalkableTile[][] tiles, Collection<TramLine> tramLines, String gameName, int id, int turnID) {
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
		this.tramLines = tramLines;
		this.dice = Dice.getInstance();
		this.id = id;
		this.turnID = turnID;
		mediator.registerDice(Dice.getInstance());
		mediator.registerGameModel(this);
		
		if(currentPlayer != null){
			for(Player player: this.players){
				if(player.getID() == currentPlayer.getID()){
					this.currentPlayer = player;
					break;
				}
			}
		}

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
		if(currentPlayer == null){
			for (Player player : players) {
				if (player.getPlayerRole() == Role.Cop) {
					this.currentPlayer = player;
				}
			}
		}
		if (isLocalPlayersTurn()) {
			state = GameState.Playing;
			this.currentTurn = new Turn();
			currentPlayer.updateState();
		} else {
			state = GameState.Waiting;
		}
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	
	public void update(float deltaTime) {
		if (isChangingPlayer) {
			changePlayerTimer += deltaTime;
			if (changePlayerTimer >= changePlayerDelay) {
				currentPlayer.getCurrentPawn().setIsActivePawn(false);
				changePlayer();
				isChangingPlayer = false;
				changePlayerTimer = 0;
			}
		}
	}
	
	public void addReplayTurns(LinkedList<Turn> turns) {
		if(turns==null || turns.size()==0)
			return;
		
		this.replayTurns = turns;
		state = GameState.Replay;
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	
	public void replay() {
		state = GameState.Replay;
		replay(replayTurns.removeFirst());	
	}
	
	private void replay(Turn turn) {
		this.currentTurn = turn;
		AbstractPawn pawn = findPawnByID(turn.getPawnID());
		IWalkableTile end = getWalkabletiles()[turn.getEndTilePos().x][turn.getEndTilePos().y];
		if (pawn != null && end != null) {
			switch (turn.getMoveType()) {
			case Metro:
				if (end instanceof TramStopTile) {
					pawn.moveByTram((TramStopTile)end);
					nextPlayer(3f);
				}
				break;
			case Walk:
				List<Point> pathWalked = turn.getPathWalked();
				TilePath tilePathWalked = new TilePath();
				for(Point point : pathWalked)
					tilePathWalked.addTileLast(getWalkabletiles()[point.x][point.y]);
				pawn.move(tilePathWalked);
				break;
			default:
				nextPlayer(2f);
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
			currentPlayer.getCurrentPawn().setIsActivePawn(false);
			changePlayer();
			dice.setResult(-1);
		}
		incrementTurnID();
	}

	private void changePlayer() {
		// sent the latest turn to the server
		if(isLocalPlayersTurn())
			GameClient.getInstance().sendTurn(currentTurn);
		
		Player previousPlayer = currentPlayer;
		int i = players.indexOf(currentPlayer);
		do{
			currentPlayer = players.get((++i) % players.size());
			// When all the list of players have been looped,
			// and all players are inactive (all crooks have escaped),
			// currentPlayer is the same as before (police player).
			// The game should end then.
			if(currentPlayer==previousPlayer) {
				endGame();
				// tell server that the game has ended
				if(isLocalPlayersTurn())
					GameClient.getInstance().sendGameEnd();
				return;
			}
		}while (!currentPlayer.isActive());
		currentPlayer.updateState();
		if (currentPlayer.isActive()) {
			if (playerClient == currentPlayer) {
				this.currentTurn = new Turn();
				state = GameState.Playing;
				pcs.firePropertyChange(PROPERTY_GAMESTATE, null, currentPlayer);
			} else if (state == GameState.Replay) {
				replay(replayTurns.removeFirst());
			} else {
				state = GameState.Waiting;
				pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
			}
		} else {
			nextPlayer(3f);
		}
	}

	private boolean isLocalPlayersTurn() {
		return currentPlayer == playerClient;
	}

	public GameState getGameState() {
		return state;
	}
	
	Turn getCurrentTurn() {
		return currentTurn;
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

	Collection<TramLine> getTramLines() {
		return Collections.unmodifiableCollection(tramLines);
	}

	public Collection<HideoutTile> getHideouts() {
		return Collections.unmodifiableCollection(hideoutTiles);
	}

	boolean isCurrentPlayerOwnerOfPawn(AbstractPawn movable) {
		return currentPlayer != null && currentPlayer.getPawns().contains(movable);
	}
	
	int getDiceResults(){
		return dice.getResult();
	}
	
	private void incrementTurnID(){
		turnID += 1;
	}
	
	public int getTurnID(){
		return turnID;
	}

	public String getName() {
		return gameName +"";
	}

	public int getID() {
		return id;
	}
}