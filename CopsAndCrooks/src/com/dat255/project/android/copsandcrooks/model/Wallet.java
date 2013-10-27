package com.dat255.project.android.copsandcrooks.model;

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
	
	private static final long serialVersionUID = 8552221330055127L;
	
	private int cash;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	public static final String PROPERTY_CASH = "Cash";
	
	/**
	 * Sets the cash for the crook.
	 * @param money the cash the crook will hold.
	 */
	void setCash(int money) {
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
	void incrementCash(int money) {
		int oldValue = cash;
		cash += money;
		pcs.firePropertyChange(PROPERTY_CASH, oldValue, cash);
	}
	
	/**
	 * Decrements the amount of cash in the wallet by a value,
	 * if the current amount of cash is enough.
	 * @param money the money to decrement with
	 */
	void decrementCash(int money) {
		if (cash < money)
			return;

		int oldValue = cash;
		cash -= money;
		pcs.firePropertyChange(PROPERTY_CASH, oldValue, cash);

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
