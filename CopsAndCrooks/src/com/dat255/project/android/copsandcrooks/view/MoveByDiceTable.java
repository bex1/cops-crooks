package com.dat255.project.android.copsandcrooks.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.model.GameModel;

/**
 * The move by dice table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class MoveByDiceTable extends Table{

	/**
	 * Ints the move by dice table.
	 * 
	 * @param assets the assets of the game
	 * @param model the gamemodel.
	 * @param hudStage the stage to draw the table on.
	 */
	public MoveByDiceTable(final Assets assets, final GameModel model, final Stage hudStage) {
		this.setFillParent(true);
		
		// register the button "roll dice"
		final TextButton rollTheDiceButton = new TextButton("Roll the dice", assets.getSkin());
		rollTheDiceButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				
				rollTheDiceButton.getParent().remove();
				for(Actor actor: hudStage.getActors()){
					if(actor instanceof IsInPrisonTable)
						actor.remove();
				}
				model.getCurrentPlayer().rollDice();
			}
		});

		add(rollTheDiceButton).size(360, 60).expand().bottom().padBottom(30);
		row();
	}
}
