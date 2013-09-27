package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * This class represents the road as a tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class RoadTile extends AbstractTile {

	/**
	 * Create a new RoadTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	public RoadTile(Point position, IMediator mediator) {
		super(position, mediator);

		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}

}
