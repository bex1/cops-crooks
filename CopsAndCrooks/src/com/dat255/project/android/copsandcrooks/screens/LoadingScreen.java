package com.dat255.project.android.copsandcrooks.screens;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.map.GameFactory;

public class LoadingScreen extends AbstractScreen {

	public LoadingScreen(CopsAndCrooks game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		game.setScreen(GameFactory.loadGame(0, game));

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
