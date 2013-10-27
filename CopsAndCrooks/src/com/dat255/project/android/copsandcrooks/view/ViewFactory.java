package com.dat255.project.android.copsandcrooks.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.model.CopCar;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.Dice;
import com.dat255.project.android.copsandcrooks.model.GameModel;
import com.dat255.project.android.copsandcrooks.model.HideoutTile;
import com.dat255.project.android.copsandcrooks.model.IPawn;
import com.dat255.project.android.copsandcrooks.model.IPlayer;
import com.dat255.project.android.copsandcrooks.model.ModelFactory;
import com.dat255.project.android.copsandcrooks.model.Officer;
import com.dat255.project.android.copsandcrooks.model.TilePath;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;
import com.dat255.project.android.copsandcrooks.view.PawnActor.Animations;

/**
 * Factory used to instantiate visual elements.
 * 
 * The visual elements consist of actors, which are placed on a stage of a screen.
 * 
 * (You need to call init before using methods in GameFactory)
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class ViewFactory {
	private Assets assets;
	private TiledMap map;
	private TiledMapTileLayer mapLayerBack, mapLayerInteract;

	private static ViewFactory instance = null;	
	
	private ViewFactory() {}

	/**
	 * Returns the instance of the factory.
	 * 
	 * @return The instance of the factory.
	 */
	public static ViewFactory getInstance(){
		if(instance == null){
			instance = new ViewFactory();
		}
		return instance;
	}
	
	private void checkAssets() throws NullPointerException{
		if(assets == null)
			throw new NullPointerException("You haven't loaded assets so you cant use any methods");
	}
	
	/**
	 * Inits the factory.
	 * 
	 * Needs to be called before construction with the factory.
	 * 
	 * @param assets ints the game factory.
	 */
	public void init(Assets assets){
		this.assets = assets;	
		readTMXMap();	
	}
	
	private  void readTMXMap(){
		// This loads a TMX file
		map = assets.getMap();
		try { 
			mapLayerBack = (TiledMapTileLayer)map.getLayers().get("background");					
			mapLayerInteract = (TiledMapTileLayer)map.getLayers().get("interact");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Values.TILE_WIDTH = (int) mapLayerBack.getTileWidth();
		Values.TILE_HEIGTH = (int) mapLayerBack.getTileHeight();
	}
	
	/**
	 * Method to be able to add actors to a collection of players.
	 * @param players - the collection you want to ad actors on.
	 * @return - List of actors tht draws your animation of your players pawn
	 */
	private List<Actor> addActor(Collection<? extends IPlayer> players){
		List<Actor> actors = new ArrayList<Actor>();
		for(IPlayer player: players){	
			Collection<? extends IPawn> pawns = player.getPawns();
			for(IPawn pawn : pawns){
				if(pawn instanceof Officer){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getOfficerAnimations();
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					actors.add(new CopActor(assets, drawable, Scaling.none, (Officer) pawn, pawnAnimations));
				}else if(pawn instanceof Crook){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getCrookAnimations();
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					actors.add(new CrookActor(assets, drawable, Scaling.none, (Crook) pawn, pawnAnimations));
				}else if(pawn instanceof CopCar){
					// Get animations
					EnumMap<Animations, Animation> pawnAnimations = getCopCarAnimations();
		
					// Specify the first drawable frame
					TextureRegionDrawable drawable = new TextureRegionDrawable(pawnAnimations.get(Animations.IDLE_ANIM).getKeyFrame(0));
					
					actors.add(new CopCarActor(assets, drawable, Scaling.none, (CopCar) pawn, pawnAnimations));
				}
			}
		}
		
		return actors;
	}
	
	private EnumMap<Animations, Animation> getCopCarAnimations() {
		TextureAtlas atlas = assets.getAtlas();
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

	private EnumMap<Animations, Animation> getCrookAnimations() {
		TextureAtlas atlas = assets.getAtlas();
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
	
	private EnumMap<Animations, Animation> getOfficerAnimations() {
		TextureAtlas atlas = assets.getAtlas();
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
	public List<PathActor> getPathActorsFor(Collection<TilePath> paths, IPlayer player) {
		checkAssets();
		TextureAtlas atlas = assets.getAtlas();
		
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
	public List<MetroLineActor> getMetroActorsFor(Collection<TilePath> paths, IPlayer player) {
		checkAssets();
		TextureAtlas atlas = assets.getAtlas();
		
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
	
	public TiledMapTileLayer getInteract(){
		return mapLayerInteract;
	}
	
	private DiceActor getDiceActorFor(Dice dice, Stage hudStage) {
		TextureAtlas atlas = assets.getAtlas();
		
		if (dice == null) {
			return null;
		}
		
		AtlasRegion[] diceAnimation = new AtlasRegion[14];
		for(int k = 0; k < 14; k++)
		{
			diceAnimation[k] = atlas.findRegion("game-screen/dice/Dice"+k);
		}
		Animation animation = new Animation(0.05f, diceAnimation);

		
		return new DiceActor(assets, dice, animation, new TextureRegionDrawable(animation.getKeyFrame(0)), Scaling.none, hudStage);
	}
	
	/**
	 * 
	 * @param game The model of the game.
	 * @param copsAndCrooks The CopsAndCrooks instance.
	 * @return a game screen.
	 */
	public Screen loadGameScreen(GameModel game, CopsAndCrooks copsAndCrooks) {
		checkAssets();
		
		List<Actor> actors = addActor(game.getPlayers());
		
		Stage hudStage = new Stage(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true);
		
		for (HideoutTile hideout : game.getHideouts()) {
			actors.add(new HideoutActor(assets, hideout, game.getPlayers(), hudStage));
			new HideoutOptionsTable(assets, hideout, hudStage);
		}
		
		ModelFactory.getInstance().saveModelToFile(game);

		return new GameScreen(assets, copsAndCrooks, game, map, mapLayerBack.getWidth()*mapLayerBack.getTileWidth(),
				mapLayerBack.getHeight()* mapLayerBack.getTileHeight(), actors, hudStage, getDiceActorFor(game.getDice(), hudStage));
	}
}
