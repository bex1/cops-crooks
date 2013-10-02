package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.DiceActor;
import com.dat255.project.android.copsandcrooks.actors.MovableActor;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class GameScreen extends AbstractScreen implements PropertyChangeListener{

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private final GameModel model;
	private final TiledMap mapToRender;
	private final Stage hudStage;

	private MoveByDiceOrMetroTable moveByDiceOrMetro;
	private MoveByDiceTable moveByDice;
	private HUDTable hudTable;

	private final int mapWidth, mapHeight;

	public GameScreen(final CopsAndCrooks game, final GameModel gameModel,
			final TiledMap tiledmap, final float mapWidth, final float mapHeight,
			final List<Actor> actors, final DiceActor dice) {
		super(game, mapWidth, mapHeight);


		this.model = gameModel;
		this.mapToRender = tiledmap;
		
		this.mapWidth = (int) mapWidth;
		this.mapHeight = (int) mapHeight;

		hudStage = new Stage(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true);
		hudStage.addActor(dice);
		
		
		model.addObserver(this);
		for(IPlayer player : model.getPlayers()){
			player.addObserver(this);
		}

		for (Actor actor : actors) {
			stage.addActor(actor);
		}

		initGuiElements();
	}

	private void initGuiElements() {
		moveByDice = new MoveByDiceTable(model);
		moveByDiceOrMetro = new MoveByDiceOrMetroTable(model);
		hudTable = new HUDTable(model.getPlayerClient());
	}

	@Override
	public void render(float delta){

		stage.setCamera(camera);
		renderer.setView(camera);
		renderer.render();
		
		super.render(delta);

		hudStage.act(delta);
		hudStage.draw();
	}

	@Override
	public void show(){
		super.show();
		// Show actors at right start pos, ie sync with model
		for (Actor actor : stage.getActors()) {
			if (actor instanceof MovableActor) {
				((MovableActor)actor).refresh();
			}
		}
		
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new OrthographicCamera(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		camera.position.set(mapWidth/2, mapHeight/2, 0);
		
		hudStage.addActor(hudTable);

		// Allows input via stage and gestures
		InputMultiplexer inputMulti = new InputMultiplexer(hudStage, gestureDetector, stage);
		Gdx.input.setInputProcessor(inputMulti);
		model.startGame();
	}

	@Override
	public void dispose(){
		renderer.dispose();
		mapToRender.dispose();
		super.dispose();
	}

	private float getCameraBoundryRight() {
		return  mapWidth - camera.viewportWidth*camera.zoom/2;
	}

	private float getCameraBoundryLeft() {
		return camera.viewportWidth*camera.zoom/2;
	}

	private float getCameraBoundryDown() {
		return camera.viewportHeight*camera.zoom/2;
	}

	private float getCameraBoundryUp() {
		return mapHeight - camera.viewportHeight*camera.zoom/2;
	}

	private GestureListener gestureListener = new GestureDetector.GestureAdapter() {

		private float initialScale = 1;

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			initialScale = camera.zoom;
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			float moveX = -deltaX * camera.zoom;
			float toX = camera.position.x + moveX;

			float moveY = deltaY * camera.zoom;
			float toY = camera.position.y + moveY;

			setCameraPosition(toX, toY);

			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			float ratio = initialDistance / distance;
			float zoom = initialScale * ratio;
			if(zoom < 2.2f  && zoom > 0.6f) {
				camera.zoom = zoom;
				// Keep within map
				setCameraPosition(camera.position.x, camera.position.y);
			}

			return false;
		}

	};
	
	private void setCameraPosition(float x, float y){
		float tmpX = x, tmpY = y;
		// Keeps the camera within the map
		if(x > getCameraBoundryRight())
			tmpX = getCameraBoundryRight();
		if(x < getCameraBoundryLeft())
			tmpX = getCameraBoundryLeft();
		if(y > getCameraBoundryUp())
			tmpY = getCameraBoundryUp();
		if(y < getCameraBoundryDown())
			tmpY = getCameraBoundryDown();
		camera.position.set(tmpX, tmpY, 0);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();

		// Check source, i.e. Who sent the event?
		if(evt.getSource() == model) {
			if(property == GameModel.PROPERTY_CURRENT_PLAYER){
				Point playersPoint = model.getCurrentPlayer().getCurrentPawn().getCurrentTile().getPosition();
				setCameraPosition(playersPoint.x* Values.TILE_WIDTH, playersPoint.y*Values.TILE_HEIGTH);
				// New turn -> show buttons where the player can select its next move
				showActButtons();
			}
		} else if (model.getCurrentPlayer() == evt.getSource()) {

			// Extract relevant data
			IPlayer currPlayer = model.getCurrentPlayer();
			Role playerRole = currPlayer.getPlayerRole();
			if(property == Player.PROPERTY_DICE_RESULT){ 
				//TODO show the results
				if(playerRole == Role.Cop){

				}
			} else if (property == Player.PROPERTY_POSSIBLE_PATHS){
				// Show the possible paths for the current player.
				clearVisiblePaths();
				showPossiblePaths(currPlayer);

			}else if(property == Player.PROPERTY_CHOOSEN_PAWN){
				if(playerRole == Role.Cop){
					clearVisiblePaths();
					currPlayer.updatePossiblePaths();
					Point currentPoint = currPlayer.getCurrentPawn().getCurrentTile().getPosition();
					setCameraPosition(currentPoint.x*Values.TILE_WIDTH, currentPoint.y*Values.TILE_HEIGTH);
				}
			}
		}
	}

	private void clearVisiblePaths() {
		// Finds all other PathActors in stage and remove them.
		if (stage != null) {
			for (Actor actor : stage.getActors()) {
				if (actor instanceof PathActor) {
					PathActor pathActor = (PathActor)actor;
					pathActor.clear();
				}
			}
		}
	}

	private void showActButtons() {
		if(model.getCurrentPlayer().isAnyWalkingPawnOnMetro()){
			hudStage.addActor(moveByDiceOrMetro);
		}else {
			hudStage.addActor(moveByDice);
		}
	}

	private void showPossiblePaths(IPlayer player) {
		List<? extends Actor> tmp = null;
		if (player.isGoingByDice()) {
			tmp = GameFactory.getPathActorsFor(player.getPossiblePaths(), player);
		} else if (player.isGoingByMetro()) {
			tmp = GameFactory.getMetroActorsFor(player.getPossiblePaths(), player);
		}
		if (tmp != null) {
			for(Actor pathActor: tmp){
				stage.addActor(pathActor);
			}
		}
	}
}