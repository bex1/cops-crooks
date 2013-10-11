package com.dat255.project.android.copsandcrooks.screens;

import java.util.Map;
import java.util.TreeMap;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.network.GameClient;
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
		GameItem gameToPlay = GameClient.getInstance().getChosenGameItem();
		GameFactory factory = GameFactory.getInstance();
		if(!gameToPlay.hasGameStarted()){
			game.setScreen(GameFactory.getInstance().loadGame(game, gameToPlay, false));
		}else if(gameToPlay.hasGameStarted() && !factory.hasLoadedThisGameModel(gameToPlay)){
			game.setScreen(GameFactory.getInstance().loadGame(game, gameToPlay, true));
		}else if(gameToPlay.hasGameStarted() && factory.hasLoadedThisGameModel(gameToPlay)){
			game.setScreen(GameFactory.getInstance().loadLocalGame(game, gameToPlay));
		}else{
			System.out.println("FAIL");
			game.setScreen(GameFactory.getInstance().loadGame(game, gameToPlay, false));
		}
	}
}
