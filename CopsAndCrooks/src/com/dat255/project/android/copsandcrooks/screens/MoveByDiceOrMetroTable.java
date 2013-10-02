package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.utils.Utilities;

public class MoveByDiceOrMetroTable extends Table {

	public MoveByDiceOrMetroTable(final GameModel model) {
		this.setFillParent(true);

		Label text = new Label("It's your turn\nplease roll the dice", Utilities.getSkin());
		text.setAlignment(Align.center);
		add(text).spaceBottom(50);
		row();
		
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

		// register the button "go by tram"
		final TextButton goByTramButton = new TextButton("Go by tram", Utilities.getSkin());
		goByTramButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				goByTramButton.getParent().remove();
				model.getCurrentPlayer().goByMetro();
			}
		} );

		add(rollTheDiceButton).size(360, 60).uniform().padBottom(10);
		row();

		add(goByTramButton).size(360, 60).uniform().padBottom(10);
		row();
	}
}
