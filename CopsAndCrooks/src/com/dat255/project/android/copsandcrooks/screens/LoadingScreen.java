package com.dat255.project.android.copsandcrooks.screens;

import java.util.Map;
import java.util.TreeMap;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class LoadingScreen extends AbstractScreen {

	public LoadingScreen(Assets assets, CopsAndCrooks game) {
		super(assets, game, Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
	}

	@Override
	public void show() {
		super.show();
		GameItem gameitem = new GameItem("Nytt", 2);
		PlayerItem player1 = new PlayerItem("Polis", Role.Cop);
		PlayerItem player2 = new PlayerItem("Tjuv", Role.Crook);
		gameitem.addPlayer(player1);
		gameitem.addPlayer(player2);
		GameFactory.getInstance().init(assets);
		game.setScreen(GameFactory.getInstance().loadGame(game, gameitem, false));
	}
}
