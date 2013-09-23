package com.dat255.project.android.copsandcrooks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.utils.Constants;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CopsAndCrooks";
		cfg.useGL20 = true;
		cfg.width = Constants.GAME_VIEWPORT_WIDTH;
		cfg.height = Constants.GAME_VIEWPORT_HEIGHT;
		
		new LwjglApplication(new CopsAndCrooks(), cfg);
	}
}
