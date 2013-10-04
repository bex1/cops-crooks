package com.dat255.project.android.copsandcrooks.screens;

import java.util.Map;
import java.util.TreeMap;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class LoadingScreen extends AbstractScreen {

	public LoadingScreen(Assets assets, CopsAndCrooks game) {
		super(assets, game, Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
	}

	@Override
	public void show() {
		super.show();
		Map<String, Role> test = new TreeMap<String, Role>();
		test.put("Tjuv", Role.Crook);
		test.put("Tjuv2", Role.Crook);
		test.put("Polis", Role.Cop);
		GameFactory factory = new GameFactory(assets);
		game.setScreen(factory.loadGame(game, test));
	}
}
