package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	private AssetManager manager;
	private TextureAtlas atlas;
	private Skin skin;
	
	public Assets() {
		manager = new AssetManager();
		atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
		manager.load("image-atlases/pages.atlas", TextureAtlas.class);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        manager.load("skin/uiskin.json", Skin.class);
        Texture.setAssetManager(manager);
	}
	
	public AssetManager getAssetManager() {
		return manager;
	}
	
	public TextureAtlas getAtlas() {
        return atlas;
    }
	
	public Skin getSkin() {
        return skin;
    }
	
	public void dispose() {
		manager.dispose();
	}
}
