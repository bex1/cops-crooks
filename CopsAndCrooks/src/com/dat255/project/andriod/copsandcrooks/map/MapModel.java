package com.dat255.project.andriod.copsandcrooks.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MapModel {
	
	TiledMap map;

	public MapModel(){
		TmxMapLoader loader = new TmxMapLoader();
		
		map = loader.load("C://users//marcus//Pictures//cops-crooks-v1");
		
		try{
			map.getLayers().get("background").getProperties().;
			map.getLayers().get("interact");
		} catch (Exception e){
			System.out.print("couldn't load the map layer! \n Exception is: " + e);
		}
		try{
			map.getTileSets();
		} catch (Exception e){
			System.out.print("couldn't load the tileset! \n Exception is: " + e);
		}
		
	}
}
