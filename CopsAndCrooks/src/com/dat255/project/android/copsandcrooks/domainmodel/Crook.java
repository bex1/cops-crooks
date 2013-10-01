package com.dat255.project.android.copsandcrooks.domainmodel;


/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Crook extends AbstractWalkingPawn {
	
	private Wallet wallet;
	private boolean attemptingGetAway, isWanted;
	
	public Crook(AbstractWalkableTile startTile, IMediator mediator) {
		super(startTile, Role.Crook, PawnType.Crook, mediator, 1);
		wallet = new Wallet();
	}
	
	/**
	 * Returns true if the crook is wanted.
	 * @return true if the crook is wanted.
	 */
	public boolean isWanted() {
		return isWanted;
	}
	
	/**
	 * Sets if the crook is wanted or not.
	 * @param wanted true if the crook is wanted, false otherwise.
	 */
	void setWanted(boolean wanted) {
		this.isWanted = wanted;
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
		if (!(currentTile instanceof HideoutTile && pawn instanceof Crook)) {
			// Should not happen, crooks can only move to an occupied tile when its an Hideout.
			assert false;
		}
	}

	/**
	 * Return true if the crook is attempting to escape.
	 * @return true if the crook is attempting to escape
	 */
	boolean isAttemptingGetAway() {
		return attemptingGetAway;
	}

	/**
	 * Set attempting get away status for this crook.
	 * @param attemptingGetAway the new get away status
	 */
	void setAttemptingGetAway(boolean attemptingGetAway) {
		this.attemptingGetAway = attemptingGetAway;
	}
}
