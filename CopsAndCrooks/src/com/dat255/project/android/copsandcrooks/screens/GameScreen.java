package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;

public class GameScreen extends AbstractScreen{

	OrthogonalTiledMapRenderer renderer;
	OrthographicCamera camera;
	GameModel model;
	TiledMap mapToRender;
	TiledMapTileLayer gameBackground; //kan heta layertorender
	
	public GameScreen(CopsAndCrooks game, GameModel gameModel, TiledMap tiledmap, TiledMapTileLayer backgroundLayer) {
		super(game);
		
		model =gameModel;
		mapToRender = tiledmap;
		gameBackground = backgroundLayer;
	}
	
	@Override
	public void render(float delta){
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			camera.translate(new Vector2(0 , 20));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			camera.translate(new Vector2(-20 , 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			camera.translate(new Vector2(20 , 0));
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			camera.translate(new Vector2(0 , -20));
		}
		
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.zoom = 0.1f+camera.zoom;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			camera.zoom = 0.1f-camera.zoom;
		}
		System.out.print(camera.position);
		camera.update();
		
		renderer.setView(camera);
		renderer.getSpriteBatch().begin();
		renderer.renderTileLayer(gameBackground);
		renderer.getSpriteBatch().end();
	}
	
	@Override
	public void resize(int w, int h){
		camera.viewportWidth = w;
		camera.viewportHeight = h;
	}
	
	@Override
	public void show(){
		renderer = new OrthogonalTiledMapRenderer(mapToRender);
		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
	}
	
	@Override
	public void hide(){
		dispose();
	}
	
	@Override
	public void dispose(){
		renderer.dispose();
		mapToRender.dispose();
	}

}
