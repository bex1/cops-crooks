package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.DiceActor;
import com.dat255.project.android.copsandcrooks.actors.MetroLineActor;
import com.dat255.project.android.copsandcrooks.actors.MovableActor;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;

public class GameScreen extends AbstractScreen implements PropertyChangeListener{

	private OrthogonalTiledMapRenderer renderer;
	private GameCamera camera;
	private final GameModel model;
	private final TiledMap mapToRender;
	private final Stage hudStage;

	private MoveByDiceOrMetroTable moveByDiceOrMetro;
	private MoveByDiceTable moveByDice;
	private HUDTable hudTable;

	private final int mapWidth, mapHeight;

	public GameScreen(final CopsAndCrooks game, final GameModel gameModel,
			final TiledMap tiledmap, final float mapWidth, final float mapHeight,
			final List<Actor> actors, final Stage hudStage, final DiceActor dice) {
		super(game, mapWidth, mapHeight);

		this.model = gameModel;
		this.mapToRender = tiledmap;
		this.hudStage = hudStage;
		this.mapWidth = (int) mapWidth;
		this.mapHeight = (int) mapHeight;

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
		Table.drawDebug(hudStage);
	}

	@Override
	public void show(){
		super.show();
		
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new GameCamera(mapWidth, mapHeight);
		// Show actors at right start pos, ie sync with model
		for (Actor actor : stage.getActors()) {
			if (actor instanceof MovableActor) {
				MovableActor pawn = (MovableActor)actor;
				pawn.refresh();
				pawn.setCamera(camera);
			}
		}
		
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		
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

			camera.setCameraPosition(toX, toY);

			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			float ratio = initialDistance / distance;
			float zoom = initialScale * ratio;
			if(zoom < 2.2f  && zoom > 0.6f) {
				camera.zoom = zoom;
				// Keep within map
				camera.setCameraPosition(camera.position.x, camera.position.y);
			}

			return false;
		}

	};


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		if (camera != null && Gdx.app.getType() == ApplicationType.Android) {
			// needed on android to relocate camera after pause
			camera.setCameraPosition(camPauseX, camPauseY);
		}
	}

	@Override
	public void pause() {
		super.pause();
		if (camera != null) {
			camPauseX = camera.position.x;
			camPauseY = camera.position.y;
		}
	}
	
	private float camPauseX;
	private float camPauseY;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String property = evt.getPropertyName();

		// Check source, i.e. Who sent the event?
		if(evt.getSource() == model) {
			if(property == GameModel.PROPERTY_CURRENT_PLAYER){
				// New turn -> show buttons where the player can select its next move
				showActButtons();
			} else if (property == GameModel.PROPERTY_GAME_ENDED) {
				Gdx.app.exit();
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

			}else if(property == Player.PROPERTY_SELECTED_PAWN){
				if(playerRole == Role.Cop){
					clearVisiblePaths();
				}
			}
		}
	}

	private void clearVisiblePaths() {
		// Finds all other PathActors in stage and remove them.
		if (stage != null) {
			for (Actor actor : stage.getActors()) {
				if (actor instanceof PathActor || actor instanceof MetroLineActor) {
					actor.addAction(Actions.sequence(Actions.removeActor(), Actions.delay(0.1f), Actions.removeActor()));
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