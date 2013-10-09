package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.Turn.MoveType;

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

	private int playerID;

	public static final String PROPERTY_POSSIBLE_PATHS = "PossiblePaths";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";
	public static final String PROPERTY_SELECTED_PAWN = "TheSelectedPawn";
	public static final String PROPERTY_IS_IN_PRISON = "IsInPrison";


	/**
	 * Initializes a new player.
	 * 
	 * @param name the name of the player. Allowed to be null.
	 * @param pawns the pawns controlled by the player, not allowed to be null or empty.
	 * The role of the pawns must match the role of the player.
	 * @param role the role of the player.
	 * @param mediator module communication unit. Not allowed to be null.
	 */
	public Player(String name, List<AbstractPawn> pawns, Role role, IMediator mediator) {
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
		wallet = new Wallet();
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
    /**
     * Checks for update every turn.
     */
    void updateState() {
    	currentPawn.setIsActivePawn(true);
    	checkIfCrookIsEscaping();
    	checkIfLifeTimeInPrison(); 
    	checkIfInPrison();
    }
    /**
     * Checks to see if the crook has been arrested four times
     * if so, the game is over
     */
    private void checkIfLifeTimeInPrison(){
    	if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.getTimesArrested() == 4){
    			crook.setIsntPlaying();
    			this.setActive(false);
    		}
    	}
    }
    /**
     * Checks to see if the crook is attempting to escape, thus finising the game session
     */
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
    /**
     * Checks to see if the crook is in prison
     */
    private void checkIfInPrison(){
    	if(this.currentPawn instanceof Crook){
    		Crook crook = (Crook)this.currentPawn;
    		if(crook.isInPrison()){
    			pcs.firePropertyChange(PROPERTY_IS_IN_PRISON, false, true);
    		}
    	}
    }
    /**
     * Checks if the pawn is standing on a tram stop 
     * @param pawn - the pawn currently active
     * @return true - if the pawn is standing on a tram stop
     */
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
    	mediator.getCurrentTurn().setMoveType(MoveType.Metro);
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
			mediator.playerTurnDone(0);
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
    		mediator.getCurrentTurn().setPawnID(currentPawn.getID());
    		mediator.getCurrentTurn().setPathWalked(new TilePath(path));
    		mediator.getCurrentTurn().setEndTile(path.getTile(0));
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
    				mediator.playerTurnDone(3f);
    				mediator.getCurrentTurn().setPawnID(currentPawn.getID());
    				mediator.getCurrentTurn().setEndTile(metroStop);
    				return;
    			}
    		}
    	}
    }
    
    /**
     * Sets the current pawn of the player to the specified
     * if its really one of the players pawns.
     * 
     * @param pawn The pawn to set as the currentPawn. Has to be one of the player's pawns.
     */
    void setCurrentPawn(AbstractPawn pawn){
    	if (pawns.contains(pawn)) {
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
	/**
	 * Takes the dice result and updates possible paths accordingly
	 * If the player is a crook and is currently in prison, the only allowed result is then 6
	 * @param result - the result from the die cast
	 */
	void diceResult(int result) {
		diceResult = result;
		pcs.firePropertyChange(PROPERTY_DICE_RESULT, -1, diceResult);
		// checks to see if the player's pawn is in prison
    	// if so then the player isn't able to move unless rolling a six.
    	if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.isInPrison() && diceResult!=6 && crook.getTurnsInPrison() > 0){
    			crook.decrementTurnsInPrison();
    			mediator.playerTurnDone(3f);
    			return;
    		}
    	}
    	goByDice = true;
    	mediator.getCurrentTurn().setMoveType(MoveType.Walk);
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
	public int getID() {
		return playerID;
	}
}
