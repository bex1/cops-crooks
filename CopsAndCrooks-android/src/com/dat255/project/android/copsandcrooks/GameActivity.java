package com.dat255.project.android.copsandcrooks;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;

public class GameActivity extends AndroidApplication {
	
	public static final String GAME = "game";
	
	private GameModel game;
	private CopsAndCrooks cops;

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
