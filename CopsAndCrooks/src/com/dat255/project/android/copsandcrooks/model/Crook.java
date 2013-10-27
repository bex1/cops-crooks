package com.dat255.project.android.copsandcrooks.model;

import com.dat255.project.android.copsandcrooks.model.GameModel.GameState;
import com.dat255.project.android.copsandcrooks.utils.Values;


/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Crook extends AbstractWalkingPawn {
	
	private Wallet wallet;
	private boolean attemptingGetAway, isWanted;
	private int turnsInPrison, timesArrested;
	
	public static final String PROPERTY_IS_WANTED = "IsWanted";
	public static final String PROPERTY_TIMES_ARRESTED = "TimesArrested";
	public static final String PROPERTY_TURNS_IN_PRISON = "TurnsInPrison";
	
	Crook(AbstractWalkableTile startTile, IMediator mediator, int id) {
		super(startTile, Role.Crook, PawnType.Crook, mediator, Values.WALKING_PAWN_MOVE_FACTOR, id);
		this.wallet = new Wallet();
	}

	/**
	 * Returns true if the crook is wanted.
	 * @return true if the crook is wanted.
	 */
	public boolean isWanted() {
		return isWanted;
	}
	
	@Override
	protected void interactWithTile() {
		if (mediator.checkState() == GameState.Replay) {
			if (currentTile instanceof HideoutTile) {
				HideoutTile hideout = (HideoutTile)currentTile;
				switch (mediator.getCurrentTurn().getHideoutChoice()) {
				case Deposit:
					hideout.depositCash(this);
					break;
				case Withdraw:
					hideout.withdrawCash(this);
					break;
				case Cancel:
					hideout.cancelInteraction();
					break;
				}
				return;
			}
		} 
		super.interactWithTile();
	}

	/**
	 * Sets if the crook is wanted or not.
	 * @param wanted true if the crook is wanted, false otherwise.
	 */
	void setWanted(boolean wanted) {
		this.isWanted = wanted;
		pcs.firePropertyChange(PROPERTY_IS_WANTED, null, isWanted);
	}
	
	/**
	 * Returns the wallet of the crook.
	 * @return the wallet of the crook.
	 */
	public Wallet getWallet() {
		return wallet;
	}

	@Override
	public void collisionAfterMove(IPawn pawn) {
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
	 * @return whether the crook is in prison or not.
	 */
	boolean isInPrison(){
		return currentTile instanceof PoliceStationTile;
	}
	/**
	 * Sets the number of turns in prison to the default value 3 if the crook is in prison
	 */
	void setTurnsInPrison(int turns){
		turnsInPrison = turns;
	}
	/**
	 * The number of turns left in prison
	 * @return turnsInPrison - number of turns left in prison
	 */
	public int getTurnsInPrison(){
		return turnsInPrison;
	}
	/**
	 * Decrements the number of turns left in prison.
	 */
	void decrementTurnsInPrison(){
		if(turnsInPrison > 0) {
			--turnsInPrison;
			if (mediator.isItMyPlayerTurn(this) && mediator.checkState() == GameState.Playing) {
				pcs.firePropertyChange(PROPERTY_TURNS_IN_PRISON, -1, turnsInPrison);
			}
		}
	}
	/**
	 * Increments the number of times arrested
	 */
	void incrementTimesArrested(){
		++timesArrested;
		pcs.firePropertyChange(PROPERTY_TIMES_ARRESTED, -1, timesArrested);
	}
	/**
	 * Returns the number of times arrested
	 * @return the number of times arrested
	 */
	public int getTimesArrested(){
		return timesArrested;
	}
}
