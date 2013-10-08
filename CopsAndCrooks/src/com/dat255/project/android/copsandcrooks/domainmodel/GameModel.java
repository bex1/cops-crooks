package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.Turn.MoveType;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Point;

public final class GameModel implements IObservable  {

	private final List<Player> players;
	private final List<PoliceStationTile> policeStationTiles;
	private final List<HideoutTile> hideoutTiles;
	private Player currentPlayer;
	private final Player playerClient;
	private final PropertyChangeSupport pcs;
	private final Dice dice;
	private final IMediator mediator;
	private boolean isChangingPlayer;
	private float changePlayerTimer;
	private float changePlayerDelay;

	// Added only because of you need to be able to get them when you load a hosted game
	private final IWalkableTile[][] walkable;
	private final Collection<TramLine> tramLines;
	private GameState state;
	private Turn currentTurn;
	private LinkedList<Turn> replayTurns;
	
	public static final String PROPERTY_CURRENT_PLAYER = "CurrentPlayer";
	public static final String PROPERTY_GAME_ENDED = "GameEnded";
	public static final String PROPERTY_GAMESTATE = "GameState";
	
	public enum GameState {
		Replay,
		Playing,
		Waiting,
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

		this.mediator = mediator;
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
		for (Player player : players) {
			if (player.getPlayerRole() == Role.Cop) {
				this.currentPlayer = player;
			}
		}
		if (currentPlayer == playerClient) {
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
		this.replayTurns = turns;
		state = GameState.Replay;
		pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
	}
	
	public void replay() {
		state = GameState.Replay;
		replay(replayTurns.pollFirst());
	}
	
	// TODO private
	private void replay(Turn turn) {
		// Test
		this.currentTurn = turn;
		turn = new Turn();
		turn.setPawnID(currentPlayer.getCurrentPawn().getID());
		turn.setMoveType(MoveType.Walk);
		TilePath path = new TilePath();
		Point pos = currentPlayer.getCurrentPawn().getCurrentTile().getPosition();
		
		AbstractWalkableTile endT = new RoadTile(new Point(pos.x, pos.y + 4), mediator);
		path.addTileLast(endT);
		path.addTileLast(new RoadTile(new Point(pos.x, pos.y + 3), mediator));
		path.addTileLast(new RoadTile(new Point(pos.x, pos.y + 2), mediator));
		path.addTileLast(new RoadTile(new Point(pos.x, pos.y + 1), mediator));
		turn.setPathWalked(path);
		turn.setEndTile(endT);
		
		
		
		AbstractPawn pawn = findPawnByID(turn.getPawnID());
		IWalkableTile end = turn.getEndTile();
		if (pawn != null && end != null) {
			switch (turn.getMoveType()) {
			case Metro:
				if (end instanceof TramStopTile) {
					pawn.moveByTram((TramStopTile)end);
				}
				break;
			case Walk:
				pawn.move(turn.getPathWalked());
				break;
			default:
				assert false;
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
		}
	}

	private void changePlayer() {
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
				replay(replayTurns.pollFirst());
			} else {
				state = GameState.Waiting;
				pcs.firePropertyChange(PROPERTY_GAMESTATE, null, state);
			}
		} else {
			nextPlayer(3f);
		}
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