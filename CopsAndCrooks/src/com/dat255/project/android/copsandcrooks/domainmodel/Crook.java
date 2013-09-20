package com.dat255.project.android.copsandcrooks.domainmodel;

import com.dat255.project.android.copsandcrooks.domainmodel.tiles.Hideout;

/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Crook extends AbstractWalkingPawn {
	
	private Wallet wallet;
	private boolean attemptingGetAway;
	
	public Crook(IMediator mediator) {
		super(Role.Crook, PawnType.Crook, mediator);
		wallet = new Wallet();
		attemptingGetAway = false;
	}
	
	/**
	 * Returns true if the crook is wanted.
	 * @return true if the crook is wanted.
	 */
	public boolean isWanted() {
		return wallet.getCash() > 0;
	}
	
	/**
	 * Returns the wallet of the crook.
	 * @return the wallet of the crook.
	 */
	public Wallet getWallet() {
		return wallet;
	}

	@Override
	public void collisionAfterMove(IMovable pawn) {
		if (!(currentTile instanceof Hideout && pawn instanceof Crook)) {
			// Should not happen, crooks can only move to an occupied tile when its an Hideout.
			assert false;
		}
	}

	/**
	 * Return true if the crook is attempting to escape.
	 * @return true if the crook is attempting to escape
	 */
	public boolean isAttemptingGetAway() {
		return attemptingGetAway;
	}

	/**
	 * Set attempting get away status for this crook.
	 * @param attemptingGetAway the new get away status
	 */
	public void setAttemptingGetAway(boolean attemptingGetAway) {
		this.attemptingGetAway = attemptingGetAway;
	}
}
