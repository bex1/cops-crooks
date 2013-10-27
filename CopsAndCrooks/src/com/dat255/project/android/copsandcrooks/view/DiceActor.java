package com.dat255.project.android.copsandcrooks.view;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.repeat;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.model.Dice;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A visual actor for the dice in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class DiceActor extends Image implements PropertyChangeListener{
	private final Animation animation;
	private final Dice dice;
	private float animTimer;
	private TextureRegionDrawable drawable;
	private Label label;
	private final Assets assets;
	private final Stage hudStage;
	
	/**
	 * 
	 * @param assets The assets for cops and crooks
	 * @param dice The first dice of this actor 
	 * @param animation The animation for the actor.
	 * @param firstDrawable The first drawable of this actor
	 * @param scaling The scaling used for the drawable
	 * @param hudStage the stage on where this actor will be drawn
	 */
	public DiceActor(final Assets assets, final Dice dice, final Animation animation, final TextureRegionDrawable firstDrawable, final Scaling scaling, final Stage hudStage) {
		super(firstDrawable, scaling);
		this.hudStage = hudStage;
		this.dice = dice;
		this.animation = animation;
		this.assets = assets;
		dice.addObserver(this);
		drawable = firstDrawable;
		TextureRegion txtReg = drawable.getRegion();
		setPosition((Values.GAME_VIEWPORT_WIDTH - txtReg.getRegionWidth())/2, (Values.GAME_VIEWPORT_HEIGHT - txtReg.getRegionHeight())/2);
		initResultLabel();
		
	}
	
	private void initResultLabel() {
		label = new Label("0", assets.getSkin());
		label.setFontScale(3f);
		label.setPosition((Values.GAME_VIEWPORT_WIDTH - label.getWidth())/2, (Values.GAME_VIEWPORT_HEIGHT - label.getHeight())/2);
		label.setColor(Color.BLACK);
	}

	@Override
	public void act(float delta){
		super.act(delta);
		dice.update(delta);
		animTimer += delta;
		// switch animation on preset interval
		drawable.setRegion(animation.getKeyFrame(animTimer, true));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == dice) {
			String property = evt.getPropertyName();
			if (property == Dice.PROPERTY_DICE_ROLLING) {
				// add to stage, ie show
				hudStage.addActor(this);
			} else if (property == Dice.PROPERTY_DICE_RESULT) {
				// remove dice from stage
				this.addAction(removeActor());
				animTimer = 0;
				// show score label with the dice result, blinking animation
				label.setText("" + dice.getResult());
				label.getColor().a = 0;
				hudStage.addActor(label);
				label.addAction(sequence(repeat(3, sequence(fadeIn(0.3f), delay(0.8f), fadeOut(0.3f))), removeActor()));
			}
		}
	}
}