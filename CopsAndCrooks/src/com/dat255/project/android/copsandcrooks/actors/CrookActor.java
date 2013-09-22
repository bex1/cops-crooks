package com.dat255.project.android.copsandcrooks.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;

public class CrookActor extends Image implements PropertyChangeListener {
	
	private final Crook crook;
	private final EnumMap<CrookAnimations, Animation> animations;
	private final TextureRegionDrawable currentDrawable;
	private float animTimer;
	private CrookAnimations currentAnimation;
	
	public enum CrookAnimations {
		IDLE_ANIM,
		WALK_EAST_ANIM,
	}
	
	/** Creates a crook aligned center.
	 * @param drawable May be null. */
	public CrookActor(final TextureRegionDrawable drawable, final Scaling scaling, final Crook crook, final EnumMap<CrookAnimations, Animation> animations) {
		super(drawable, scaling);
		this.crook = crook;
		this.animations = animations;
		currentDrawable = drawable;
		currentAnimation = CrookAnimations.IDLE_ANIM;
		crook.addObserver(this);
		
		this.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// For testing, moves the guy
				crook.setCurrentTile((new RoadTile(new Point(500, 0))));
				return super.touchDown(event, x, y, pointer, button);
			}
        } );
	}
	
	@Override
	public void act(float delta) {
		crook.update(delta);
		animTimer += delta;
		currentDrawable.setRegion(animations.get(currentAnimation).getKeyFrame(animTimer, true));
		
		super.act(delta);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == crook) {
			switch (evt.getPropertyName()) {
			case Crook.PROPERTY_CURRENT_TILE:
				// test
				currentAnimation = CrookAnimations.WALK_EAST_ANIM;
				animTimer = 0;
				// test end
				Point crookTilePosition = crook.getCurrentTile().getPosition();
				this.addAction(sequence(moveTo(crookTilePosition.x, crookTilePosition.y, 3f, Interpolation.linear), 
						new Action() {
					@Override
					public boolean act(float delta) {
						currentAnimation = CrookAnimations.IDLE_ANIM;
						animTimer = 0;
						return true;
					}
				}));
				break;
			case Crook.PROPERTY_IS_IN_POLICE_HOUSE:
				// use fade out fade in 
				break;
			}
		}
	}
}
