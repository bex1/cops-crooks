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

	private CopsAndCrooks cops;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		if (savedInstanceState == null)
			cops = new CopsAndCrooks();
		else
			cops = new CopsAndCrooks((GameModel)savedInstanceState.getSerializable(GAME));

		initialize(cops, cfg);

		GameClient.getInstance().setCurrentGameModel(cops.getModel());

		
		
		CommunicateTask turnUpdateTask = new CommunicateTask(this);
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
