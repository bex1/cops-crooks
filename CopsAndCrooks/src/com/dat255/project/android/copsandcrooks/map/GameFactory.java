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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.CopActor;
import com.dat255.project.android.copsandcrooks.actors.CopCarActor;
import com.dat255.project.android.copsandcrooks.actors.CrookActor;
import com.dat255.project.android.copsandcrooks.actors.DiceActor;
import com.dat255.project.android.copsandcrooks.actors.HideoutActor;
import com.dat255.project.android.copsandcrooks.actors.MetroLineActor;
import com.dat255.project.android.copsandcrooks.actors.MovableActor.Animations;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.CopCar;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Dice;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.ModelFactory;
import com.dat255.project.android.copsandcrooks.domainmodel.Officer;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.screens.GameScreen;
import com.dat255.project.android.copsandcrooks.screens.HideoutOptionsTable;
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
	
	public static Screen loadGame(CopsAndCrooks game, Map<String, Role> userInfo){
		// This loads a TMX file
		TiledMap map = new TmxMapLoader().load("map-images/cops-crooks-map-v2.tmx");  
		
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
		
		Stage hudStage = new Stage(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true);

		//Loads a GameModel
		GameModel model = ModelFactory.loadGameModel(mapLayerInteract, userInfo);
		Collection<? extends IPlayer> players = model.getPlayers();
		
		List<Actor> actors = addActor(players);
		for (HideoutTile hideout : model.getHideouts()) {
			actors.add(new HideoutActor(hideout, players, hudStage));
			new HideoutOptionsTable(hideout, hudStage);
		}
		
		return new GameScreen(game, model, map, mapLayerBack.getWidth()*mapLayerBack.getTileWidth(),
				mapLayerBack.getHeight()* mapLayerBack.getTileHeight(), actors, hudStage, getDiceActorFor(model.getDice()));
	}

	public static Screen loadHostedGame(CopsAndCrooks game, GameModel model){
		
		// This loads a TMX file
		TiledMap map = new TmxMapLoader().load("map-images/cops-crooks-map-v2.tmx");  
		
		// Takes out the layer that will contain the background graphics
		TiledMapTileLayer mapLayerBack;
		try {
			mapLayerBack = (TiledMapTileLayer)map.getLayers().get("background");					
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		GameModel newModel;
		try {
			newModel = ModelFactory.loadHostedGameModel(model);
			List<Actor> actors = addActor(newModel.getPlayers());
		
			Stage hudStage = new Stage(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true);
			
			for (HideoutTile hideout : model.getHideouts()) {
				actors.add(new HideoutActor(hideout, newModel.getPlayers(), hudStage));
				new HideoutOptionsTable(hideout, hudStage);
			}
			
			return new GameScreen(game, newModel, map, mapLayerBack.getWidth()*mapLayerBack.getTileWidth(),
					mapLayerBack.getHeight()* mapLayerBack.getTileHeight(), actors, hudStage, getDiceActorFor(model.getDice()));
	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Method to be able to add actors to a collection of players.
	 * @param players - the collection you want to ad actors on.
	 * @return - List of actors tht draws your animation of your players pawn
	 */
	private static List<Actor> addActor(Collection<? extends IPlayer> players){
		List<Actor> actors = new ArrayList<Actor>();
		for(IPlayer player: players){	
			Collection<? extends IMovable> pawns = player.getPawns();
			for(IMovable pawn : pawns){
				if(pawn instanceof Officer){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getOfficerAnimations();
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					actors.add(new CopActor(drawable, Scaling.none, (Officer) pawn, pawnAnimations));
				}else if(pawn instanceof Crook){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getCrookAnimations();
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					actors.add(new CrookActor(drawable, Scaling.none, (Crook) pawn, pawnAnimations));
				}else if(pawn instanceof CopCar){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getCopCarAnimations();
		
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					
					actors.add(new CopCarActor(drawable, Scaling.none, (CopCar) pawn, pawnAnimations));
				}
			}
		}
		
		return actors;
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
