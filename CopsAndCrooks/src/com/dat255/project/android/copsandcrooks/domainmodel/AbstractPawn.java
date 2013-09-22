package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IInteractiveTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.Constants;

/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractPawn implements IMovable {
	
	private Role pawnRole;
	private PawnType pawnType;
	
	// Used to communicate within the module
	protected final IMediator mediator;
	
	protected IWalkableTile currentTile;
	// TODO likely add a previous tile field so we know which tile we should animate the player move from
	private TilePath pathToMove;
	
	private boolean isMoving;
	private float moveTimer;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROPERTY_CURRENT_TILE = "CurrentTile";
	
	protected AbstractPawn(Role pawnRole, PawnType pawnType, IMediator mediator) {
		if (mediator == null) {
			throw new IllegalArgumentException("mediator not allowed to be null");
		}
		this.pawnRole = pawnRole;
		this.pawnType = pawnType;
		this.mediator = mediator;
	}

	@Override
	public void setCurrentTile(IWalkableTile currTile) {
		IWalkableTile oldTile = currentTile;
		this.currentTile = currTile;
		
		 // The current tiled changed, someone moved us -> notify
        pcs.firePropertyChange(PROPERTY_CURRENT_TILE, oldTile, currentTile);
		
	}

	@Override
	public IWalkableTile getCurrentTile() {
		return currentTile;
	}

	@Override
	public void move(TilePath path) {
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("path is null or empty");
		}
		this.pathToMove = path;
		this.isMoving = true;
	}

	@Override
	public void update(float deltaTime) {
		// Check if we are walking
		if (isMoving) {
			// Take steps with delay
			moveTimer += deltaTime;
		    if (moveTimer >= Constants.PAWN_MOVE_DELAY) {
		    	this.setCurrentTile(pathToMove.getNextTile());
		       
		        // Check if we stepped on the endtile of the path
		        if (pathToMove.isEmpty()) {
		        	isMoving = false;
		        	
		        	
		        	if (currentTile != null && currentTile.isOccupied()) {
		        		// We collided, communicate with the module via the mediator	
		        		mediator.didCollideAfterMove(this);
		        	}
		        	// Try to interact with the tile
		        	this.interactWithTile();
		        	
		        }
		        // Reset timer
		        moveTimer -= Constants.PAWN_MOVE_DELAY;
		    }
		}
	}
	
	private void interactWithTile() {
		// Check if the endTile is an interactive tile
		if (currentTile instanceof IInteractiveTile) {
			IInteractiveTile interactableTile = (IInteractiveTile) currentTile;
			// Lets interact with it
		    interactableTile.interact(this);
		}
	}
	
	@Override
	public Role getPawnRole() {
		return pawnRole;
	}

	@Override
	public PawnType getPawnType() {
		return pawnType;
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
