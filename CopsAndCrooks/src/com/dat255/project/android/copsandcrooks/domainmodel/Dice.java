package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

public final class Dice implements IObservable {
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private final Random rand = new Random();
	private final IMediator mediator;
	private int diceResult;
	
	public static final String PROPERTY_DICE_ROLLING = "DiceRolling";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";
	
	private class RollTask extends Task {
		private Player player;
		
		RollTask(Player player) {
			this.player = player;
		}

		@Override
		public void run () {
			diceResult = 1 + rand.nextInt(6);
			pcs.firePropertyChange(PROPERTY_DICE_RESULT, -1, diceResult);
			player.diceResult(diceResult);
			this.cancel();
		}
	}

	public Dice(IMediator mediator) {
		if (mediator == null)
			throw new IllegalArgumentException("Mediator not allowed to be null");
		this.mediator = mediator;
		mediator.registerDice(this);
	}
	
	void roll(Player player) {
		pcs.firePropertyChange(PROPERTY_DICE_ROLLING, false, true);
		mediator.schedule(new RollTask(player), 1.4f);
	}

	@Override
	public void addObserver(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	@Override
	public void removeObserver(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	public int getResult() {
		return diceResult;
	}
}
