package com.dat255.project.android.copsandcrooks.domainmodel;


import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;


/**
 * A class representing a police station tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class PoliceStationTile extends AbstractWalkableTile implements IInteractiveTile {

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
