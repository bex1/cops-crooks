package com.dat255.project.android.copsandcrooks.domainmodel;


/**
 * A cop car in the game Cops&Crooks
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CopCar extends AbstractPawn implements ISelectablePawn {

	public CopCar(AbstractWalkableTile startTile, IMediator mediator) {
		super(startTile, Role.Police, PawnType.Car, mediator, 2);
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

	@Override
	public void gotSelected() {
		mediator.changePawn(this);
	}
}
