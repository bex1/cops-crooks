package com.dat255.project.android.copsandcrooks.screens;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.Constants;

public class LoadingScreen extends AbstractScreen {

	public LoadingScreen(CopsAndCrooks game) {
		super(game, Constants.GAME_VIEWPORT_WIDTH, Constants.GAME_VIEWPORT_HEIGHT);
	}

	@Override
	public void show() {
		super.show();
		game.setScreen(GameFactory.loadGame(0, game));
	}
}
