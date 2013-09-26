package com.dat255.project.android.copsandcrooks.actors;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;

/**
 * A visual actor for paths in the game Cops&Crooks.
 * 
 * @author Bex
 */
public class PathActor extends Group {
	private final TilePath tilePath;
	
	/**
	 * Instantiates a visual PathActor along the specified path.
	 * 
	 * @param path The model of the path.
	 * @param pathImages The images for the path, excluding the end image.
	 * @param pathEndImage The end image of the path.
	 * @param player The player who can interact with the paths.
	 */
	public PathActor(final TilePath path, final List<Image> pathImages, final Image pathEndImage, final Player player) {
		this.tilePath = path;
		
		for (Image img : pathImages) {
			this.addActor(img);
		}
		
		final PathActor thisActor = this;
		
		pathEndImage.addListener(new ClickListener() {
			
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				player.choosePath(tilePath);
				
				Stage stage = getStage();
				
				// Fins all other PathActors in stage and remove them.
				if (stage != null) {
					for (Actor actor : stage.getActors()) {
						if (actor instanceof PathActor && actor != thisActor) {
							actor.remove();
						}
					}
				}
				
				// Remove ourself from stage.
				thisActor.remove();
			}
		});
		
		this.addActor(pathEndImage);
	}
}
