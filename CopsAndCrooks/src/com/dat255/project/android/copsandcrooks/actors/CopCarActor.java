package com.dat255.project.android.copsandcrooks.actors;

import java.beans.PropertyChangeEvent;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.IPawn;
import com.dat255.project.android.copsandcrooks.domainmodel.ISelectablePawn;
import com.dat255.project.android.copsandcrooks.screens.Assets;

/**
 * A visual actor for cop cars in the game Cops&Crooks.
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CopCarActor extends CopActor{

	public CopCarActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling, final ISelectablePawn pawn, 
			final EnumMap<Animations, Animation> animations){
		super(assets, drawable, scaling, pawn, animations);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == IPawn.PROPERTY_IS_MOVING) {
			// Will keep its direction if stopped moving. Will not go into idle.
			if (!pawn.isMoving()) {
				animTimer = 0;
			} 
		} else {
			super.propertyChange(evt);
		}
	}
}
