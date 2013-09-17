package com.dat255.project.android.copsandcrooks.domainmodel;

/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Crook extends AbstractPawn {
	private int cash;
	
	/**
	 * Sets the cash for the crook.
	 * @param money the cash thr crook will hold.
	 */
	public void setCash(int money) {
		int oldValue = cash;
		cash = money;
		pcs.firePropertyChange("Cash", oldValue, cash);
	}
	
	/**
	 * Returns the cash on the crook.
	 * @return the cash on the crook.
	 */
	public int getCash() {
		return cash;
	}
	
	/**
	 * Increments the cash amount on the crook.
	 * @param money the money to increment with
	 */
	public void incrementCash(int money) {
		int oldValue = cash;
		cash += money;
		pcs.firePropertyChange("Cash", oldValue, cash);
	}
	
	/**
	 * Decrements the cash amount on the crook.
	 * 
	 * @param money the money to decrement with
	 * @return true if there was enough cash
	 */
	public boolean decrementCash(int money) {
		if (cash < money) {
			return false;
		}
		int oldValue = cash;
		cash -= money;
		pcs.firePropertyChange("Cash", oldValue, cash);
		return true;
		
	}
	
	/**
	 * Returns true if the crook is wanted.
	 * @return true if the crook is wanted.
	 */
	public boolean isWanted() {
		return cash > 0;
	}
}
