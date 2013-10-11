package com.dat255.project.android.copsandcrooks;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;

public class GameActivity extends AndroidApplication {
	
	public static final String GAME = "game";
	
	private GameModel game;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			setContentView(R.layout.activity_game);

			AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
			cfg.useGL20 = true;
			
			CopsAndCrooks cops = new CopsAndCrooks();

			initialize(cops, cfg);
			
			game = cops.getModel();
			
		} else {
			setContentView(R.layout.activity_game);

			AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
			cfg.useGL20 = true;

			game = (GameModel)savedInstanceState.get(GAME);

			initialize(new CopsAndCrooks(game), cfg);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		game = (GameModel)savedInstanceState.get(GAME);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(GAME, game);
	}

	/*@Override
	protected void onResume() {
		setContentView(R.layout.activity_game);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = true;

		game = (GameModel)savedInstanceState.get(GAME);

		initialize(new CopsAndCrooks(game), cfg);
		super.onResume();
	}*/
	
	
}
