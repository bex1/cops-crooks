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
	void update(float deltaTime);
	
	/**
	 * Returns the tile on which the movable is currently standing on.
	 * @return the walkable tile on which the movable is currently standing on.
	 */
	IWalkableTile getCurrentTile();
	
	/**
	 * Sets the current tile on which the movable is standing on.
	 *
	 * @param currTile the current tile on which the movable is standing on.
	 * Allowed to be null to move the pawn out of the game.
	 */
	//TODO: RELOCATE TO ABSTRACT
	void setCurrentTile(IWalkableTile currTile);
	
	/**
	 * Returns the next tile where the movable is heading.
	 * @return the next tile where the movable is heading.
	 */
	IWalkableTile getNextTile();
	
	/**
	 * Moves the movable object along the given path.
	 * @param path the path of walkable tiles in the path. Not allowed to be null or empty.
	 */
	//TODO: RELOCATE TO ABSTRACT
	void move(TilePath path);
	
	/**
	 * Alerts the IMovable that it has collided with another IMovable after it has moved.
	 * @param pawn the IMovable pawn that collided with this one.
	 */
	//TODO: RELOCATE TO ABSTRACT
	void collisionAfterMove(IMovable pawn);
	
	/**
	 * Returns the role of this pawn.
	 * @return the role of this pawn.
	 */
	//TODO: RELOCATE TO ABSTRACT
	Role getPawnRole();
	
	/**
	 * Returns the type of this pawn.
	 * @return the type of this pawn.
	 */
	//TODO: RELOCATE TO ABSTRACT
	PawnType getPawnType();
	
	/**
	 * Returns the number of tiles the pawn can move over in one step.
	 * @return the number of tiles the pawn can move over in one step.
	 */
	//TODO: RELOCATE TO ABSTRACT
	int tilesMovedEachStep();
	
	/**
	 * Returns the direction of the pawn.
	 * @return the direction of the pawn.
	 */
	Direction getDirection();
	
	/**
	 * Returns true if the pawn is moving, false otherwise.
	 * @return true if the pawn is moving, false otherwise.
	 */
	boolean isMoving();
	
	public static final String PROPERTY_NEXT_TILE = "NextTile";
	public static final String PROPERTY_IS_MOVING = "IsMoving";
	public static final String PROPERTY_CURRENT_TILE = "CurrentTile";
	public static final String PROPERTY_IS_IN_POLICE_HOUSE = "IsInPoliceHouse";
	
	//TODO: MAKE OWN CLASS, PACKAGE PRIVATE
	public enum PawnType {
		Crook,
		Officer,
		Car,
	}
}
