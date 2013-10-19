package com.dat255.project.android.copsandcrooks.actors;

import java.beans.PropertyChangeEvent;
import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.ISelectablePawn;
import com.dat255.project.android.copsandcrooks.screens.Assets;

public class CopCarActor extends CopActor{

	public CopCarActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling, final ISelectablePawn pawn, 
			final EnumMap<Animations, Animation> animations){
		super(assets, drawable, scaling, pawn, animations);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == IMovable.PROPERTY_IS_MOVING) {
			// Will keep its direction if stopped moving. Will not go into idle.
			if (!pawn.isMoving()) {
				animTimer = 0;
			} 
		} else {
			super.propertyChange(evt);
		}

	}
	
	

}
