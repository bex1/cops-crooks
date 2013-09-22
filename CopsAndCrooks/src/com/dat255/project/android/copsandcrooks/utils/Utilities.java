package com.dat255.project.android.copsandcrooks.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public final class Utilities {
	private static TextureAtlas atlas;
	
	public synchronized static TextureAtlas getAtlas() {
        if( atlas == null ) {
            atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
        }
        return atlas;
    }
	
	public synchronized static void disposeUtils() {
		if(atlas != null) atlas.dispose();
	}
	
}
