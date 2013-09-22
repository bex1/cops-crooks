package com.dat255.project.android.copsandcrooks.screens;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.utils.Constants;

public class GameScreen extends AbstractScreen{

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private GameModel model;
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
		mapToRender = tiledmap;
		gameBackground = backgroundLayer;
		this.actors = actors;
		mapWidth = (int) (gameBackground.getWidth() * gameBackground.getTileWidth());
		mapHeight = (int) (gameBackground.getHeight() * gameBackground.getTileHeight());
		
		stage = new Stage(mapWidth, mapHeight, true);
		
		for (Actor actor : actors) {
			stage.addActor(actor);
		}
	}
	
	@Override
	public void render(float delta){
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(camera.position.y < getCameraBoundryUp())
				camera.translate(new Vector2(0 , 20));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(camera.position.x > getCameraBoundryLeft())
				camera.translate(new Vector2(-20 , 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(camera.position.x < getCameraBoundryRight())
				camera.translate(new Vector2(20 , 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(camera.position.y > getCameraBoundryDown())
				camera.translate(new Vector2(0 , -20));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			if(camera.zoom < 2.2f ) {
				camera.zoom = 0.1f+camera.zoom;
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
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			if(camera.zoom > .6f)
				camera.zoom = camera.zoom- 0.1f;
		}
		
		
		camera.update();
		renderer.setView(camera);
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(gameBackground);
		renderer.getSpriteBatch().end();
		super.render(delta);
	}
	
	@Override
	public void resize(int w, int h){
		super.resize(w, h);
		camera.viewportWidth = w;
		camera.viewportHeight = h;
	}
	
	@Override
	public void show(){
		super.show();
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new OrthographicCamera();
		camera.position.set(Constants.GAME_VIEWPORT_WIDTH/2, Constants.GAME_VIEWPORT_HEIGHT/2, 0);
		stage.setCamera(camera);
	}
	
	@Override
	public void dispose(){
		super.dispose();
		renderer.dispose();
		mapToRender.dispose();
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
	
}
