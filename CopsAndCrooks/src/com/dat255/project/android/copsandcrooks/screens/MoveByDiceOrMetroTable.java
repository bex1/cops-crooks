package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;

public class MoveByDiceOrMetroTable extends Table {

	public MoveByDiceOrMetroTable(final Assets assets, final GameModel model) {
		this.setFillParent(true);
		
		// register the button "roll dice"
		final TextButton rollTheDiceButton = new TextButton("Roll the dice", assets.getSkin());
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
		final TextButton goByTramButton = new TextButton("Go by Metro", assets.getSkin());
		goByTramButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				goByTramButton.getParent().remove();
				model.getCurrentPlayer().goByMetro();
			}
		} );

		add(rollTheDiceButton).size(360, 60).expand().bottom().padBottom(10);
		row();

		add(goByTramButton).size(360, 60).uniform().padBottom(10);
		row();
	}
}
