package com.dat255.project.android.copsandcrooks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CopsAndCrooks";
		cfg.useGL20 = true;
		cfg.width = Values.GAME_VIEWPORT_WIDTH;
		cfg.height = Values.GAME_VIEWPORT_HEIGHT;
		
		new LwjglApplication(new CopsAndCrooks(CopsAndCrooks.Platform.Desktop), cfg);
	}
}
