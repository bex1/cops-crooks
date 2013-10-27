package com.dat255.project.android.copsandcrooks.view;

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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.model.Direction;
import com.dat255.project.android.copsandcrooks.model.IPawn;
import com.dat255.project.android.copsandcrooks.model.IWalkableTile;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A visual actor for pawns in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * A pawn actor demands camera focus when its model is selected and paints a selection background behind it.
 * 
 * It uses 5 animations, one for idle and 4 for walking in different directions.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class PawnActor extends Image implements PropertyChangeListener {
	protected final IPawn pawn;
	private final EnumMap<Animations, Animation> animations;
	protected final TextureRegionDrawable currentDrawable;
	protected float animTimer;
	private Animations currentAnimation;
	private GameCamera camera;
	private final PawnActor thisActor;
	private Image selectedBackground;
	protected final Assets assets;
	
	public enum Animations{
		IDLE_ANIM,
		MOVE_NORTH_ANIM,
		MOVE_EAST_ANIM,
		MOVE_WEST_ANIM,
		MOVE_SOUTH_ANIM,
	}

	/**
	 * Inits a pawn actor.
	 * 
	 * @param assets The assets for cops and crooks
	 * @param drawable The first drawable of this actor
	 * @param scaling The scaling used for the drawable
	 * @param pawn The selectable pawn that this actor represents
	 * @param animations The animations for the actor.
	 */
	public PawnActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling, final IPawn pawn, 
			final EnumMap<Animations, Animation> animations){
		super(drawable, scaling);
		this.pawn = pawn;
		this.animations = animations;
		this.assets = assets;
		thisActor = this;
		currentDrawable = drawable;
		// start in idle
		currentAnimation = Animations.IDLE_ANIM;
		pawn.addObserver(this);
		initBackgrounds();
	}
	
	protected void initBackgrounds() {
		TextureAtlas atlas = assets.getAtlas();
		// Get selected background image
		TextureRegionDrawable selected = new TextureRegionDrawable(atlas.findRegion("game-screen/status/Selected"));
		selectedBackground = new Image(selected, Scaling.none);
		selectedBackground.setSize(getWidth(), getHeight());
		selectedBackground.getColor().a = 0.6f;
	}

	/**
	 * Sets the camera that the pawn actor can use to request focus.
	 * @param camera The camera of the game.
	 */
	public void setCamera(GameCamera camera) {
		this.camera = camera;
	}
	
	@Override
	public void act(float delta){
		// Update the state of the pawn.
		this.pawn.update(delta);
		animTimer += delta;
		// Switch animations according to preset interval
		currentDrawable.setRegion(animations.get(currentAnimation).getKeyFrame(animTimer, true));
		super.act(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (pawn.isActivePawn()) {
			// Draw selected background
			selectedBackground.setPosition(getX(), getY());
			selectedBackground.draw(batch, parentAlpha);
		} 
		super.draw(batch, parentAlpha);
	}


	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == this.pawn) {
			String property = evt.getPropertyName();
			if (property == IPawn.PROPERTY_NEXT_TILE) {
				// Next tile changed, ie the pawn is heading in a direction.
				// We animate a walk.
				animateWalk();
			} else if (property == IPawn.PROPERTY_CURRENT_TILE) {
				if (!pawn.isMoving()) {
					// If the state is not moving we just place the pawn directly without animations and delay
					moveDirectly();
				}
			} else if (property == IPawn.PROPERTY_IS_MOVING) {
				if (!pawn.isMoving()) {
					animTimer = 0;
					// Stopped moving, go to idle.
					currentAnimation = Animations.IDLE_ANIM;
				} 
			} else if (property == IPawn.PROPERTY_IS_PLAYING) {
				if (!pawn.isPlaying()) {
					// When the pawn is no longer playing, we fade it out of the game.
					this.addAction(sequence(fadeOut(1f), removeActor()));
				}
			} else if (property == IPawn.PROPERTY_IS_ACTIVE_PAWN) {
				if (pawn.isActivePawn()) {
					// Request camera focus to the pawns position.
					IWalkableTile tile = pawn.getCurrentTile();
					if (tile != null) {
						Point currentPos = tile.getPosition();
						float x = currentPos.x * Values.TILE_WIDTH - ((this.getWidth() - Values.TILE_WIDTH)/2);
						float y = currentPos.y * Values.TILE_HEIGTH - ((this.getHeight() - Values.TILE_HEIGTH)/2);
						this.addAction(new CameraMove(0, x, y));
					}
				} 
			}
		}
	}
	
	/**
	 * Used to refresh the pawn actor so it is placed according to the model.
	 */
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
				// A selected pawn, then we use a small fade animation. And move the camera to the new pos.
				this.addAction(sequence(fadeOut(0.5f), 
						parallel(moveTo(x, y) , new CameraMove(0, x, y)), 
						fadeIn(0.5f)));
			} else {
				// A deselected pawn, just move.
				this.addAction(sequence(fadeOut(0.5f), 
						moveTo(x, y), 
						fadeIn(0.5f)));
			}
		}
	}
	
	/**
	 * Camera move action with a linear interpolation.
	 * 
	 * @author Bex
	 *
	 */
	private class CameraMove extends TemporalAction {
		private float startX, startY;
		private float endX, endY;
		
		/**
		 * Called at start to calculate start pos.
		 */
		protected void begin () {
			camera.setCameraPosition(thisActor.getX(), thisActor.getY());
			startX = camera.position.x;
			startY = camera.position.y;
		}
		
		/**
		 * Ints a camera move.
		 * 
		 * @param duration The duration of the move.
		 * @param x The end center x-coordinate of the camera.
		 * @param y The end center y-coordinate of the camera.
		 */
		public CameraMove(float duration, float x, float y) {
			this.setDuration(duration);
			this.setInterpolation(Interpolation.linear);
			endX = x;
			endY = y;
		}
		
		/**
		 * Called during the move to update the position.
		 */
		@Override
		protected void update(float percent) {
			camera.setCameraPosition(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
		}
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

			float x = pawnNextPosition.x * Values.TILE_WIDTH - ((this.getWidth() - Values.TILE_WIDTH)/2); 
			float y = pawnNextPosition.y * Values.TILE_HEIGTH - ((this.getHeight() - Values.TILE_HEIGTH)/2);
			// Add move action and camera move.
			this.addAction(parallel(new CameraMove(Values.PAWN_MOVE_DELAY, x, y), 
									moveTo(x, y, Values.PAWN_MOVE_DELAY, Interpolation.linear)));
		}
	}
}
