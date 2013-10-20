package com.dat255.project.android.copsandcrooks.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A class representing text displayed when is in prison.
 *
 */
public class IsInPrisonTable extends Table {

	public IsInPrisonTable(final Assets assets, final IPlayer player, final Crook crook){
		this.setFillParent(true);
		
		final Label isInPrisonLabel = new Label(player.getName() + " is in Prison for " + crook.getTurnsInPrison() + 
																" turns\nroll a " +
																Values.DICE_RESULT_TO_ESCAPE + 
																" to escape", assets.getSkin());
		isInPrisonLabel.setFontScale(0.7f);
		isInPrisonLabel.setAlignment(Align.center);
		isInPrisonLabel.setColor(Color.BLACK);
		add(isInPrisonLabel);
	}

	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);
		
		this.addAction(sequence(delay(2.5f), fadeOut(0.5f), Actions.removeActor()));
	}
}
