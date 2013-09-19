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
	private List<LinkedList<IWalkableTile>> possiblePaths;
	
	private Role playerRole;
	
	private String name;
	
	private Wallet wallet;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROPERTY_POSSIBLE_PATHS = "PossiblePaths";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";

	/**
	 * Initializes a new player.
	 * 
	 * @param name the name of the player.
	 * @param pawns the pawns controlled by the player, not allowed to be null or empty.
	 * The role of the pawns must match the role of the player.
	 * @param role the role of the player.
	 */
	public Player(String name, List<IMovable> pawns, Role role, IMediator mediator) {
		if (pawns == null || pawns.isEmpty()) {
			throw new IllegalArgumentException("pawns not allowed to be null or empty");
		}
		if (mediator == null) {
			throw new IllegalArgumentException("Mediator not allowed to be null");
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
    	int oldValue = diceResult;
    	diceResult = mediator.rollDice();
    	pcs.firePropertyChange(PROPERTY_DICE_RESULT, oldValue, diceResult);
    	updatePossiblePaths();
    }
    
    /**
     * Updates the possible paths that the pawn can move in.
     */
    public void updatePossiblePaths() {
    	int steps = diceResult * currentPawn.tilesMovedEachStep();
    	possiblePaths = mediator.getPossiblePaths(currentPawn.getPawnType(), currentPawn, steps);
    	pcs.firePropertyChange(PROPERTY_POSSIBLE_PATHS, null, possiblePaths);
    }
    
    // TODO Add method that moves along a chosen path. -> if we make a path class we just select one of the possible paths.
    // Dont forget to change diceResult to 0 after move...
    
    @Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
}
