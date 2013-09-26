package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.utils.Values;

public class Officer extends AbstractWalkingPawn {
	
	public Officer(IMediator mediator) {
		super(Role.Police, PawnType.Officer, mediator, 1);
		// All officers start in the police house
		this.setIsInPoliceStation(true);
	}

	@Override
	public void collisionAfterMove(IMovable pawn) {
		if (pawn instanceof Crook) {
			Crook crook = (Crook)pawn;
			if (crook.isWanted()) {
				// We collided with a wanted crook -> Arrest
				// Officer takes the crook to the police station
				mediator.moveToPoliceStation(crook);
				mediator.moveToPoliceStation(this);
				crook.setIsInPoliceStation(true);
				this.setIsInPoliceStation(true);
				
				//Take bounty
				mediator.addCashToMyPlayer((int)(crook.getWallet().getCash() * 
						Values.POLICE_CASH_REWARD_FACTOR), this);
				crook.getWallet().setCash(0);
			}
		}
	}

	public void gotSelected(Officer pawn) {
		mediator.changePawn(pawn);
	}
}
