package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel.GameState;

public class ReplayTable extends Table{
	

	public ReplayTable(Assets assets, final GameModel model) {
		this.setFillParent(true);
		
		// register the button "roll dice"
		final TextButton replayButton = new TextButton("Replay", assets.getSkin());
		replayButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				replayButton.getParent().remove();
				model.replay();
			}
		});

		add(replayButton).size(360, 60).expand().bottom().padBottom(30);
		row();
	}
}
