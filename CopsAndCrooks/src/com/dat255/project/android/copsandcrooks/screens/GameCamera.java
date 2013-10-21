package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * The game camera of cops and crooks.
 * 
 * It is an orthographic camera which always makes sure that it stays within the map.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class GameCamera extends OrthographicCamera {
	private final int mapWidth, mapHeight;
	
	GameCamera(int mapWidth, int mapHeight) {
		super(Values.GAME_VIEWPORT_WIDTH, Values.GAME_VIEWPORT_HEIGHT);
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		position.set(mapWidth/2, mapHeight/2, 0);
	}

	public void setCameraPosition(float x, float y){
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
		this.position.set(tmpX, tmpY, 0);
	}
	
	private float getCameraBoundryRight() {
		return  mapWidth - viewportWidth * zoom/2;
	}

	private float getCameraBoundryLeft() {
		return viewportWidth * zoom/2;
	}

	private float getCameraBoundryDown() {
		return viewportHeight * zoom/2;
	}

	private float getCameraBoundryUp() {
		return mapHeight - viewportHeight * zoom/2;
	}
}
