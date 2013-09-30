package com.dat255.project.android.copsandcrooks.domainmodel;


import com.dat255.project.android.copsandcrooks.domainmodel.IMovable.PawnType;
import com.dat255.project.android.copsandcrooks.utils.Point;

/**
 * This class represents a tramstop.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class TramStopTile extends AbstractWalkableTile implements IInteractiveTile{
	
	/**
	 * Create a TramStopTile with a position.
	 * @param position the position of this tile
	 * @param mediator the mediator
	 */
	public TramStopTile(Point position, IMediator mediator) {
		super(position, mediator);
		
		pawnTypes.add(PawnType.Crook);
		pawnTypes.add(PawnType.Car);
		pawnTypes.add(PawnType.Officer);
	}

	@Override
	public void interact(IMovable target) {
		if(target instanceof AbstractWalkingPawn){
			AbstractWalkingPawn pawn = (AbstractWalkingPawn) target;
			pawn.standingOnTramstop(true);
		}
	}

}
