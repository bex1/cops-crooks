package com.dat255.project.android.copsandcrooks.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.Direction;
import com.dat255.project.android.copsandcrooks.domainmodel.Mediator;
import com.dat255.project.android.copsandcrooks.domainmodel.TilePath;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class CrookActor extends Image implements PropertyChangeListener {

	private final Crook crook;
	private final EnumMap<CrookAnimations, Animation> animations;
	private final TextureRegionDrawable currentDrawable;
	private float animTimer;
	private CrookAnimations currentAnimation;

	public enum CrookAnimations {
		IDLE_ANIM,
		WALK_EAST_ANIM,
		WALK_SOUTH_ANIM,
		WALK_WEST_ANIM,
		WALK_NORTH_ANIM,
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
				// TODO select ?
				return super.touchDown(event, x, y, pointer, button);
			}
		} );
		
		//test
		crook.setCurrentTile(new RoadTile(new Point(0,2), new Mediator()));
		//test end
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
			String property = evt.getPropertyName();
			if (property == Crook.PROPERTY_NEXT_TILE) {
				animateWalk();
			} else if (property == Crook.PROPERTY_CURRENT_TILE) {
				if (!crook.isMoving()) {
					moveDirectly();
				}
			} else if (property == Crook.PROPERTY_IS_IN_POLICE_HOUSE) {
				// use fade out fade in 
			} 
		}
	}
	
	private void moveDirectly() {
		// Make sure we move into idle
		currentAnimation = CrookAnimations.IDLE_ANIM;
		animTimer = 0;
		
		// Just set the position according to mocel
		Point currentPos = crook.getCurrentTile().getPosition();
		this.setPosition(currentPos.x *60 - 20, currentPos.y * 60);
	}

	private void animateWalk() {
		if (crook.isMoving()) {
			// Animate according to direction
			Direction direction = crook.getDirection();
			switch (direction) {
			case EAST:
				currentAnimation = CrookAnimations.WALK_EAST_ANIM;
				break;
			case NORTH:
				currentAnimation = CrookAnimations.WALK_NORTH_ANIM;
				break;
			case SOUTH:
				currentAnimation = CrookAnimations.WALK_SOUTH_ANIM;
				break;
			case WEST:
				currentAnimation = CrookAnimations.WALK_WEST_ANIM;
				break;
			}
			
			// Check where we will walk
			Point crookNextPosition = crook.getNextTile().getPosition();
			
			// Add move action
			this.addAction(moveTo(crookNextPosition.x *60 - 20, crookNextPosition.y * 60, Values.PAWN_MOVE_DELAY, Interpolation.linear));
		}
	}
}
