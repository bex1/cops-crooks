package com.dat255.project.android.copsandcrooks.actors;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;

/**
 * A visual actor for paths in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class PathActor extends Group {
	private final TilePath tilePath;

	private final PathActor thisActor;
	
	/**
	 * Instantiates a visual PathActor along the specified path.
	 * 
	 * @param path The model of the path.
	 * @param pathImages The images for the path, excluding the end image.
	 * @param pathEndImage The end image of the path.
	 * @param pathEndImageClicked The clicked end image of the path.
	 * @param player The player who can interact with the paths.
	 */
	public PathActor(final TilePath path, final List<Image> pathImages, final Image pathEndImage, final Image pathEndImageClicked, final IPlayer player) {
		this.tilePath = path;
		this.thisActor = this;
		
		this.addActor(pathEndImage);
		for (Image img : pathImages) {
			this.addActor(img);
			img.setTouchable(Touchable.disabled);
		}

		ClickListener click = new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
									 int pointer, int button) {
				pathEndImage.setDrawable(pathEndImageClicked.getDrawable());
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
							PathActor pathActor = (PathActor) actor;
							pathActor.clear();
							thisActor.addAction(Actions.sequence(Actions.removeActor(), Actions.delay(0.1f), Actions.removeActor()));
						}
					}
				}
			}
		};
		pathEndImage.addListener(click);
	}

}


