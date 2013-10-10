package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	private TextureAtlas atlas;
	private Skin skin;
	private TiledMap map;
	
	public Assets() {
		atlas = new TextureAtlas(Gdx.files.internal("image-atlases/pages.atlas"));
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        map = new TmxMapLoader().load("map-images/cops-crooks-map-v2.tmx"); 
	}
	
	public TextureAtlas getAtlas() {
        return atlas;
    }
	
	public Skin getSkin() {
        return skin;
    }
	
	public TiledMap getMap() {
		return map;
	}
}
