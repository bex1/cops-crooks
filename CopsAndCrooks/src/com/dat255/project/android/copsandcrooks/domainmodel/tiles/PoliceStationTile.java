package com.dat255.project.android.copsandcrooks.domainmodel.tiles;


import com.dat255.project.android.copsandcrooks.utils.Point;

import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;

/**
 * A class representing a police station tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class PoliceStationTile extends AbstractTile implements IInteractiveTile {

	/**
	 * Create a new PoliceStationTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	public PoliceStationTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IMovable target) {
		
	}

}
