package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Point;

import java.util.Collection;
import java.util.List;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;


/**
 * This class represents a walkable tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public interface IWalkableTile extends IObservable{
	
	/**
	 * Checks if the tile is occupied by an IMovable object
	 * @return true if this tile is occupied
	 */
	public boolean isOccupied();
	
	/**
	 * Set the occupied-status of this tile
	 */
	public void setOccupiedBy(PawnType pawnType);

	/**
	 * Set the occupied-status of this tile to none
	 */
	public void setNotOccupied();
	
	/**
	 * Get the occupied-status of this tile
	 */
	public PawnType getOccupiedBy();
	
	/**
	 * Get the position of this tile
	 * @return the position of this tile
	 */
	public Point getPosition();
	
	/**
	 * Set the position of this tile
	 */
	public void setPosition(Point position);
	

	/**
	 * Get the allowed pawntypes for this tile
	 * @return the allowed pawntypes
	 */
	public Collection<PawnType> getAllowedPawnTypes();

}
