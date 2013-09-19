package com.dat255.project.andriod.copsandcrooks.map;

import java.awt.Point;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
/**
 * This class creates a 2 dimension world from an .tmx file, that is 
 * Intractable and reads the background
 * @author Group 25
 *
 */
public class MapModel {
	
	TiledMap map;							
	TiledMapTileLayer mapLayerBack;
	TiledMapTileLayer mapLayerInteract;
	IWalkableTile[][] walkable;
	Point policeCarStart = new Point();

	public MapModel(){
		
		// This loads a TMX file
		map = new TmxMapLoader().load("../CopsAndCrooks/etc/map-images/cops-crooks-map-v1.tmx");  
		
		// Takes out the layer that will contain the background graphics
		mapLayerBack = (TiledMapTileLayer)map.getLayers().get("background");					
	
		// Takes out the layer that will contain the interactive tiles
		mapLayerInteract = (TiledMapTileLayer)map.getLayers().get("interact");		
		
		//Creates a matrix that will contain all the diffrent tiles
		walkable = new IWalkableTile[mapLayerInteract.getWidth()][ mapLayerInteract.getHeight()];				
		
		for(int i = 0; i < mapLayerInteract.getWidth(); i++){
			for(int j = 0; j < mapLayerInteract.getHeight(); j++){
				
				//try catch made because of null pointer exception when we dont have a walkable tile when we read inte frpm the .tmx file
				try{
					
					switch(mapLayerInteract.getCell(i, j).getTile().getId()){
					case 1: 	// Acording to the tileset case 1 is the road tiles
						//walkable[i][j] = new RoadTile(new Point(i, j));
						break;						
					case 2: 	// Acording to the tileset case 2 is the get away tiles
						//walkable[i][j] = new GetAway(new Point(i, j));
						break;
					case 3: 	// Acording to the tileset case 3 is the polisoffice tiles
						//walkable[i][j] = new PoliceStation(new Point(i, j));
						break;
					case 4: 	// Acording to the tileset case 4 is the tramstops tiles
						//walkable[i][j] = new TramStop(new Point(i, j));
						break;
					case 5: 	// Acording to the tileset case 5 is the IntelligenceAgency tiles
						//walkable[i][j] = new IntelligenceAgency(new Point(i, j));
						break;
					case 6: 	// Acording to tileset case 6 will be the coordinate of the poliscar(this will be a road tile)
						//walkable[i][j] = new RoadTile(new Point(i, j));
						policeCarStart.setLocation(i, j);
						break;
					case 7: 	// Acording to the tileset case 7 is the Hiding tiles
						//walkable[i][j] = new Hideout(new Point(i, j));
						break;
					case 8: 	// Acording to the tileset case 8 is the Travelagency tiles
						//walkable[i][j] = new TravelAgency(new Point(i, j));
						break;
					case 9: 	// Acording to the tileset case 9 is the Bank tiles containing 2000
						//walkable[i][j] = new RobbableBuilding(new Point(i, j), 2000);
						break;
					case 10: 	// Acording to the tileset case 10 is the Bank tiles containing 5000
						//walkable[i][j] = new  RobbableBuilding(new Point(i, j), 5000);
						break;
					case 11: 	// Acording to the tileset case 11 is the Bank tiles containing 10000
						//walkable[i][j] = new  RobbableBuilding(new Point(i, j), 10000);
						break;
					case 12: 	// Acording to the tileset case 12 is the Bank tiles containing 20000
						//walkable[i][j] = new  RobbableBuilding(new Point(i, j), 20000);
						break;
					}
				} catch (NullPointerException e){
					//walkable[i][j] = null;
				}
			}
		}		
	}
	
	/**
	 * 
	 * @return - A point containing the coordinate of the Spawningpoint of the police car
	 */
	public Point getPolicaCarRespawnPoint(){
		return (Point) policeCarStart.clone();
	}
	
	/**
	 * 
	 * @return - Returns the 2 d array containing the interactive tiles
	 */
	public IWalkableTile[][] getWalkableTiles(){
		return walkable.clone();
	}
	
	/**
	 * 
	 * @return - This returns the background layer 
	 */
	public TiledMapTileLayer getBackground(){
		return mapLayerBack;
	}
	
	/**
	 * 
	 * @return - the whole map that's created
	 */
	public TiledMap getMap(){
		return map;
	}
	
	
	
}
