package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

/**
 * Represents a wallet that can hold money.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public final class Wallet implements IObservable {
	
	private int cash;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROPERTY_CASH = "Cash";
	
	/**
	 * Sets the cash for the crook.
	 * @param money the cash the crook will hold.
	 */
	public void setCash(int money) {
		int oldValue = cash;
		cash = money;
		pcs.firePropertyChange(PROPERTY_CASH, oldValue, cash);
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
		pcs.firePropertyChange(PROPERTY_CASH, oldValue, cash);
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
		pcs.firePropertyChange(PROPERTY_CASH, oldValue, cash);
		return true;
		
	}

	@Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}
}
