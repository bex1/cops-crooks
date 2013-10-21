package com.dat255.project.android.copsandcrooks.actors;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.IWalkableTile;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.MetroStopTile;

/**
 * A visual actor for metro lines in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class MetroLineActor extends Group {
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
		this.thisActor = this;

		int i = 0;
		for (final Image img : pathImages) {
			this.addActor(img);
			IWalkableTile tile = path.getTile(i);
			if (tile instanceof MetroStopTile) {
				final MetroStopTile metroStop = (MetroStopTile)tile;
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
									thisActor.addAction(Actions.sequence(Actions.removeActor(), Actions.delay(0.1f), Actions.removeActor()));
								}
							}
						}
					}
				});
			}
			i++;
		}
	}
}
