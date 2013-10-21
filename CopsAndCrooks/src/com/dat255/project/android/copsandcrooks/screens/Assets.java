package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The assets class provides assets access.
 * 
 * We use a TextureAtlas for textures, a skin for ui, and a Tiledmap for the map.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class Assets {
	private final TextureAtlas atlas;
	private final Skin skin;
	private final TiledMap map;
	
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
