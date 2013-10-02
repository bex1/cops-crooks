package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;

/**
 * A cop car in the game Cops&Crooks
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CopCar extends AbstractPawn {

	public CopCar(IMediator mediator, int id) {
		super(Role.Cop, PawnType.Car, mediator, 2, id);
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
	public int tilesMovedEachStep() {
		return 2;
	}

	public void gotSelected() {
		mediator.changePawn(this);
	}
}
