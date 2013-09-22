package com.dat255.project.android.copsandcrooks.domainmodel.tiles;

import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;


/**
 * This class represents the road as a tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class RoadTile extends AbstractTile {

	public RoadTile(Point position) {
		super(position);

		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}

}
