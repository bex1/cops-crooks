package com.dat255.project.android.copsandcrooks.model;

import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * Officer pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class Officer extends AbstractWalkingPawn implements ISelectablePawn {
	
	Officer(AbstractWalkableTile startTile, IMediator mediator, int id) {
		super(startTile, Role.Cop, PawnType.Officer, mediator, Values.WALKING_PAWN_MOVE_FACTOR, id);
		// All officers start in the police house
		this.setIsInPoliceStation(true);
	}

	@Override
	public void collisionAfterMove(IPawn pawn) {
		if (pawn instanceof Crook) {
			Crook crook = (Crook)pawn;
			if (crook.isWanted()) {
				// We collided with a wanted crook -> Arrest
				// Officer takes the crook to the police station
				if(crook.getTimesArrested() != Values.MAX_TIMES_ARRESTED)
					crook.incrementTimesArrested();
					
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
		if (mediator.isItMyPlayerTurn(this)) {
			this.setIsActivePawn(true);
		}
	}
}
