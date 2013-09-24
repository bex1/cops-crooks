package com.dat255.project.android.copsandcrooks.actors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Officer;

public class OfficerActor extends Image implements PropertyChangeListener {
	
	private final Officer officer;
	private final EnumMap<OfficerAnimations, Animation> animations;
	private final TextureRegionDrawable currentDrawable;
	private float animTimer;
	private OfficerAnimations currentAnimation;
	
	public enum OfficerAnimations{
		IDLE_ANIM,
		WALK_NORTH_ANIM,
		WALK_EAST_ANIM,
		WALK_WEST_ANIM,
		WALK_SOUTH_ANIM,
	}
	
	public OfficerActor(final TextureRegionDrawable drawable, final Scaling scaling, final Officer officer, 
			final EnumMap<OfficerAnimations, Animation> animations){
		super(drawable, scaling);
		this.officer = officer;
		this.animations = animations;
		currentDrawable = drawable;
		currentAnimation = OfficerAnimations.IDLE_ANIM;
		officer.addObserver(this);
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
