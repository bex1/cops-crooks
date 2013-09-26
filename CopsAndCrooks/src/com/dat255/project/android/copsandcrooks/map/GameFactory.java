package com.dat255.project.android.copsandcrooks.map;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.CrookActor;
import com.dat255.project.android.copsandcrooks.actors.CrookActor.CrookAnimations;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.GetAwayTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IntelligenceAgencyTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.PoliceStationTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RobbableBuildingTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.TramStopTile;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.TravelAgencyTile;
import com.dat255.project.android.copsandcrooks.screens.GameScreen;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Utilities;
import com.dat255.project.android.copsandcrooks.utils.Values;
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
		TiledMap map = new TmxMapLoader().load("map-images/cops-crooks-map-v1.tmx");  
		
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
		Values.TILE_WIDTH = (int) mapLayerBack.getTileWidth();
		Values.TILE_HEIGTH = (int) mapLayerBack.getTileHeight();
		
		List<Player> players = new ArrayList<Player>();
		List<IMovable> pawns = new ArrayList<IMovable>();
		List<Actor> actors = new ArrayList<Actor>();
		
		// Testing of crookactor
		Crook crook = new Crook(mediator);
		
		// Using an enumeration map makes sure that all keys passed are valid keys
		EnumMap<CrookAnimations, Animation> crookAnimations = new EnumMap<CrookAnimations, Animation>(CrookAnimations.class);
		
		AtlasRegion[] stopAnimation = new AtlasRegion[8];
		for(int i = 0; i < 8; i++)
		{
			stopAnimation[i] = Utilities.getAtlas().findRegion("game-screen/crook/stopped"+String.format("%04d", i));
		}
		Animation idle = new Animation(1f, stopAnimation);
		
		crookAnimations.put(CrookAnimations.IDLE_ANIM, idle);
		
		AtlasRegion[] walkEastAnimation = new AtlasRegion[8];
		for(int i = 0; i < 8; i++)
		{
			walkEastAnimation[i] = Utilities.getAtlas().findRegion("game-screen/crook/walking e"+String.format("%04d", i));
		}
		Animation walkEast = new Animation(0.2f, walkEastAnimation);
		
		crookAnimations.put(CrookAnimations.WALK_EAST_ANIM, walkEast);
		
		AtlasRegion[] walkNorthAnimation = new AtlasRegion[8];
		for(int i = 0; i < 8; i++)
		{
			walkNorthAnimation[i] = Utilities.getAtlas().findRegion("game-screen/crook/walking n"+String.format("%04d", i));
		}
		Animation walkNorth = new Animation(0.2f, walkNorthAnimation);
		
		crookAnimations.put(CrookAnimations.WALK_NORTH_ANIM, walkNorth);
		
		AtlasRegion[] walkSouthAnimation = new AtlasRegion[8];
		for(int i = 0; i < 8; i++)
		{
			walkSouthAnimation[i] = Utilities.getAtlas().findRegion("game-screen/crook/walking s"+String.format("%04d", i));
		}
		Animation walkSouth = new Animation(0.2f, walkSouthAnimation);
		
		crookAnimations.put(CrookAnimations.WALK_SOUTH_ANIM, walkSouth);
		
		AtlasRegion[] walkWestAnimation = new AtlasRegion[8];
		for(int i = 0; i < 8; i++)
		{
			walkWestAnimation[i] = Utilities.getAtlas().findRegion("game-screen/crook/walking w"+String.format("%04d", i));
		}
		Animation walkWest = new Animation(0.2f, walkWestAnimation);
		
		crookAnimations.put(CrookAnimations.WALK_WEST_ANIM, walkWest);
		
		// Specify the first drawable frame
        TextureRegionDrawable drawable = new TextureRegionDrawable(stopAnimation[0]);
     	
        // Create our crook actor
        CrookActor crookActor = new CrookActor(drawable, Scaling.none, crook, crookAnimations);
      
        
        actors.add(crookActor);
		
		pawns.add(crook);
		
		Player player = new Player("Gunnar", pawns, Role.Crook, mediator);
		players.add(player);
		
		//Creates a matrix that will contain all the diffrent tiles
		IWalkableTile[][] walkable = new IWalkableTile[mapLayerInteract.getWidth()][ mapLayerInteract.getHeight()];				
		
		for(int i = 0; i < mapLayerInteract.getWidth(); i++){
			for(int j = 0; j < mapLayerInteract.getHeight(); j++){
				
				//try catch made because of null pointer exception when we dont have a walkable tile when we read inte frpm the .tmx file
				try{
					
					switch(mapLayerInteract.getCell(i, j).getTile().getId()){
					case 1: 	// Acording to the tileset case 1 is the road tiles
						walkable[i][j] = new RoadTile(new Point(i, j), mediator);
						break;						
					case 2: 	// Acording to the tileset case 2 is the get away tiles
						walkable[i][j] = new GetAwayTile(new Point(i, j), mediator);
						break;
					case 3: 	// Acording to the tileset case 3 is the polisoffice tiles
						walkable[i][j] = new PoliceStationTile(new Point(i, j), mediator);
						break;
					case 4: 	// Acording to the tileset case 4 is the tramstops tiles
						walkable[i][j] = new TramStopTile(new Point(i, j), mediator);
						break;
					case 5: 	// Acording to the tileset case 5 is the IntelligenceAgency tiles
						walkable[i][j] = new IntelligenceAgencyTile(new Point(i, j), mediator);
						break;
					case 6: 	// Acording to tileset case 6 will be the coordinate of the poliscar(this will be a road tile)
						walkable[i][j] = new RoadTile(new Point(i, j), mediator);
						policeCarStart.set(i, j);
						break;
					case 7: 	// Acording to the tileset case 7 is the Hiding tiles
						walkable[i][j] = new HideoutTile(new Point(i, j), null, mediator);
						break;
					case 8: 	// Acording to the tileset case 8 is the Travelagency tiles
						TravelAgencyTile.createTravelAgency(new Point(i, j), mediator);
						walkable[i][j] = TravelAgencyTile.getInstance();
						break;
					case 9: 	// Acording to the tileset case 9 is the Bank tiles containing 2000
						walkable[i][j] = new RobbableBuildingTile(new Point(i, j), mediator, 2000);
						break;
					case 10: 	// Acording to the tileset case 10 is the Bank tiles containing 5000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 5000);
						break;
					case 11: 	// Acording to the tileset case 11 is the Bank tiles containing 10000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 10000);
						break;
					case 12: 	// Acording to the tileset case 12 is the Bank tiles containing 20000
						walkable[i][j] = new  RobbableBuildingTile(new Point(i, j), mediator, 20000);
						break;
					}
				} catch (NullPointerException e){
					walkable[i][j] = null;
				}
			}
		}
		
		//create a game model
		GameModel gameModel =new GameModel(mediator, players, walkable);
		
		// Test for path actors
		/*TilePath path = new TilePath();
		path.addTile(new RoadTile(new Point(0, 4), mediator));
		path.addTile(new RoadTile(new Point(0, 5), mediator));
		path.addTile(new RoadTile(new Point(1, 5), mediator));
		path.addTile(new RoadTile(new Point(2, 5), mediator));
		path.addTile(new RoadTile(new Point(3, 5), mediator));
		path.addTile(new RoadTile(new Point(3, 4), mediator));
		path.addTile(new RoadTile(new Point(3, 3), mediator));
		path.addTile(new RoadTile(new Point(2, 3), mediator));
		path.addTile(new RoadTile(new Point(2, 2), mediator));
		path.addTile(new RoadTile(new Point(1, 2), mediator));
		
		List<TilePath> paths = new ArrayList<TilePath>();
		paths.add(path);
		List<PathActor> pathActors = getPathActorsFor(paths, player);
		
		actors.addAll(pathActors);
		For the test to work, uncomment the check in Player.choosePath*/
		// Test end
	
		// create the controller and view of the game
		return new GameScreen(game, gameModel, map, mapLayerBack, actors);
		
	}
	
	/**
	 * Creates path actors for the specified paths connected to the specified player.
	 * 
	 * @param paths The paths to create PathActors for.
	 * @param player The player who can click the paths.
	 * @return A list of PathActors.
	 */
	public static List<PathActor> getPathActorsFor(List<TilePath> paths, Player player) {
		
		List<PathActor> pathActors = new ArrayList<PathActor>();
	
		List<Image> pathImages = new ArrayList<Image>();
		
		for (TilePath path : paths) {
			
			for (int i = 1; i < path.getPathSize(); i++) {
				AtlasRegion region = Utilities.getAtlas().findRegion("game-screen/path/GreenDotPath");
				
				Image pathImage = new Image(region);
				Point pathTilePos = path.getTile(i).getPosition();
				pathImage.setPosition(pathTilePos.x * Values.TILE_WIDTH, 
									  pathTilePos.y * Values.TILE_HEIGTH);
				pathImages.add(pathImage);
				
			}
			
			AtlasRegion region = Utilities.getAtlas().findRegion("game-screen/path/GreenDotPathEnd");
			Image pathEnd = new Image(region);
			Point pathEndPos = path.getTile(0).getPosition();
			pathEnd.setPosition(pathEndPos.x * Values.TILE_WIDTH, 
								pathEndPos.y * Values.TILE_HEIGTH);
			
			pathActors.add(new PathActor(path, pathImages, pathEnd, player));
		}
		return pathActors;
	}
	
}
