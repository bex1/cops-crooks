package com.dat255.project.android.copsandcrooks.domainmodel;

import java.util.LinkedList;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Interface describing a movable object.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IMovable extends IObservable {
	
	/**
	 * Updates the state of the movable.
	 * @param deltaTime the time between current frame and last.
	 */
	public void update(float deltaTime);
	
	/**
	 * Returns the tile on which the movable is currently standing on.
	 * @return the walkable tile on which the movable is currently standing on.
	 */
	IWalkableTile getCurrentTile();
	
	/**
	 * Sets the current tile on which the movable is standing on.
	 */
	void setCurrentTile(IWalkableTile currTile);
	
	/**
	 * Moves the movable object along the given path.
	 * @param path list of walkable tiles in the path. Not allowed to be null or empty.
	 */
	void move(LinkedList<IWalkableTile> path);
	
	/**
	 * Alerts the IMovable that it has collided with another IMovable.
	 * @param pawn the IMovable pawn that collided with this one.
	 */
	void collision(IMovable pawn);
}
