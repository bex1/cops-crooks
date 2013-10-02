package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.utils.Utilities;

public class MoveByDiceTable extends Table{

	public MoveByDiceTable(final GameModel model) {
		this.setFillParent(true);
		
		// register the button "roll dice"
		final TextButton rollTheDiceButton = new TextButton("Roll the dice", Utilities.getSkin());
		rollTheDiceButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				rollTheDiceButton.getParent().remove();
				model.getCurrentPlayer().rollDice();
			}
		});

		add(rollTheDiceButton).size(360, 60).expand().bottom().padBottom(30);
		row();
	}
}
