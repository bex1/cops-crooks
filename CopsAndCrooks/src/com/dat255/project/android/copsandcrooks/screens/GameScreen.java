package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.dat255.project.andriod.copsandcrooks.map.MapModel;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;

public class GameScreen extends AbstractScreen{

	MapModel map = new MapModel();
	OrthogonalTiledMapRenderer otmr;
	OrthographicCamera camera;
	
	public GameScreen(CopsAndCrooks game) {
		super(game);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(float delta){
		Gdx.gl20.glClearColor(0, 0, 0, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			camera.translate(new Vector2(0 , 20));
		}if(Gdx.input.isKeyPressed(Input.Keys.A)){
			camera.zoom = 0.1f+camera.zoom;
		}
		System.out.print(camera.position);
		camera.update();
		
		otmr.setView(camera);
		otmr.getSpriteBatch().begin();
		otmr.renderTileLayer(map.getBackground());
		otmr.getSpriteBatch().end();
	}
	
	@Override
	public void resize(int w, int h){
		camera.viewportWidth = w;
		camera.viewportHeight = h;
	}
	
	@Override
	public void show(){
		otmr = new OrthogonalTiledMapRenderer(map.getMap());
		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
	}
	
	@Override
	public void hide(){
		dispose();
	}
	
	@Override
	public void dispose(){
		otmr.dispose();
		map.getMap().dispose();
	}

}
