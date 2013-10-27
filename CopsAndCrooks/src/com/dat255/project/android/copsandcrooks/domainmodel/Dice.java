package com.dat255.project.android.copsandcrooks.domainmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Values;
/**
 * A crook pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public final class Dice implements IObservable{
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	private final Random rand = new Random();
	private int diceResult = -1;
	private boolean isRolling;
	private float isRollingTimer;
	private Player player;
	
	public static final String PROPERTY_DICE_ROLLING = "DiceRolling";
	public static final String PROPERTY_DICE_RESULT = "DiceResult";

	Dice(IMediator mediator) {
		mediator.registerDice(this);
	}

	/**
	 * Checks to see wether the die is rolling and with that helps the animation.
	 * @param deltaTime - time to animate the die.
	 */
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
	/**
	 * Returns the result of the rolled die.
	 * @return - the result of the rolled die.
	 */
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
