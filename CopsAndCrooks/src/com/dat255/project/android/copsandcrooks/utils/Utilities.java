package com.dat255.project.android.copsandcrooks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Utilities {
	private static TextureAtlas atlas;
	private static Skin skin;
	
	public synchronized static TextureAtlas getAtlas() {
        if( atlas == null ) {
            atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
        }
        return atlas;
    }
	
	public synchronized static Skin getSkin()
    {
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
            skin = new Skin(skinFile);
        }
        return skin;
    }
	
	public synchronized static void disposeUtils() {
		if(atlas != null) atlas.dispose();
	}
	
}
