package com.dat255.project.android.copsandcrooks.screens;

import java.util.HashMap;
import java.util.Map;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class LoadingScreen extends AbstractScreen {

	public LoadingScreen(CopsAndCrooks game) {
		super(game, Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
	}

	@Override
	public void show() {
		super.show();
		Map<String, Role> test = new HashMap<String, Role>();
		test.put("Player1", Role.Crook);
		test.put("Player2", Role.Crook);
		game.setScreen(GameFactory.loadGame(game, test));
	}
}
