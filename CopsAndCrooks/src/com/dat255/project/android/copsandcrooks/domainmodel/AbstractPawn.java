package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IInteractiveTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractPawn implements IMovable {
	
	protected IWalkableTile currentTile;
	
	private LinkedList<IWalkableTile> pathToMove;
	private boolean isMoving;
	
	private static final float MOVE_DELAY = 0.5f;
	private float moveTimer;
	
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	@Override
	public void setCurrentTile(IWalkableTile currTile) {
		IWalkableTile oldTile = currentTile;
		this.currentTile = currTile;
		
		 // The current tiled changed, someone moved us -> notify
        pcs.firePropertyChange("CurrentTile", oldTile, currentTile);
		
	}

	@Override
	public IWalkableTile getCurrentTile() {
		return currentTile;
	}

	@Override
	public void move(LinkedList<IWalkableTile> path) {
		if (path == null || path.size() < 1) {
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
		    if (moveTimer >= MOVE_DELAY) {
		    	this.setCurrentTile(pathToMove.pollFirst());
		       
		        // Check if we stepped on the endtile of the path
		        if (pathToMove.isEmpty()) {
		        	isMoving = false;
		        	
		        	// Try to interact with the tile
		        	this.interactWithTile();
		        }
		        	
		        // Reset timer
		        moveTimer -= MOVE_DELAY;
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
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
	
}
