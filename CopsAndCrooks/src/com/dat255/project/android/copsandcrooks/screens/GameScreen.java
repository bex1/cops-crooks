package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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
		
		camera.position.set(AbstractScreen.GAME_VIEWPORT_WIDTH, AbstractScreen.GAME_VIEWPORT_HEIGHT, 0);
		camera.update();
		
		otmr.setView(camera);
		otmr.getSpriteBatch().begin();
		otmr.renderTileLayer(map.getBackground());
		otmr.getSpriteBatch().end();
	}
	
	@Override
	public void resize(int w, int h){
		camera.viewportWidth = w*2;
		camera.viewportHeight = h*2;
	}
	
	@Override
	public void show(){
		otmr = new OrthogonalTiledMapRenderer(map.getMap());
		camera = new OrthographicCamera();
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
