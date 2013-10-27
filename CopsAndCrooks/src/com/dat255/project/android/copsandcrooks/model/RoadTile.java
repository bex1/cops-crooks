package com.dat255.project.android.copsandcrooks.model;

import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * This class represents the road as a tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class RoadTile extends AbstractWalkableTile {
	
	private static final long serialVersionUID = 8001224843327L;

	/**
	 * Create a new RoadTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	RoadTile(Point position, IMediator mediator) {
		super(position, mediator);

		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}
}
