package com.dat255.project.android.copsandcrooks;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

public class GameActivity extends AndroidApplication {
	
	public static final String GAME = "game";
	
	private GameModel game;
	private CopsAndCrooks cops;

	private CommunicateTask turnUpdateTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (savedInstanceState == null) {
			setContentView(R.layout.activity_game);

			AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
			cfg.useGL20 = true;
			
			cops = new CopsAndCrooks();

			initialize(cops, cfg);
		} else {
			setContentView(R.layout.activity_game);

			AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
			cfg.useGL20 = true;
			
			game = (GameModel)savedInstanceState.getSerializable(GAME);
			
			cops = new CopsAndCrooks(game);

			initialize(cops, cfg);
		}

		GameClient.getInstance().setCurrentGameModel(cops.getModel());

		turnUpdateTask = new CommunicateTask(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			turnUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
		else
			turnUpdateTask.execute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(GAME, cops.getModel());
	}

	@Override
	protected void onStop() {
		this.finish();
		super.onStop();
	}
	
	
}
