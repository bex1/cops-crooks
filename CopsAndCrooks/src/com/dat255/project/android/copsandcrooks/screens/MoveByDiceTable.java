package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;

public class MoveByDiceTable extends Table{

	public MoveByDiceTable(Assets assets, final GameModel model, final Stage hudStage) {
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
