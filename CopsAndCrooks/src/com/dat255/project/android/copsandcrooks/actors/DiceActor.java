package com.dat255.project.android.copsandcrooks.actors;

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
import com.dat255.project.android.copsandcrooks.domainmodel.Dice;
import com.dat255.project.android.copsandcrooks.screens.Assets;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class DiceActor extends Image implements PropertyChangeListener{
	private final Animation animation;
	private final Dice dice;
	private float animTimer;
	private TextureRegionDrawable drawable;
	private Label label;
	private Assets assets;
	
	
	public DiceActor(Assets assets, final Dice dice, final Animation animation, final TextureRegionDrawable firstDrawable, final Scaling scaling) {
		super(firstDrawable, scaling);
		this.dice = dice;
		this.animation = animation;
		this.assets = assets;
		this.setVisible(false);
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
		if (this.isVisible()) {
			dice.update(delta);
			animTimer += delta;
			drawable.setRegion(animation.getKeyFrame(animTimer, true));
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == dice) {
			String property = evt.getPropertyName();
			if (property == Dice.PROPERTY_DICE_ROLLING) {
				this.setVisible(true);
			} else if (property == Dice.PROPERTY_DICE_RESULT) {
				this.setVisible(false);
				animTimer = 0;
				Stage stage = this.getStage();
				label.setText("" + dice.getResult());
				label.getColor().a = 0;
				stage.addActor(label);
				label.addAction(sequence(repeat(3, sequence(fadeIn(0.3f), delay(0.8f), fadeOut(0.3f))), removeActor()));
			}
		}
	}
}
