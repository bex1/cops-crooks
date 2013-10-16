package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Random;

import com.badlogic.gdx.utils.Timer.Task;
import com.dat255.project.android.copsandcrooks.utils.IObservable;

public final class Dice implements IObservable, Serializable {
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private final Random rand = new Random();
	private int diceResult = -1;
	private boolean isRolling;
	private float isRollingTimer;
	private static final float ROLL_DELAY = 1.4f;
	private Player player;
	
	private static Dice instance= null;
	
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
			if (isRollingTimer >= ROLL_DELAY) {
				isRolling = false;
				isRollingTimer = 0;
				diceResult = 1 + rand.nextInt(6);
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
		diceResult = (i%6) + 1;
		// Added to prevent you can close the app to get one more try to roll dice
		if(i == -1){			
			diceResult = -1;		
		}
	}
}
