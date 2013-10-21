package com.dat255.project.android.copsandcrooks.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

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
 * The arrested table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class ArrestedTable extends Table {

	public ArrestedTable(final Assets assets, final IPlayer player, final Crook crook){
		this.setFillParent(true);
		
		Label isArrestedLabel;
		
		int arrested = crook.getTimesArrested();
		int arrestsRemain = Values.MAX_TIMES_ARRESTED - arrested;
		if (arrestsRemain > 0) {
			isArrestedLabel = new Label(player.getName() + " got arrested for the " + crook.getTimesArrested() + 
																" time\n" + arrestsRemain + " arrests remaining", assets.getSkin());
		} else {
			isArrestedLabel = new Label(player.getName() + " was sentenced to lifetime", assets.getSkin());
		}
		isArrestedLabel.setFontScale(0.7f);
		isArrestedLabel.setAlignment(Align.center);
		isArrestedLabel.setColor(Color.BLACK);
		add(isArrestedLabel);
	}

	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);
		
		this.addAction(sequence(delay(2.5f), fadeOut(0.5f), Actions.removeActor()));
	}

}
