package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractPawn implements IMovable {
	protected IWalkableTile currentTile;
	
	private LinkedList<IWalkableTile> pathToWalk;
	private boolean isWalking;
	
	private static final float WALK_DELAY = 0.5f;
	private float walkTimer;

	@Override
	public void setCurrentTile(IWalkableTile currTile) {
		this.currentTile = currTile;
	}

	@Override
	public IWalkableTile getCurrentTile() {
		return currentTile;
	}

	@Override
	public void Move(LinkedList<IWalkableTile> path) {
		if (path == null || path.size() < 1) {
			throw new IllegalArgumentException("path is null or empty");
		}
		this.pathToWalk = path;
		this.isWalking = true;
	}

	@Override
	public void update(float deltaTime) {
		if (isWalking) {
			walkTimer += deltaTime;
		    if (walkTimer >= WALK_DELAY) {
		        currentTile = pathToWalk.pollFirst();
		        // TODO fire property change and catch in view
		        
		        isWalking = !pathToWalk.isEmpty();
		        	
		        // Reset timer
		        walkTimer -= WALK_DELAY;
		    }
		}
	}
}
