package com.dat255.project.android.copsandcrooks.view;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.IPlayer;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * The in prison table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class IsInPrisonTable extends Table {

	/**
	 * Ints the prison table. 
	 * 
	 * @param assets the assets of the game
	 * @param player the player who has a crook in prison.
	 * @param crook the crook who is in prison.
	 */
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
