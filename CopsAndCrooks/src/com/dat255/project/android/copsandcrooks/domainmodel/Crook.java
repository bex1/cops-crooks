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
	private int turnsInPrison;
	
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
	/**
	 * Returns whether the crook is in prison or not.
	 * @param pawn - check to see if the pawn is a Crook, Cops can't be in prison.
	 * @return whether the crook is in prison or not.
	 */
	boolean isInPrison(){
		return currentTile instanceof PoliceStationTile;
	}
	/**
	 * Sets the number of turns in prison to the default value 3 if the crook is in prison
	 */
	void setTurnsInPrison(){
			turnsInPrison = 3;
	}
	/**
	 * The number of turns left in prison
	 * @return turnsInPrison - number of turns left in prison
	 */
	int getTurnsInPrison(){
		return turnsInPrison;
	}
	/**
	 * Decrements the number of turns left in prison.
	 */
	public void decrementTurnsInPrison(){
		if(turnsInPrison > 0)
		--turnsInPrison;
	}
}
