package com.dat255.project.android.copsandcrooks.actors;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.tiles.RoadTile;
import com.dat255.project.android.copsandcrooks.utils.Constants;

public class CrookActor extends Image implements PropertyChangeListener {
	
	private Crook crook;
	private Map<String, Animation> animations;
	private TextureRegionDrawable currentDrawable;
	private float idleTimer;
	public static final String IDLE_ANIM = "IdleAnim";
	
	/** Creates a crook aligned center.
	 * @param drawable May be null. */
	public CrookActor(TextureRegionDrawable drawable, Scaling scaling, Crook crook, Map<String, Animation> animations) {
		super(drawable, scaling);
		this.crook = crook;
		this.animations = animations;
		currentDrawable = drawable;
		crook.addObserver(this);
	}
	
	@Override
	public void act(float delta) {
		crook.update(delta);
		idleTimer += delta;
		currentDrawable.setRegion(animations.get(IDLE_ANIM).getKeyFrame(idleTimer, true));
		// For testing, moves the guy
		if(Gdx.input.isKeyPressed(Input.Keys.C)){
			crook.setCurrentTile((new RoadTile(new Point(100, 0))));
		}
		super.act(delta);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == crook) {
			switch (evt.getPropertyName()) {
			case Crook.PROPERTY_CURRENT_TILE:
				Point crookTilePosition = crook.getCurrentTile().getPosition();
				this.addAction(moveTo(crookTilePosition.x, crookTilePosition.y, Constants.PAWN_MOVE_DELAY, Interpolation.linear));
				break;
			case Crook.PROPERTY_IS_IN_POLICE_HOUSE:
				// not sure if needed yet
				break;
			}
		}
	}
}
