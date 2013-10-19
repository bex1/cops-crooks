package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.GameModel.GameState;
import com.dat255.project.android.copsandcrooks.domainmodel.Turn.MoveType;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A player in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Player implements IPlayer, Serializable {
	private final IMediator mediator;

	private final List<AbstractPawn> pawns;
	private AbstractPawn currentPawn;
	private int diceResult;
	private Collection<TilePath> possiblePaths;

	private final Role playerRole;

	private final String name;

	private final Wallet wallet;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private boolean goByMetro;
	private boolean goByDice;
	private boolean isActive;

	private final String playerID;

	/**
	 * Initializes a new player.
	 * 
	 * @param name the name of the player. Allowed to be null.
	 * @param pawns the pawns controlled by the player, not allowed to be null or empty.
	 * The role of the pawns must match the role of the player.
	 * @param role the role of the player.
	 * @param mediator module communication unit. Not allowed to be null.
	 * @param wallet 
	 */
	Player(String name, List<AbstractPawn> pawns, Role role, IMediator mediator, Wallet wallet, String id) {
		if (pawns == null || pawns.isEmpty()) {
			throw new IllegalArgumentException("pawns not allowed to be null or empty");
		}
		if (mediator == null) {
			throw new IllegalArgumentException("Mediator not allowed to be null");
		}
		if (name == null) {
			name = "";
		}
		// Check so the pawn roles match the player role.
		for (AbstractPawn pawn : pawns) {
			if (role != pawn.getPawnRole()) {
				throw new IllegalArgumentException("A " + role.name() + 
						" player may only control pawns of role " + role.name());
			}
		}
		this.name = name;
		this.pawns = pawns;
		this.playerRole = role;
		this.mediator = mediator;
		this.currentPawn = pawns.get(0);
		this.isActive = true;
		this.playerID = id;
		if(wallet == null)
			this.wallet = new Wallet();
		else
			this.wallet = wallet;
	}


	@Override
	public Role getPlayerRole() {
		return playerRole;
	}


	@Override
    public Collection<AbstractPawn> getPawns() {
        return Collections.unmodifiableCollection(pawns);
    }
    
    @Override
    public AbstractPawn getCurrentPawn() {
    	return currentPawn;
    }
        
    @Override
    public String getName() {
    	return name;
    }
    
    @Override
    public Wallet getWallet() {
    	return wallet;
    }
    
    void updateState() {
    	currentPawn.setIsActivePawn(true);
    	checkIfCrookIsEscaping();
    	checkIfLifeTimeInPrison();
    	checkIfCrookStillInPrison();
    }
    
    private void checkIfCrookStillInPrison() {
		if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.isInPrison()){
    			crook.decrementTurnsInPrison();
    		}
    	}
	}



    private void checkIfLifeTimeInPrison(){
    	if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.getTimesArrested() == Values.MAX_TIMES_ARRESTED){
    			crook.setIsntPlaying();
    			this.setActive(false);
    		}
    	}
    }
    
    private void checkIfCrookIsEscaping() {
		if (currentPawn instanceof Crook) {
			Crook crook = (Crook)currentPawn;
			if (crook.isAttemptingGetAway()) {
				// Take cash and add to player
				Wallet crookWallet = crook.getWallet();
				wallet.incrementCash(crookWallet.getCash());
				crook.setIsntPlaying();
				this.setActive(false);
			}
		}
	}
    
   
	private boolean isOnMetro(AbstractPawn pawn) {
    	if (pawn instanceof AbstractWalkingPawn) {
    		AbstractWalkingPawn walkingPawn = (AbstractWalkingPawn)pawn;
    		return walkingPawn.isWaitingOnTram();
    	}
    	return false;
    }
    	
    @Override
    public boolean isAnyWalkingPawnOnMetro(){
    	for(AbstractPawn pawn: pawns){
    		if (isOnMetro(pawn)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    @Override
    public boolean isGoingByMetro() {
    	return goByMetro;
    }
    
    @Override
    public boolean isGoingByDice() {
    	return goByDice;
    }

    @Override
    public void goByMetro() {
    	goByMetro = true;
    	if (mediator.checkState() == GameState.Playing) {
    		mediator.getCurrentTurn().setMoveType(MoveType.Metro);
    	}
    	updatePossiblePaths();
    }
    
    @Override
    public void rollDice() {
    	mediator.rollDice(this);
    }
    /**
     * Updates the possible paths the player's pawn can walk
     */
    void updatePossiblePaths() {
    	if (goByDice) {
    		int steps = diceResult * currentPawn.tilesMovedEachStep();
    		possiblePaths = mediator.getPossiblePaths(currentPawn, steps);
    	} else if (goByMetro && isOnMetro(currentPawn)) {
    		possiblePaths = mediator.getPossibleMetroPaths(currentPawn);
    	} else {
    		possiblePaths = null;
    	}
    	// No possible paths and crook... -> Next player

		if ((possiblePaths == null || possiblePaths.isEmpty()) && playerRole == Role.Crook) {
			mediator.playerTurnDone(2f);
			return;
		}
		pcs.firePropertyChange(PROPERTY_POSSIBLE_PATHS, null, possiblePaths);

	}

    @Override
	public Collection<TilePath> getPossiblePaths() {
		if (possiblePaths != null) {
			return Collections.unmodifiableCollection(possiblePaths);
		}
		return null;
	}

    @Override
    public void choosePath(TilePath path){
    	if (possiblePaths != null && possiblePaths.contains(path) && goByDice) {
    		possiblePaths = null;
    		if (mediator.checkState() == GameState.Playing) {
    			mediator.getCurrentTurn().setPawnID(currentPawn.getID());
    			mediator.getCurrentTurn().setPathWalked(new TilePath(path));
    			mediator.getCurrentTurn().setEndTile(path.getTile(0));
    		}
    		// The path passed the test -> move
    		currentPawn.move(path);
    		diceResult = 0;
    		goByDice = false;
    	}
    	goByDice = false;
    }
    
    @Override
    public void chooseMetroStop(TramStopTile metroStop){
    	if (possiblePaths != null && goByMetro) {
    		for (TilePath path : possiblePaths) {
    			if (path.contains(metroStop)) {
    				possiblePaths = null;
    				// The path passed the test -> move directly
    				currentPawn.moveByTram(metroStop);
    				goByMetro = false;
    				mediator.playerTurnDone(Values.DELAY_CHANGE_PLAYER_MOVE_BY_METRO);
    				if (mediator.checkState() == GameState.Playing) {
    					mediator.getCurrentTurn().setPawnID(currentPawn.getID());
    					mediator.getCurrentTurn().setEndTile(metroStop);
    				}
    				return;
    			}
    		}
    	}
    }
    
    
    void setCurrentPawn(AbstractPawn pawn){
    	if (pawns.contains(pawn)) {
    		if (mediator.checkState() == GameState.Playing) {
    			mediator.getCurrentTurn().setPawnID(currentPawn.getID());
    		}
    		currentPawn.setIsActivePawn(false);
    		AbstractPawn oldValue = currentPawn;
    		currentPawn = pawn;
    		currentPawn.setIsActivePawn(true);
    		pcs.firePropertyChange(PROPERTY_SELECTED_PAWN, oldValue, currentPawn);
    		updatePossiblePaths();
    	}
    }
    
    @Override

	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
	
	void diceResult(int result) {
		diceResult = result;
		pcs.firePropertyChange(PROPERTY_DICE_RESULT, -1, diceResult);
		// checks to see if the player's pawn is in prison
    	// if so then the player isn't able to move unless rolling a six.
		if(this.currentPawn instanceof Crook){
			Crook crook = ((Crook)this.currentPawn);
			if(crook.isInPrison() && crook.getTurnsInPrison() > 0){
				if (diceResult!=Values.DICE_RESULT_TO_ESCAPE) {
					mediator.playerTurnDone(Values.DELAY_CHANGE_PLAYER_IN_PRISON);
					return;
				} else {
					crook.setWanted(true);
				}
			}
		}
		goByDice = true;
		if (mediator.checkState() == GameState.Playing) {
			mediator.getCurrentTurn().setMoveType(MoveType.Walk);
		}
    	updatePossiblePaths();
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean active) {
		isActive = active;
	}


	@Override
	public String getID() {
		return playerID;
	}
}
