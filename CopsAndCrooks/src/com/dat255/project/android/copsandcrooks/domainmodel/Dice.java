package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import com.dat255.project.android.copsandcrooks.utils.IObservable;

public final class Dice implements IObservable {
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private final Random rand = new Random();

	public Dice(IMediator mediator) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		
		mediator.registerDice(this);
	}
	
	public int roll() {
		return 1 + rand.nextInt(6);
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
