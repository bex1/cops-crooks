package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Interface describing a movable object.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public interface IMovable extends IObservable {
	
	/**
	 * Updates the state of the movable.
	 * @param deltaTime the time between current frame and last.
	 */
	void update(float deltaTime);
	
	/**
	 * Returns the tile on which the movable is currently standing on.
	 * @return the walkable tile on which the movable is currently standing on.
	 */
	IWalkableTile getCurrentTile();
	
	/**
	 * Returns the next tile where the movable is heading.
	 * @return the next tile where the movable is heading.
	 */
	IWalkableTile getNextTile();
	
	/**
	 * Returns the direction of the pawn.
	 * @return the direction of the pawn.
	 */
	Direction getDirection();
	
	/**
	 * Crook 0 =< id < 10
	 * Cop 10 =< id <20
	 * CopCar = 20 (There will always only be 1 Copcar)
	 * @return
	 */
	public int getID();
	
	/**
	 * Returns true if the pawn is moving, false otherwise.
	 * @return true if the pawn is moving, false otherwise.
	 */
	boolean isMoving();
	
	boolean isPlaying();
	
	public static final String PROPERTY_NEXT_TILE = "NextTile";
	public static final String PROPERTY_IS_MOVING = "IsMoving";
	public static final String PROPERTY_CURRENT_TILE = "CurrentTile";
	public static final String PROPERTY_IS_IN_POLICE_HOUSE = "IsInPoliceHouse";
	public static final String PROPERTY_IS_PLAYING = "IsPlaying";
	
	//TODO: MAKE OWN CLASS, PACKAGE PRIVATE
	public enum PawnType {
		Crook,
		Officer,
		Car,
	}

	
}
