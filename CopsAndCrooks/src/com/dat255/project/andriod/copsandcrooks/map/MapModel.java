package com.dat255.project.andriod.copsandcrooks.map;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class MapModel {
	
	TiledMap map;							
	TiledMapTileLayer mapLayerBack;
	TiledMapTileLayer mapLayerInteract;
	IWalkableTile[][] walkable;

	public MapModel(){
		
		TiledMapTileSet tileset;
		
		map = new TmxMapLoader().load("../CopsAndCrooks/etc/map-images/cops-crooks-map-v1.tmx");  // This loads a TMX file
		mapLayerBack = (TiledMapTileLayer)map.getLayers().get("background");					// Takes out the layer that will contain the background graphics
	
		mapLayerInteract = (TiledMapTileLayer)map.getLayers().get("interact");		// Takes out the layer that will contain the interactive tiles 
		walkable = new IWalkableTile[mapLayerInteract.getWidth()][ mapLayerInteract.getHeight()];				//Creates a matrix that will contain all the diffrent tiles
		for(int i = 0; i < mapLayerInteract.getWidth(); i++){
			for(int j = 0; j < mapLayerInteract.getHeight(); j++){
				try{																//try catch made because of null pointer exception when we dont have a walkable tile when we read inte frpm the .tmx file
					switch(mapLayerInteract.getCell(i, j).getTile().getId()){
					case 1: 	// Acording to the tileset case 1 is the road tiles
						//walkable[i][j] = new RoadTile();
						break;						
					case 2: 	// Acording to the tileset case 2 is the escape tiles
						//walkable[i][j] = new 
						break;
					case 3: 	// Acording to the tileset case 3 is the Polisoffice tiles
						//walkable[i][j] = new PoliceOfficeTile();
						break;
					case 4: 	// Acording to the tileset case 4 is the Tram Stops tiles
						//walkable[i][j] = new TramStopTile();
						break;
					case 5: 	// Acording to the tileset case 5 is the 
						
						break;
					case 6: 	// Dosen't exist
						
						break;
					case 7: 	// Acording to the tileset case 7 is the Hiding tiles
						//walkable[i][j] = new 
						break;
					case 8: 	// Acording to the tileset case 8 is the Travelagency tiles
						//walkable[i][j] = new 
						break;
					case 9: 	// Acording to the tileset case 9 is the Bank tiles containing 2000
						//walkable[i][j] = new 
						break;
					case 10: 	// Acording to the tileset case 10 is the Bank tiles containing 5000
						//walkable[i][j] = new 
						break;
					case 11: 	// Acording to the tileset case 11 is the Bank tiles containing 10000
						//walkable[i][j] = new 
						break;
					case 12: 	// Acording to the tileset case 12 is the Bank tiles containing 20000
						//walkable[i][j] = new 
						break;
					}
				} catch (NullPointerException e){// This Try/- catch is use to eliminate all the tiles that isn't movable
					//walkable[i][j] = null;
				}
			}
		}
		
		try{
			map.getTileSets();
		} catch (Exception e){
			System.out.print("couldn't load the tileset! \n Exception is: " + e);
		}
		
	}
	
	public IWalkableTile[][] getWalkableTiles(){
		return walkable.clone();
	}
	
	public TiledMapTileLayer getBackground(){
		return mapLayerBack;
	}
	
	public TiledMap getMap(){
		return map;
	}
	
	
	
}
