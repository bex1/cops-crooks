package com.dat255.project.android.copsandcrooks.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Direction;
import com.dat255.project.android.copsandcrooks.domainmodel.IMediator;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.utils.IObservable;
import com.dat255.project.android.copsandcrooks.utils.Values;
import com.dat255.project.android.copsandcrooks.utils.Point;
/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class MovableActor extends Image implements PropertyChangeListener {
	private final IMovable pawn;
	private final EnumMap<Animations, Animation> animations;
	private final TextureRegionDrawable currentDrawable;
	private float animTimer;
	private Animations currentAnimation;
	private IMediator mediator;
	
	public enum Animations{
		IDLE_ANIM,
		MOVE_NORTH_ANIM,
		MOVE_EAST_ANIM,
		MOVE_WEST_ANIM,
		MOVE_SOUTH_ANIM,
	}

	public MovableActor(final TextureRegionDrawable drawable, final Scaling scaling, final IMovable pawn, 
			final EnumMap<Animations, Animation> animations, final IMediator mediator){
		super(drawable, scaling);
		this.mediator = mediator;
		this.pawn = pawn;
		this.animations = animations;
		currentDrawable = drawable;
		currentAnimation = Animations.IDLE_ANIM;
		pawn.addObserver(this);
		
		this.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO select ?
				mediator.changePawn(pawn);
				return super.touchDown(event, x, y, pointer, button);
			}
		});


	}
	@Override
	public void act(float delta){
		this.pawn.update(delta);
		animTimer += delta;
		currentDrawable.setRegion(animations.get(currentAnimation).getKeyFrame(animTimer, true));
		super.act(delta);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == this.pawn) {
			String property = evt.getPropertyName();
			if (property == IMovable.PROPERTY_NEXT_TILE) {
				animateWalk();
			} else if (property == IMovable.PROPERTY_CURRENT_TILE) {
				if (!pawn.isMoving()) {
					moveDirectly();
				}
			} else if (property == IMovable.PROPERTY_IS_IN_POLICE_HOUSE) {
				// use fade out fade in 
			} 
		}

	}
	private void moveDirectly() {
		// Make sure we move into idle
		currentAnimation = Animations.IDLE_ANIM;
		animTimer = 0;

		// Just set the position according to mocel
		Point currentPos = pawn.getCurrentTile().getPosition();
		this.setPosition(currentPos.x *60 - 20, currentPos.y * 60);
	}
	private void animateWalk() {
		if (pawn.isMoving()) {
			// Animate according to direction
			Direction direction = pawn.getDirection();
			switch (direction) {
			case EAST:
				currentAnimation = Animations.MOVE_EAST_ANIM;
				break;
			case NORTH:
				currentAnimation = Animations.MOVE_NORTH_ANIM;
				break;
			case SOUTH:
				currentAnimation = Animations.MOVE_SOUTH_ANIM;
				break;
			case WEST:
				currentAnimation = Animations.MOVE_WEST_ANIM;
				break;
			}

			// Check where we will walk
			Point pawnNextPosition = pawn.getNextTile().getPosition();

			// Add move action
			this.addAction(moveTo(pawnNextPosition.x *60 - 20, pawnNextPosition.y * 60, Values.PAWN_MOVE_DELAY, Interpolation.linear));
		}
	}
}
