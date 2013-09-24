package com.dat255.project.android.copsandcrooks.actors;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class PathActor extends Actor {
	private final List<Image> pathImages;
	private final Image pathEnd;
	private final TilePath tilePath;
	
	public PathActor(final TilePath path, final Player player) {
		this.tilePath = path;
		pathImages = new ArrayList<Image>();
		
		for (int i = 0; i < path.getPathSize() - 1; i++) {
			// TODO add drawable....
			Image pathImage = new Image();
			Point pathTilePos = path.getTile(i).getPosition();
			pathImage.setPosition(pathTilePos.x * Values.TILE_WIDTH, 
								  pathTilePos.y * Values.TILE_HEIGTH);
			pathImages.add(pathImage);
		}
		
		// TODO add end drawable
		pathEnd = new Image();
		Point pathEndPos = path.getTile(path.getPathSize()-1).getPosition();
		pathEnd.setPosition(pathEndPos.x * Values.TILE_WIDTH, 
							pathEndPos.y * Values.TILE_HEIGTH);
		
		final PathActor thisActor = this;
		
		pathEnd.addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Show some visual click change on the pathEnd
				
				
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				player.choosePath(tilePath);
				
				// Remove everything from stage.
				for (Image pathImage : pathImages) {
					pathImage.remove();
				}
				pathEnd.remove();
				
				thisActor.remove();
			}
		});
	}
	
	@Override
	protected void setStage(Stage stage) {
		super.setStage(stage);

		if (stage != null) {
			// Also add the path images to stage
			for (Image pathImage : pathImages) {
				stage.addActor(pathImage);
			}
			stage.addActor(pathEnd);
		}
	}
}
