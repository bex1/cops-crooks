package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * A player in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Player implements IObservable {
	private IMediator mediator;
	
	private List<IMovable> pawns;
	private IMovable currentPawn;
	private int diceResult;
	private List<TilePath> possiblePaths;
	
	private Role playerRole;
	
	private String name;
	
	private Wallet wallet;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROPERTY_POSSIBLE_PATHS = "PossiblePaths";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";
	public static final String PROPERTY_CURRENT_PAWN = "CurrentPawn";

	/**
	 * Initializes a new player.
	 * 
	 * @param name the name of the player. Allowed to be null.
	 * @param pawns the pawns controlled by the player, not allowed to be null or empty.
	 * The role of the pawns must match the role of the player.
	 * @param role the role of the player.
	 * @param mediator module communication unit. Not allowed to be null.
	 */
	public Player(String name, List<IMovable> pawns, Role role, IMediator mediator) {
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
		for (IMovable pawn : pawns) {
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
	
	/**
	 * Returns the type of player this is.
	 * @return the type of player this is.
	 */
	public Role getPlayerRole() {
		return playerRole;
	}
	
	/**
	 * Returns an unmodifiable collection of the pawns which the player controls.
	 * 
	 * @return an unmodifiable collection of the pawns which the player controls.
	 */
    public Collection<IMovable> getPawns() {
        return Collections.unmodifiableList(pawns);
    }
    
    /**
     * Returns the current pawn of the player.
     * 
     * @return the current pawn of the player.
     */
    public IMovable getCurrentPawn() {
    	return currentPawn;
    }
    
    /**
     * Sets the currentpawn of the player to the specified
     * if its really one of the players pawns.
     * 
     * @param pawn The pawn to set as the currentpawn. Has to be one of the player's pawns.
     */
    public void setCurrentPawn(IMovable pawn) {
    	if (pawns.contains(pawn)) {
    		IMovable oldValue = currentPawn;
    		currentPawn = pawn;
    		pcs.firePropertyChange(PROPERTY_CURRENT_PAWN, oldValue, currentPawn);
    	}
    }
    
    /**
     * Returns the name of the player.
     * 
     * @return the name of the player.
     */
    public String getName() {
    	return name;
    }
    
    /**
     * Returns the wallet of the player.
     * 
     * @return the wallet of the player.
     */
    public Wallet getWallet() {
    	return wallet;
    }
    
    /**
     * Roll the dice.
     */
    public void rollDice() {
    	//int oldValue = diceResult;
    	diceResult = mediator.rollDice();
    	pcs.firePropertyChange(PROPERTY_DICE_RESULT, -1, diceResult);
    	updatePossiblePaths();
    }
    
    /**
     * Updates the possible paths that the pawn can move in.
     */
    private void updatePossiblePaths() {
    	int steps = diceResult * currentPawn.tilesMovedEachStep();
    	possiblePaths = mediator.getPossiblePaths(currentPawn.getPawnType(), currentPawn, steps);
    	pcs.firePropertyChange(PROPERTY_POSSIBLE_PATHS, null, possiblePaths);
    }

    /**
     * The player choose a path and the current pawn then moves along it.
     * 
     * @param path The selected path.
     */
    public void choosePath(TilePath path){
    	if (possiblePaths != null && possiblePaths.contains(path)) {
    		possiblePaths = null;
    		// The path passed the test -> move
    		currentPawn.move(path);
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
}
