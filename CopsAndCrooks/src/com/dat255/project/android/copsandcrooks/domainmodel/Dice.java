package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Random;

import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Values;

public final class Dice implements IObservable, Serializable {
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private final Random rand = new Random();
	private int diceResult = -1;
	private boolean isRolling;
	private float isRollingTimer;
	private Player player;
	
	private static Dice instance;
	
	public static final String PROPERTY_DICE_ROLLING = "DiceRolling";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";

	// Only added to make class singleton
	private Dice() {
	}
	
	public static Dice getInstance(){
		if(instance == null){
			instance = new Dice();
		}
		return instance;
	}
	
	public void update(float deltaTime) {
		if (isRolling) {
			isRollingTimer += deltaTime;
			if (isRollingTimer >= Values.ROLL_DELAY) {
				isRolling = false;
				isRollingTimer = 0;
				diceResult = 1 + rand.nextInt(Values.MAX_DICE_ROLL);
				pcs.firePropertyChange(PROPERTY_DICE_RESULT, -1, diceResult);
				player.diceResult(diceResult);
			}
		}
	}
	
	void roll(Player player) {
		this.player = player;
		pcs.firePropertyChange(PROPERTY_DICE_ROLLING, false, true);
		isRolling = true;
		isRollingTimer = 0;
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
	
	void setResult(int i){
		diceResult = (i%Values.MAX_DICE_ROLL) + 1;
		// Added to prevent you can close the app to get one more try to roll dice
		if(i == -1){			
			diceResult = -1;		
		}
	}
}
