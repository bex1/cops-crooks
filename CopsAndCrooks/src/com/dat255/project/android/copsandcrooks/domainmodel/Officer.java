package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * Officer pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Officer extends AbstractWalkingPawn implements ISelectablePawn {
	
	public Officer(AbstractWalkableTile startTile, IMediator mediator, int id) {
		super(startTile, Role.Cop, PawnType.Officer, mediator, 1, id);
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
				
				Wallet crookWallet = crook.getWallet();
				//Take bounty
				mediator.addCashToMyPlayer((int)(crookWallet.getCash() * 
						Values.POLICE_CASH_REWARD_FACTOR), this);
				crookWallet.setCash(0);
				crook.setWanted(false);
			}
		}
	}

	@Override
	public void gotSelected() {
		mediator.changePawn(this);
	}
}
