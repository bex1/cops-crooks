package com.dat255.project.android.copsandcrooks;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.model.GameModel;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.view.CopsAndCrooks;

/**
 * This activity represents the game itself. This is where the game is
 * displayed.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class GameActivity extends AndroidApplication {
	
	public static final String GAME = "game";

	private CopsAndCrooks cops;
	private CommunicateTask turnUpdateTask ;

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

		
		
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(GAME, cops.getModel());
	}

	@Override
	protected void onPause() {
		this.turnUpdateTask.cancel(true);
		super.onPause();
	}

	@Override
	protected void onResume() {
		GameClient.getInstance().setCurrentGameModel(cops.getModel());
		turnUpdateTask = new CommunicateTask(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			turnUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
		else
			turnUpdateTask.execute();
		super.onResume();
	}
	
}
