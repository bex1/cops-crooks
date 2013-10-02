package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A player in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Player implements IPlayer {
	private IMediator mediator;

	private List<AbstractPawn> pawns;
	private AbstractPawn currentPawn;
	private int diceResult;
	private Collection<TilePath> possiblePaths;

	private Role playerRole;

	private String name;

	private Wallet wallet;

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private boolean goByMetro;

	private boolean goByDice;
	public static final String PROPERTY_POSSIBLE_PATHS = "PossiblePaths";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";
	public static final String PROPERTY_CHOOSEN_PAWN = "TheSelectedPawn";

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
    
    void updateState() {
    	checkIfCrookIsEscaping();
    	checkIfLifeTimeInPrison();
    	
    }
    
    private void checkIfLifeTimeInPrison(){
    	if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.getTimesArrested() == 4){
    			crook.setIsPlaying(false);
    			//TODO: inactivate player
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
				crook.setIsPlaying(false);
				crook.setCurrentTile(null);
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
    	updatePossiblePaths();
    }
    
    @Override
    public void rollDice() {
    	// checks to see if the player's pawn is in prison
    	// if so then the player isn't ablt to move unless rolling a six.
    	if(this.currentPawn instanceof Crook){
    		Crook crook = ((Crook)this.currentPawn);
    		if(crook.isInPrison() && diceResult!=6 && crook.getTurnsInPrison() > 0){
    			crook.decrementTurnsInPrison();
    			mediator.playerTurnDone();
    			return;
    		}
    	}
    	
    	mediator.rollDice(this);
    }
    
    @Override
    public void updatePossiblePaths() {
    	if (goByDice) {
    		int steps = diceResult * currentPawn.tilesMovedEachStep();
    		possiblePaths = mediator.getPossiblePaths(currentPawn.getPawnType(), currentPawn, steps);
    	} else if (goByMetro && isOnMetro(currentPawn)) {
    		possiblePaths = mediator.getPossibleMetroPaths(currentPawn);
    	}
    	// No possible paths and crook... -> Next player

		if ((possiblePaths == null || possiblePaths.isEmpty()) && playerRole == Role.Crook) {
			mediator.playerTurnDone();
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
    				currentPawn.setCurrentTile(metroStop);
    				goByMetro = false;
    				mediator.playerTurnDone();
    				return;
    			}
    		}
    	}
    }
    
    /**
     * Sets the current pawn of the player to the specified
     * if its really one of the players pawns.
     * 
     * @param pawn The pawn to set as the currentpawn. Has to be one of the player's pawns.
     */
    void setCurrentPawn(AbstractPawn pawn){
    	if (pawns.contains(pawn)) {
    		AbstractPawn oldValue = currentPawn;
    		currentPawn = pawn;
    		pcs.firePropertyChange(PROPERTY_CHOOSEN_PAWN, oldValue, currentPawn);
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
    	goByDice = true;
    	updatePossiblePaths();
	}
}
