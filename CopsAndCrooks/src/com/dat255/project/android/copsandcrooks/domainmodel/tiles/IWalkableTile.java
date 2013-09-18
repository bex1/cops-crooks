package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import javax.vecmath.Vector2d;

/**
 * This class represents a walkable tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IWalkableTile {
	
	/**
	 * Checks if the tile is occupied by an IMovable object
	 * @return true if this tile is occupied
	 */
	public boolean isOccupied();
	
	/**
	 * Set the occupied-status of this tile
	 */
	public void setOccupied(boolean occupied);
	
	/**
	 * Get the position of this tile
	 * @return the position of this tile
	 */
	public Vector2d getPosition();
	
	/**
	 * Set the position of this tile
	 */
	public void setPosition(Vector2d position);
}
