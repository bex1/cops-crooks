package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.actors.PathActor;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.IWalkableTile;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class GameScreen extends AbstractScreen implements PropertyChangeListener{

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private final GameModel model;
	private TiledMap mapToRender;
	private TiledMapTileLayer gameBackground; //kan heta layertorender

	private int mapWidth, mapHeight;

	private List<Actor> actors;

	public GameScreen(CopsAndCrooks game, GameModel gameModel,
			TiledMap tiledmap, TiledMapTileLayer backgroundLayer,
			List<Actor> actors) {
		super(game, backgroundLayer.getWidth() * backgroundLayer.getTileWidth(), 
				backgroundLayer.getHeight() * backgroundLayer.getTileHeight());

		model = gameModel;
		model.addObserver(this);
		mapToRender = tiledmap;
		gameBackground = backgroundLayer;
		this.actors = actors;
		mapWidth = (int) (gameBackground.getWidth() * gameBackground.getTileWidth());
		mapHeight = (int) (gameBackground.getHeight() * gameBackground.getTileHeight());
		for(Player player : model.getPlayers()){
			player.addObserver(this);
		}
		for (Actor actor : actors) {
			stage.addActor(actor);
		}
	}

	@Override
	public void render(float delta){
		
		stage.setCamera(camera);
		renderer.setView(camera);
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(gameBackground);
		renderer.getSpriteBatch().end();
		getTable().setBounds(camera.position.x- camera.viewportWidth/2, camera.position.y- camera.viewportHeight/2, 
				Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
		super.render(delta);
		
		
	}

	@Override
	public void show(){
		super.show();
		model.startGame();
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new OrthographicCamera(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
		GestureDetector gestureDetector = new GestureDetector(gestureListener);
		
		
		camera.position.set(mapWidth/2, mapHeight/2, 0);
		
		// Allows input via stage and gestures
		InputMultiplexer inputMulti = new InputMultiplexer(gestureDetector, stage);
		Gdx.input.setInputProcessor(inputMulti);
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

			if(toX> getCameraBoundryRight())
				toX =  getCameraBoundryRight();
			if(toX < getCameraBoundryLeft())
				toX = getCameraBoundryLeft();
			if(toY > getCameraBoundryUp())
				toY = getCameraBoundryUp();
			if(toY < getCameraBoundryDown())
				toY = getCameraBoundryDown();
			camera.position.x = toX;
			camera.position.y = toY;

			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			float ratio = initialDistance / distance;
			float zoom = initialScale * ratio;
			if(zoom < 2.2f  && zoom > 0.6f) {

				camera.zoom = zoom;
				// Keep within map
				if(camera.position.x > getCameraBoundryRight())
					camera.position.x = getCameraBoundryRight();
				if(camera.position.x < getCameraBoundryLeft())
					camera.position.x = getCameraBoundryLeft();
				if(camera.position.y > getCameraBoundryUp())
					camera.position.y = getCameraBoundryUp();
				if(camera.position.y < getCameraBoundryDown())
					camera.position.y = getCameraBoundryDown();
			}
			return false;
		}

	};

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getSource() != null){
			String property = evt.getPropertyName();
			//check if the player has made a move otherwise he rolls a dice or travels the tramstop.
			if(property.equals(GameModel.PROPERTY_NEW_TURN)){
				
				final Table table = super.getTable();
				
				table.add(model.getCurrentPlayer().getName() + " it's your turn\nplease roll the dice").spaceBottom(50);
		        table.row();
				
				// register the button "roll dice"
				final TextButton rollTheDiceButton = new TextButton("Roll the dice", getSkin());
				rollTheDiceButton.addListener(new ClickListener() {
				 @Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					 super.touchUp(event, x, y, pointer, button);
				     	// TODO click sound
					 	// TODO roll the die
					 	model.getCurrentPlayer().rollDice();
					 	table.clear();
				     }
				} );
				table.add(rollTheDiceButton).size(350, 60).uniform().spaceBottom(10);
				table.row();
				//TODO if the player is standing at a tramstop
				if(model.getCurrentPlayer().isAnyPawnOnTramstop()){
					// register the button "go by tram"
					final TextButton goByTramButton = new TextButton("Go by tram", getSkin());
					goByTramButton.addListener(new ClickListener() {
					 @Override
					public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
						 super.touchUp(event, x, y, pointer, button);
					     	// TODO click sound
						 	// TODO go by tram
					     }
					} );
					table.add(goByTramButton).size(350, 60).uniform().spaceBottom(10);
					table.row();
				}
			}else if(property.equals(Player.PROPERTY_DICE_RESULT)){ 
				//TODO show the results
				getTable().clear();
				if(model.getCurrentPlayer().getPlayerRole().equals(Role.Police)){
					
				}
			}else if (property.equals(Player.PROPERTY_POSSIBLE_PATHS)){
				if(model.getCurrentPlayer().getPlayerRole() == Role.Crook){
					try{
						List<PathActor> tmp = GameFactory.getPathActorsFor((List<TilePath>)evt.getNewValue(), model.getCurrentPlayer());
						for(PathActor pathActor: tmp){
							stage.addActor(pathActor);
						}
					}catch(NullPointerException e){
						model.nextPlayer();
					}
				}else if(model.getCurrentPlayer().getPlayerRole() == Role.Police){
					try{
						List<PathActor> tmp = GameFactory.getPathActorsFor((List<TilePath>)evt.getNewValue(), model.getCurrentPlayer());
						for(PathActor pathActor: tmp){
							stage.addActor(pathActor);
						}
					}catch(NullPointerException e){}
				}
			}else if(property.equals(Player.PROPERTY_CHOOSEN_PAWN)){
				if(model.getCurrentPlayer().getPlayerRole() == Role.Police){
					model.getCurrentPlayer().updatePossiblePaths();
				}
			}
		}
	}



}
