package com.dat255.project.android.copsandcrooks.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Direction;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IWalkableTile;
import com.dat255.project.android.copsandcrooks.screens.GameCamera;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;
/**
 * This class represents an abstract pawn in the game Cops&Crooks.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class MovableActor extends Image implements PropertyChangeListener {
	private final IMovable pawn;
	private final EnumMap<Animations, Animation> animations;
	protected final TextureRegionDrawable currentDrawable;
	private float animTimer;
	private Animations currentAnimation;
	private GameCamera camera;
	private final MovableActor thisActor;
	
	public enum Animations{
		IDLE_ANIM,
		MOVE_NORTH_ANIM,
		MOVE_EAST_ANIM,
		MOVE_WEST_ANIM,
		MOVE_SOUTH_ANIM,
	}

	public MovableActor(final TextureRegionDrawable drawable, final Scaling scaling, final IMovable pawn, 
			final EnumMap<Animations, Animation> animations){
		super(drawable, scaling);
		this.pawn = pawn;
		this.animations = animations;
		thisActor = this;
		currentDrawable = drawable;
		currentAnimation = Animations.IDLE_ANIM;
		pawn.addObserver(this);
	}
	
	public void setCamera(GameCamera camera) {
		this.camera = camera;
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
			} else if (property == IMovable.PROPERTY_IS_MOVING) {
				if (!pawn.isMoving()) {
					animTimer = 0;
					currentAnimation = Animations.IDLE_ANIM;
				} 
			} else if (property == IMovable.PROPERTY_IS_PLAYING) {
				if (!pawn.isPlaying()) {
					this.addAction(sequence(fadeOut(1f), removeActor()));
				}
			} else if (property == IMovable.PROPERTY_IS_ACTIVE_PAWN) {
				if (pawn.isActivePawn()) {
					Point currentPos = pawn.getCurrentTile().getPosition();
					float x = currentPos.x * Values.TILE_WIDTH - ((this.getWidth() - Values.TILE_WIDTH)/2);
					float y = currentPos.y * Values.TILE_HEIGTH - ((this.getHeight() - Values.TILE_HEIGTH)/2);
					this.addAction(new CameraMove(0, x, y));
				}
			}
		}

	}
	
	public void refresh() {
		moveDirectly();
	}
	
	private void moveDirectly() {
		// Just set the position according to model
		IWalkableTile currTile = pawn.getCurrentTile();
		if (currTile != null) {
			Point currentPos = pawn.getCurrentTile().getPosition();
			float x = currentPos.x * Values.TILE_WIDTH - ((this.getWidth() - Values.TILE_WIDTH)/2);
			float y = currentPos.y * Values.TILE_HEIGTH - ((this.getHeight() - Values.TILE_HEIGTH)/2);
			if (pawn.isActivePawn()) {
				this.addAction(sequence(fadeOut(0.5f), 
						parallel(moveTo(x, y) , new CameraMove(0, x, y)), 
						fadeIn(0.5f)));
			} else {
				this.addAction(sequence(fadeOut(0.5f), 
						moveTo(x, y), 
						fadeIn(0.5f)));
			}
		}
	}

	
	private class CameraMove extends TemporalAction {
		private float startX, startY;
		private float endX, endY;
		
		protected void begin () {
			camera.setCameraPosition(thisActor.getX(), thisActor.getY());
			startX = camera.position.x;
			startY = camera.position.y;
		}
		
		public CameraMove(float duration, float x, float y) {
			this.setDuration(duration);
			this.setInterpolation(Interpolation.linear);
			endX = x;
			endY = y;
		}
		
		@Override
		protected void update(float percent) {
			camera.setCameraPosition(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
		}
	};
	
	
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

			float x = pawnNextPosition.x * Values.TILE_WIDTH - ((this.getWidth() - Values.TILE_WIDTH)/2); 
			float y = pawnNextPosition.y * Values.TILE_HEIGTH - ((this.getHeight() - Values.TILE_HEIGTH)/2);
			// Add move action
			this.addAction(parallel(new CameraMove(Values.PAWN_MOVE_DELAY, x, y), 
									moveTo(x, y, Values.PAWN_MOVE_DELAY, Interpolation.linear)));
		}
	}
}
