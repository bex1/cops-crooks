package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import javax.vecmath.Vector2d;

/**
 * This class represents all tiles with a position 
 * and stores whether it's occupied or not.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractTile implements IWalkableTile{

	private boolean occupied;	
	private Vector2d position;
	
	/**
	 * Construct a new AbstractTile with a position.
	 * @param position the tile's position
	 */
	public AbstractTile(Vector2d position) {
		this.position = position;
	}

	@Override
	public boolean isOccupied(){
		return this.occupied;
	}

	@Override
	public void setOccupied(boolean occupied) {
		this.occupied = occupied;
	}
	
	@Override
	public Vector2d getPosition(){
		return this.position;
	}
	
	@Override
	public void setPosition(Vector2d position){
		this.position = position;
	}
}
