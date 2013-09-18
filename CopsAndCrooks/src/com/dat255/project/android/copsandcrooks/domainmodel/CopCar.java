package com.dat255.project.android.copsandcrooks.domainmodel;

/**
 * A cop car in the game Cops&Crooks
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CopCar extends AbstractPawn {

	public CopCar(IMediator mediator) {
		super(mediator);
	}

	@Override
	public void collisionAfterMove(IMovable pawn) {
		if (pawn instanceof Crook) {
			Crook crook = (Crook)pawn;
			if (crook.isWanted()) {
				// We collided with a wanted crook -> Arrest
				// Car stays and crook gets moved to the police station
				mediator.moveToPoliceStation(crook);
				crook.setIsInPoliceStation(true);
			}
		}
	}
}
