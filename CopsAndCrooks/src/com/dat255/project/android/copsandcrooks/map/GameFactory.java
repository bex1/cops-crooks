package com.dat255.project.android.copsandcrooks.map;

import java.awt.Point;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.*;
import com.dat255.project.android.copsandcrooks.screens.GameScreen;
/**
 * 
 * @author Group 25
 *
 */
public class GameFactory {

	/**
	 * 
	 * @return
	 */
	public static Screen loadGame(int numberOfPlayers, CopsAndCrooks game){
		
		// Creates a mediator
		Mediator mediator = new Mediator();
		
		//The point of the police car respawnpoint
		Point policeCarStart = new Point();
		
		// This loads a TMX file
		TiledMap map = new TmxMapLoader().load("../CopsAndCrooks/etc/map-images/cops-crooks-map-v1.tmx");  
		
		// Takes out the layer that will contain the background graphics
		TiledMapTileLayer mapLayerBack;
		// Takes out the layer that will contain the interactive tiles
		TiledMapTileLayer mapLayerInteract;
		try {
			mapLayerBack = (TiledMapTileLayer)map.getLayers().get("background");					

			mapLayerInteract = (TiledMapTileLayer)map.getLayers().get("interact");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		
		//Creates a matrix that will contain all the diffrent tiles
		IWalkableTile[][] walkable = new IWalkableTile[mapLayerInteract.getWidth()][ mapLayerInteract.getHeight()];				
		
		for(int i = 0; i < mapLayerInteract.getWidth(); i++){
			for(int j = 0; j < mapLayerInteract.getHeight(); j++){
				
				//try catch made because of null pointer exception when we dont have a walkable tile when we read inte frpm the .tmx file
				try{
					
					switch(mapLayerInteract.getCell(i, j).getTile().getId()){
					case 1: 	// Acording to the tileset case 1 is the road tiles
						walkable[i][j] = new RoadTile(new Point(i, j));
						break;						
					case 2: 	// Acording to the tileset case 2 is the get away tiles
						walkable[i][j] = new GetAway(new Point(i, j));
						break;
					case 3: 	// Acording to the tileset case 3 is the polisoffice tiles
						walkable[i][j] = new PoliceStation(new Point(i, j));
						break;
					case 4: 	// Acording to the tileset case 4 is the tramstops tiles
						walkable[i][j] = new TramStop(new Point(i, j));
						break;
					case 5: 	// Acording to the tileset case 5 is the IntelligenceAgency tiles
						walkable[i][j] = new IntelligenceAgency(new Point(i, j));
						break;
					case 6: 	// Acording to tileset case 6 will be the coordinate of the poliscar(this will be a road tile)
						walkable[i][j] = new RoadTile(new Point(i, j));
						policeCarStart.setLocation(i, j);
						break;
					case 7: 	// Acording to the tileset case 7 is the Hiding tiles
						walkable[i][j] = new Hideout(new Point(i, j), null);
						break;
					case 8: 	// Acording to the tileset case 8 is the Travelagency tiles
						walkable[i][j] = new TravelAgency(new Point(i, j));
						break;
					case 9: 	// Acording to the tileset case 9 is the Bank tiles containing 2000
						walkable[i][j] = new RobbableBuilding(new Point(i, j), 2000);
						break;
					case 10: 	// Acording to the tileset case 10 is the Bank tiles containing 5000
						walkable[i][j] = new  RobbableBuilding(new Point(i, j), 5000);
						break;
					case 11: 	// Acording to the tileset case 11 is the Bank tiles containing 10000
						walkable[i][j] = new  RobbableBuilding(new Point(i, j), 10000);
						break;
					case 12: 	// Acording to the tileset case 12 is the Bank tiles containing 20000
						walkable[i][j] = new  RobbableBuilding(new Point(i, j), 20000);
						break;
					}
				} catch (NullPointerException e){
					walkable[i][j] = null;
				}
			}
		}
		//create a game model
		GameModel gameModel =new GameModel(mediator, null, walkable);
		
		// create the controller and view of the game
		return new GameScreen(game, gameModel, map, mapLayerBack);
		
	}
	
}
