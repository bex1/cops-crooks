package com.dat255.project.android.copsandcrooks.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.CopActor;
import com.dat255.project.android.copsandcrooks.actors.CopCarActor;
import com.dat255.project.android.copsandcrooks.actors.DiceActor;
import com.dat255.project.android.copsandcrooks.actors.MetroLineActor;
import com.dat255.project.android.copsandcrooks.actors.MovableActor;
import com.dat255.project.android.copsandcrooks.actors.MovableActor.Animations;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.AbstractPawn;
import com.dat255.project.android.copsandcrooks.domainmodel.AbstractWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.CopCar;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Dice;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.GetAwayTile;
import com.dat255.project.android.copsandcrooks.domainmodel.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.IntelligenceAgencyTile;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.Officer;
import com.dat255.project.android.copsandcrooks.domainmodel.PathFinder;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.PoliceStationTile;
import com.dat255.project.android.copsandcrooks.domainmodel.RoadTile;
import com.dat255.project.android.copsandcrooks.domainmodel.RobbableBuildingTile;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.TramLine;
import com.dat255.project.android.copsandcrooks.domainmodel.TramStopTile;
import com.dat255.project.android.copsandcrooks.domainmodel.TravelAgencyTile;
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
	
	private static TextureAtlas atlas = Utilities.getAtlas();

	/**
	 * 
	 * @return
	 */
	public static Screen loadGame(CopsAndCrooks game, Map<String, Role> userInfo){
		// Creates a mediator
		Mediator mediator = new Mediator();
	
		//The tile of the police car respawnpoint
		RoadTile policeCarStart = null;
		
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
		
		//Creates a matrix that will contain all the diffrent tiles
		AbstractWalkableTile[][] walkable = new AbstractWalkableTile[mapLayerInteract.getWidth()][ mapLayerInteract.getHeight()];				
		List<HideoutTile> listOfHideOut = new ArrayList<HideoutTile>();
		List<PoliceStationTile> listOfPoliceStation = new ArrayList<PoliceStationTile>();
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
						PoliceStationTile station = new PoliceStationTile(new Point(i, j), mediator);
						walkable[i][j] = station;
						listOfPoliceStation.add(station);
						break;
					case 4: 	// Acording to the tileset case 4 is the tramstops tiles
						walkable[i][j] = new TramStopTile(new Point(i, j), mediator);
						break;
					case 5: 	// Acording to the tileset case 5 is the IntelligenceAgency tiles
						walkable[i][j] = new IntelligenceAgencyTile(new Point(i, j), mediator);
						break;
					case 6: 	// Acording to tileset case 6 will be the coordinate of the poliscar(this will be a road tile)
						policeCarStart = new RoadTile(new Point(i, j), mediator);
						walkable[i][j] = policeCarStart;
						break;
					case 7: 	// Acording to the tileset case 7 is the Hiding tiles
						HideoutTile hideout = new HideoutTile(new Point(i, j), null, mediator);
						walkable[i][j] = hideout;
						listOfHideOut.add(hideout);
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
		
		List<Player> players = new ArrayList<Player>();
		List<Actor> actors = new ArrayList<Actor>();
		
		int numberOfOfficers = userInfo.keySet().size();
		int numberOfCopCars = 1;
		for (String name : userInfo.keySet()) {
			List<AbstractPawn> pawns = new ArrayList<AbstractPawn>();
			if (userInfo.get(name) == Role.Police) {
				for (int i = 0; i < numberOfOfficers; i++) {
					Officer officer = new Officer(listOfPoliceStation.get(i+1), mediator);
					pawns.add(officer);

					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getOfficerAnimations();

					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));

					actors.add(new CopActor(drawable, Scaling.none, officer, pawnAnimations));
				}
				for (int i = 0; i < numberOfCopCars; i++) {
					CopCar copCar = new CopCar(listOfPoliceStation.get(i), mediator);
					pawns.add(copCar);

					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getCopCarAnimations();

					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));

					actors.add(new CopCarActor(drawable, Scaling.none, copCar, pawnAnimations));
				}
				
				players.add( new Player(name, pawns, Role.Police, mediator));
				
			} else if (userInfo.get(name) == Role.Crook) {
				Crook crook = new Crook(listOfHideOut.get(0), mediator);
				pawns.add(crook);
				
				// Get animations
				EnumMap<Animations, Animation> pawnAnimations = getCrookAnimations();
				
				// Specify the first drawable frame
		        TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
				
				actors.add(new MovableActor(drawable, Scaling.none, crook, pawnAnimations));
				
				players.add( new Player(name, pawns, Role.Crook, mediator));
			}
		}
		

		//create a game model
		GameModel gameModel =new GameModel(mediator, players.get(0), players, walkable);
	
		// create the controller and view of the game
		Dice dice = new Dice(mediator);
		DiceActor diceActor = getDiceActorFor(dice);
		new PathFinder(walkable, mediator, new ArrayList<TramLine>());
		return new GameScreen(game, gameModel, map, mapLayerBack, actors, diceActor);
	}
	
	private static EnumMap<Animations, Animation> getCopCarAnimations() {
		// Using an enumeration map makes sure that all keys passed are valid keys
		EnumMap<Animations, Animation> pawnAnimations = new EnumMap<Animations, Animation>(Animations.class);

		AtlasRegion[] stopAnimation = new AtlasRegion[1];
		for(int k = 0; k < 1; k++)
		{
			stopAnimation[k] = atlas.findRegion("game-screen/copcar/copcar s");
		}
		Animation idle = new Animation(1f, stopAnimation);

		pawnAnimations.put(Animations.IDLE_ANIM, idle);

		AtlasRegion[] walkEastAnimation = new AtlasRegion[1];
		for(int k = 0; k < 1; k++)
		{
			walkEastAnimation[k] = atlas.findRegion("game-screen/copcar/copcar e");
		}
		Animation walkEast = new Animation(0.2f, walkEastAnimation);

		pawnAnimations.put(Animations.MOVE_EAST_ANIM, walkEast);

		AtlasRegion[] walkNorthAnimation = new AtlasRegion[1];
		for(int k = 0; k < 1; k++)
		{
			walkNorthAnimation[k] = atlas.findRegion("game-screen/copcar/copcar n");
		}
		Animation walkNorth = new Animation(0.2f, walkNorthAnimation);

		pawnAnimations.put(Animations.MOVE_NORTH_ANIM, walkNorth);

		AtlasRegion[] walkSouthAnimation = new AtlasRegion[1];
		for(int k = 0; k < 1; k++)
		{
			walkSouthAnimation[k] = atlas.findRegion("game-screen/copcar/copcar s");
		}
		Animation walkSouth = new Animation(0.2f, walkSouthAnimation);

		pawnAnimations.put(Animations.MOVE_SOUTH_ANIM, walkSouth);

		AtlasRegion[] walkWestAnimation = new AtlasRegion[1];
		for(int k = 0; k < 1; k++)
		{
			walkWestAnimation[k] = atlas.findRegion("game-screen/copcar/copcar w");
		}
		Animation walkWest = new Animation(0.2f, walkWestAnimation);

		pawnAnimations.put(Animations.MOVE_WEST_ANIM, walkWest);

		return pawnAnimations;
	}

	private static EnumMap<Animations, Animation> getCrookAnimations() {
		// Using an enumeration map makes sure that all keys passed are valid keys
		EnumMap<Animations, Animation> pawnAnimations = new EnumMap<Animations, Animation>(Animations.class);

		AtlasRegion[] stopAnimation = new AtlasRegion[8];
		for(int k = 0; k < 8; k++)
		{
			stopAnimation[k] = atlas.findRegion("game-screen/crook/stopped"+String.format("%04d", k));
		}
		Animation idle = new Animation(1f, stopAnimation);
		
		pawnAnimations.put(Animations.IDLE_ANIM, idle);
		
		AtlasRegion[] walkEastAnimation = new AtlasRegion[8];
		for(int k = 0; k < 8; k++)
		{
			walkEastAnimation[k] = atlas.findRegion("game-screen/crook/walking e"+String.format("%04d", k));
		}
		Animation walkEast = new Animation(0.2f, walkEastAnimation);
		
		pawnAnimations.put(Animations.MOVE_EAST_ANIM, walkEast);
		
		AtlasRegion[] walkNorthAnimation = new AtlasRegion[8];
		for(int k = 0; k < 8; k++)
		{
			walkNorthAnimation[k] = atlas.findRegion("game-screen/crook/walking n"+String.format("%04d", k));
		}
		Animation walkNorth = new Animation(0.2f, walkNorthAnimation);
		
		pawnAnimations.put(Animations.MOVE_NORTH_ANIM, walkNorth);
		
		AtlasRegion[] walkSouthAnimation = new AtlasRegion[8];
		for(int k = 0; k < 8; k++)
		{
			walkSouthAnimation[k] = atlas.findRegion("game-screen/crook/walking s"+String.format("%04d", k));
		}
		Animation walkSouth = new Animation(0.2f, walkSouthAnimation);
		
		pawnAnimations.put(Animations.MOVE_SOUTH_ANIM, walkSouth);
		
		AtlasRegion[] walkWestAnimation = new AtlasRegion[8];
		for(int k = 0; k < 8; k++)
		{
			walkWestAnimation[k] = atlas.findRegion("game-screen/crook/walking w"+String.format("%04d", k));
		}
		Animation walkWest = new Animation(0.2f, walkWestAnimation);
		
		pawnAnimations.put(Animations.MOVE_WEST_ANIM, walkWest);
		
		return pawnAnimations;
	}

	private static EnumMap<Animations, Animation> getOfficerAnimations() {
		// Using an enumeration map makes sure that all keys passed are valid keys
		EnumMap<Animations, Animation> pawnAnimations = new EnumMap<Animations, Animation>(Animations.class);
		
		AtlasRegion[] stopAnimation = new AtlasRegion[1];
		for(int k = 1; k <= 1; k++)
		{
			stopAnimation[k-1] = atlas.findRegion("game-screen/officer/stopped s"+k);
		}
		Animation idle = new Animation(1f, stopAnimation);
		
		pawnAnimations.put(Animations.IDLE_ANIM, idle);
		
		AtlasRegion[] walkEastAnimation = new AtlasRegion[4];
		for(int k = 1; k <= 4; k++)
		{
			walkEastAnimation[k-1] = atlas.findRegion("game-screen/officer/walking e"+k);
		}
		Animation walkEast = new Animation(0.2f, walkEastAnimation);
		
		pawnAnimations.put(Animations.MOVE_EAST_ANIM, walkEast);
		
		AtlasRegion[] walkNorthAnimation = new AtlasRegion[4];
		for(int k = 1; k <= 4; k++)
		{
			walkNorthAnimation[k-1] = atlas.findRegion("game-screen/officer/walking n"+k);
		}
		Animation walkNorth = new Animation(0.2f, walkNorthAnimation);
		
		pawnAnimations.put(Animations.MOVE_NORTH_ANIM, walkNorth);
		
		AtlasRegion[] walkSouthAnimation = new AtlasRegion[4];
		for(int k = 1; k <= 4; k++)
		{
			walkSouthAnimation[k-1] = atlas.findRegion("game-screen/officer/walking s"+k);
		}
		Animation walkSouth = new Animation(0.2f, walkSouthAnimation);
		
		pawnAnimations.put(Animations.MOVE_SOUTH_ANIM, walkSouth);
		
		AtlasRegion[] walkWestAnimation = new AtlasRegion[4];
		for(int k = 1; k <= 4; k++)
		{
			walkWestAnimation[k-1] = atlas.findRegion("game-screen/officer/walking w"+k);
		}
		Animation walkWest = new Animation(0.2f, walkWestAnimation);
		
		pawnAnimations.put(Animations.MOVE_WEST_ANIM, walkWest);
		
		return pawnAnimations;
	}

	/**
	 * Creates path actors for the specified paths connected to the specified player.
	 * 
	 * @param paths The paths to create PathActors for.
	 * @param player The player who can click the paths.
	 * @return A list of PathActors.
	 */
	public static List<PathActor> getPathActorsFor(Collection<TilePath> paths, IPlayer player) {
		
		List<PathActor> pathActors = new ArrayList<PathActor>();
		
		if (paths == null || paths.isEmpty()) {
			return null;
		}
	
		List<Image> pathImages = new ArrayList<Image>();
		
		for (TilePath path : paths) {
			
			for (int i = 1; i < path.getPathLength(); i++) {
				AtlasRegion region = atlas.findRegion("game-screen/path/GreenDotPath");
				
				Image pathImage = new Image(region);
				Point pathTilePos = path.getTile(i).getPosition();
				pathImage.setPosition(pathTilePos.x * Values.TILE_WIDTH, 
									  pathTilePos.y * Values.TILE_HEIGTH);
				pathImages.add(pathImage);
				
			}
			
			AtlasRegion region = atlas.findRegion("game-screen/path/GreenDotPathEnd");
			Image pathEnd = new Image(region);
			Point pathEndPos = path.getTile(0).getPosition();
			pathEnd.setPosition(pathEndPos.x * Values.TILE_WIDTH, 
								pathEndPos.y * Values.TILE_HEIGTH);
			
			AtlasRegion clickRegion = atlas.findRegion("game-screen/path/GreenDotPathEndClick");
			Image pathEndClick = new Image(clickRegion);
			pathEndClick.setPosition(pathEndPos.x * Values.TILE_WIDTH, 
								pathEndPos.y * Values.TILE_HEIGTH);
			
			pathActors.add(new PathActor(path, pathImages, pathEnd, pathEndClick, player));
		}
		return pathActors;
	}
	
	/**
	 * Creates path actors for the specified paths connected to the specified player.
	 * 
	 * @param paths The paths to create PathActors for.
	 * @param player The player who can click the paths.
	 * @return A list of PathActors.
	 */
	public static List<MetroLineActor> getMetroActorsFor(Collection<TilePath> paths, IPlayer player) {
		
		List<MetroLineActor> pathActors = new ArrayList<MetroLineActor>();
		
		if (paths == null || paths.isEmpty()) {
			return null;
		}
	
		List<Image> pathImages = new ArrayList<Image>();
		
		for (TilePath path : paths) {
			
			for (int i = 0; i < path.getPathLength(); i++) {
				AtlasRegion region = atlas.findRegion("game-screen/path/GreenDotPathEnd");
				
				Image pathImage = new Image(region);
				Point pathTilePos = path.getTile(i).getPosition();
				pathImage.setPosition(pathTilePos.x * Values.TILE_WIDTH, 
									  pathTilePos.y * Values.TILE_HEIGTH);
				pathImages.add(pathImage);
				
			}
			
			
			AtlasRegion clickRegion = atlas.findRegion("game-screen/path/GreenDotPathEndClick");
			Image pathClick = new Image(clickRegion);
			
			pathActors.add(new MetroLineActor(path, pathImages, pathClick, player));
		}
		return pathActors;
	}
	
	private static DiceActor getDiceActorFor(Dice dice) {
		if (dice == null) {
			return null;
		}
		
		AtlasRegion[] diceAnim = new AtlasRegion[14];
		for(int k = 0; k < 14; k++)
		{
			diceAnim[k] = atlas.findRegion("game-screen/dice/Dice"+k);
		}
		Animation animation = new Animation(0.05f, diceAnim);

		
		return new DiceActor(dice, animation, new TextureRegionDrawable(animation.getKeyFrame(0)), Scaling.none);
	}
}
