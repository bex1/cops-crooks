package com.dat255.project.android.copsandcrooks.screens;

import java.util.Collection;

import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;

public class ScoreScreen extends AbstractScreen {
	
	private ScoreBoardTable scoreBoard;

	public ScoreScreen(Assets assets, CopsAndCrooks game, float stageWidth,
			float stageHeight, final Collection<? extends IPlayer> players) {
		super(assets, game, stageWidth, stageHeight);
		new ScoreBoardTable(assets, players);
	}
	
	@Override
	public void show(){
		super.show();
		stage.addActor(scoreBoard);
	}

}
