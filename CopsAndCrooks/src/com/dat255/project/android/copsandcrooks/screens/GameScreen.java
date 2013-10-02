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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class GameScreen extends AbstractScreen implements PropertyChangeListener{

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private final GameModel model;
	private final TiledMap mapToRender;
	private final Stage hudStage;

	private Table actionsTable;
	private TextButton rollTheDiceButton;
	private TextButton goByTramButton;

	private final int mapWidth, mapHeight;

	public GameScreen(final CopsAndCrooks game, final GameModel gameModel,
			final TiledMap tiledmap, final float mapWidth, final float mapHeight,
			final List<Actor> actors) {
		super(game, mapWidth, mapHeight);

		this.model = gameModel;
		this.mapToRender = tiledmap;
		
		this.mapWidth = (int) mapWidth;
		this.mapHeight = (int) mapHeight;

		hudStage = new Stage(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT, true);

		model.addObserver(this);
		for(Player player : model.getPlayers()){
			player.addObserver(this);
		}

		for (Actor actor : actors) {
			stage.addActor(actor);
		}

		initGuiElements();
	}

	private void initGuiElements() {
		actionsTable = new Table(getSkin());
		actionsTable.setFillParent(true);
		// register the button "roll dice"
		rollTheDiceButton = new TextButton("Roll the dice", getSkin());
		rollTheDiceButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				rollTheDiceButton.getParent().clear();
				model.getCurrentPlayer().rollDice();
				
			}
		});

		// register the button "go by tram"
		goByTramButton = new TextButton("Go by tram", getSkin());
		goByTramButton.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// TODO click sound
				// TODO go by tram
				goByTramButton.getParent().clear();
			}
		} );
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
		
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new OrthographicCamera(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		camera.position.set(mapWidth/2, mapHeight/2, 0);

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
				// New turn -> show buttons where the player can select its next move
				showActButtons();
			}
		} else if (model.getCurrentPlayer() == evt.getSource()) {

			// Extract relevant data
			Player currPlayer = model.getCurrentPlayer();
			Role playerRole = currPlayer.getPlayerRole();

			if(property == Player.PROPERTY_DICE_RESULT){ 
				//TODO show the results
				if(playerRole == Role.Officer){

				}
			} else if (property == Player.PROPERTY_POSSIBLE_PATHS){
				// Show the possible paths for the current player.
				clearVisiblePaths();
				showPossiblePaths(currPlayer);

			}else if(property == Player.PROPERTY_CHOOSEN_PAWN){
				if(playerRole == Role.Officer){
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
		hudStage.addActor(actionsTable);

		Player currentPlayer = model.getCurrentPlayer();

		Point focuspoint= currentPlayer.getCurrentPawn().getCurrentTile().getPosition();
		setCameraPosition(focuspoint.x*Values.TILE_WIDTH, focuspoint.y*Values.TILE_HEIGTH);
		
		actionsTable.add(currentPlayer.getName() + " it's your turn\nplease roll the dice").spaceBottom(50);
		actionsTable.row();

		actionsTable.add(rollTheDiceButton).size(360, 60).uniform().padBottom(10);
		actionsTable.row();

		//TODO if the player is standing at a tramstop
		if(currentPlayer.isAnyWalkingPawnOnTramstop()){
			actionsTable.add(goByTramButton).size(360, 60).uniform().padBottom(10);
			actionsTable.row();
		}
	}

	private void showPossiblePaths(Player player) {
		List<PathActor> tmp = GameFactory.getPathActorsFor(player.getPossiblePaths(), player);
		if (tmp != null) {
			for(PathActor pathActor: tmp){
				stage.addActor(pathActor);
			}
		}
	}
}