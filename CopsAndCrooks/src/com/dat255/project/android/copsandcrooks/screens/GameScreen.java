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
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.MusicManager;
import com.dat255.project.android.copsandcrooks.utils.MusicManager.CopsAndCrooksMusic;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class GameScreen extends AbstractScreen implements PropertyChangeListener{

	private OrthogonalTiledMapRenderer renderer;
	private GameCamera camera;
	private final GameModel model;
	private final TiledMap mapToRender;
	private final Stage hudStage;

	private MoveByDiceOrMetroTable moveByDiceOrMetro;
	private MoveByDiceTable moveByDice;
	private HUDTable hudTable;
	private ReplayTable replayTable;
	private final GameFactory factory;

	private final int mapWidth, mapHeight;

	public GameScreen(Assets assets, final CopsAndCrooks game, final GameModel gameModel,
			final TiledMap tiledmap, final float mapWidth, final float mapHeight,
			final List<Actor> actors, final Stage hudStage, final DiceActor dice) {
		super(assets, game, mapWidth, mapHeight);
		this.factory = GameFactory.getInstance();
		this.model = gameModel;
		this.mapToRender = tiledmap;
		this.hudStage = hudStage;
		this.mapWidth = (int) mapWidth;
		this.mapHeight = (int) mapHeight;


		model.addObserver(this);
		for(IPlayer player : model.getPlayers()){
			player.addObserver(this);
			for (IMovable pawn : player.getPawns()) {
				pawn.addObserver(this);
			}
		}

		for (Actor actor : actors) {
			stage.addActor(actor);
		}

		initGuiElements();
	}

	private void initGuiElements() {
		moveByDice = new MoveByDiceTable(assets, model, hudStage);
		moveByDiceOrMetro = new MoveByDiceOrMetroTable(assets, model);
		hudTable = new HUDTable(assets, model.getPlayerClient(), model, hudStage, model.getPlayers());
		replayTable = new ReplayTable(assets, model);
	}

	@Override
	public void render(float delta){
		model.update(delta);

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
		
		MusicManager.getInstance().play(CopsAndCrooksMusic.GAME);

	}

	@Override
	public void dispose(){
		MusicManager.getInstance().dispose();
		SoundManager.getInstance().dispose();
		renderer.dispose();
		mapToRender.dispose();
		hudStage.dispose();
		//TODO dispose replay so it will finish before we dispose and save them model
		factory.saveModelToFile(model);
		super.dispose();
	}


	@Override
	public void resume() {
		super.resume();
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
		Object source = evt.getSource();
		IPlayer currPlayer = model.getCurrentPlayer();

		// Check source, i.e. Who sent the event?
		if(source == model) {
			if(property == GameModel.PROPERTY_GAMESTATE){
				switch (model.getGameState()) {
				case Playing:
					// New turn -> show buttons where the player can select its next move
					showActButtons();
					break;
				case Replay:
					hudStage.addActor(replayTable);
					break;
				}
			} else if (property == GameModel.PROPERTY_GAME_ENDED) {
				game.setScreen(new ScoreScreen(assets, game, Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, model.getPlayers()));
			}
		} else if (currPlayer == source) {

			Role playerRole = currPlayer.getPlayerRole();
			if (property == IPlayer.PROPERTY_POSSIBLE_PATHS){
				// Show the possible paths for the current player.
				clearVisiblePaths();
				showPossiblePaths(currPlayer);

			}else if(property == IPlayer.PROPERTY_SELECTED_PAWN){
				if(playerRole == Role.Cop){
					clearVisiblePaths();
				}
			}
		}  else if (currPlayer.getPawns().contains(source) && source instanceof Crook) {
			if (property == Crook.PROPERTY_TURNS_IN_PRISON) {
				// Will remove itself
				hudStage.addActor(new IsInPrisonTable(assets, currPlayer, (Crook)source));
			} else if (property == Crook.PROPERTY_TIMES_ARRESTED) {
				// Will remove itself
				hudStage.addActor(new ArrestedTable(assets, currPlayer, (Crook)source));
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
			tmp = factory.getPathActorsFor(player.getPossiblePaths(), player);
		} else if (player.isGoingByMetro()) {
			tmp = factory.getMetroActorsFor(player.getPossiblePaths(), player);
		}
		if (tmp != null) {
			for(Actor pathActor: tmp){
				stage.addActor(pathActor);
			}
		}
	}
}