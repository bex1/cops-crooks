package com.dat255.project.android.copsandcrooks.model;


import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;


/**
 * A class representing a police station tile.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class PoliceStationTile extends AbstractWalkableTile implements IInteractiveTile {
	
	private static final long serialVersionUID = 73221455775217L;

	/**
	 * Create a new PoliceStationTile.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	PoliceStationTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IPawn target) {
		if(target instanceof Crook){
			Crook crook = (Crook)target;
			crook.setTurnsInPrison(Values.TURNS_IN_PRISON);
		}
	}
}
