package com.dat255.project.android.copsandcrooks.model;


import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * This class represents a tramstop.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class MetroStopTile extends AbstractWalkableTile{
	
	private static final long serialVersionUID = 12289645622564327L;
	
	/**
	 * Create a MetroStopTile with a position.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	MetroStopTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}
}
