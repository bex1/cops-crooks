package com.dat255.project.android.copsandcrooks.actors;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.dat255.project.android.copsandcrooks.domainmodel.Player;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;

/**
 * A visual actor for paths in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class PathActor extends Group {
	private final TilePath tilePath;

	private final Player player;
	private final Timer removeTimer;
	
	private final Task removeTask = new Task() {
		@Override
		public void run () {
			Stage stage = getStage();
			if (stage != null) {
				for (Actor actor : stage.getActors()) {
					if (actor instanceof PathActor) {
						actor.remove();
					}
				}
			}
			removeTimer.stop();
		}
	};
	
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
		this.player = player;
		this.removeTimer = new Timer();
		
		this.addActor(pathEndImage);
		for (Image img : pathImages) {
			this.addActor(img);
			img.setTouchable(Touchable.disabled);
		}
		
		pathEndImage.addListener(click);
	}
	
	private ClickListener click = new ClickListener() {
		
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			
			return super.touchDown(event, x, y, pointer, button);
		}

		@Override
		public void touchUp(InputEvent event, float x, float y,
				int pointer, int button) {
			player.choosePath(tilePath);
			
			final Stage stage = getStage();
			// Finds all other PathActors in stage and remove them.
			if (stage != null) {
				for (Actor actor : stage.getActors()) {
					if (actor instanceof PathActor) {
						PathActor pathActor = (PathActor)actor;
						pathActor.clear();
					}
				}
			}
			
			// Can't remove the PathActors in stage before the pathActors has cleared all children
			// We solve this by scheduling a task that will do this in 0.5 seconds.
			removeTimer.scheduleTask(removeTask, 0.5f);
			removeTimer.start();
		}
	};

	
	
}
