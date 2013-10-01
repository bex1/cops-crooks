package com.dat255.project.android.copsandcrooks.actors;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.TramStopTile;

public class MetroLineActor extends Group {
	private final Timer removeTimer;
	
	private final MetroLineActor thisActor;

	/**
	 * Instantiates a visual PathActor along the specified path.
	 * 
	 * @param path The model of the path.
	 * @param pathImages The images for the path.
	 * @param pathImageClicked The clicked image of the path.
	 * @param player The player who can interact with the paths.
	 */
	public MetroLineActor(final TilePath path, final List<Image> pathImages, final Image pathImageClicked, final IPlayer player) {
		if (path.getPathLength() != pathImages.size()) {
			throw new IllegalArgumentException("The tilepath size and image list size must match");
		}
		this.removeTimer = new Timer();
		this.thisActor = this;

		int i = 0;
		for (final Image img : pathImages) {
			this.addActor(img);
			IWalkableTile tile = path.getTile(i);
			if (tile instanceof TramStopTile) {
				final TramStopTile metroStop = (TramStopTile)tile;
				img.addListener(new ClickListener() {
					
					@Override
					public boolean touchDown(InputEvent event, float x, float y,
							int pointer, int button) {
						pathImageClicked.setPosition(img.getX(), img.getY());
						img.setDrawable(pathImageClicked.getDrawable());
						return super.touchDown(event, x, y, pointer, button);
					}

					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						player.chooseMetroStop(metroStop);

						final Stage stage = getStage();
						// Finds all other PathActors in stage and remove them.
						if (stage != null) {
							for (Actor actor : stage.getActors()) {
								if (actor instanceof MetroLineActor) {
									MetroLineActor metroActor = (MetroLineActor)actor;
									metroActor.clear();
									thisActor.addAction((Actions.removeActor()));
								}
							}
						}
					}
				});
			}
		}
		i++;
	}
	
	@Override
	public void clear() {
		super.clear();
		
		// Can't remove the PathActors in stage before the pathActors has cleared all children
		// We solve this by scheduling a task that will do this in 0.5 seconds.
		removeTimer.scheduleTask(new RemoveTask(), 0.1f);
		removeTimer.start();
	}
	
	private class RemoveTask extends Task {
		@Override
		public void run () {
			thisActor.remove();
			removeTimer.stop();
			cancel();
		}
	}
}
