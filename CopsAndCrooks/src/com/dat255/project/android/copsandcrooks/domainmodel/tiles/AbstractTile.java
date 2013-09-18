package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import java.awt.Point;


/**
 * This class represents all tiles with a position 
 * and stores whether it's occupied or not.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public abstract class AbstractTile implements IWalkableTile{

	private boolean occupied;	
	private Point position;
	
	/**
	 * Construct a new AbstractTile with a position.
	 * @param position the tile's position
	 */
	public AbstractTile(Point position) {
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
	public Point getPosition(){
		return this.position;
	}
	
	@Override
	public void setPosition(Point position){
		this.position = position;
	}
}
