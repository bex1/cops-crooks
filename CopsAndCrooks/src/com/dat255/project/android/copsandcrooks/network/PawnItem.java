package com.dat255.project.android.copsandcrooks.network;

import java.io.Serializable;

import com.dat255.project.android.copsandcrooks.utils.Point;

/**	
 * Class used to describe a pawn
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class PawnItem implements Serializable{
	
	private static final long serialVersionUID = 4882004331125447L;
	
	/**
	 * The position of the PawnItem
	 */
	public final Point position;
	
	/**
	 * The id of the PawnItem
	 */
	public final int id;

	/**
	 * Create a new PawnItem with the gives position and id
	 * @param position
	 * @param id
	 */
	public PawnItem(Point p, int id) {
		this.position = p;
		this.id = id;
	}
	
	/**
	 * Create a new PawnItem
	 */
	public PawnItem(){
		position = null;
		id = -1;
	}

}
