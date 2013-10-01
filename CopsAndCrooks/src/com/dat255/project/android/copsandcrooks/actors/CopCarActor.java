package com.dat255.project.android.copsandcrooks.actors;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.ISelectablePawn;

public class CopCarActor extends CopActor{

	public CopCarActor(final TextureRegionDrawable drawable, final Scaling scaling, final ISelectablePawn pawn, 
			final EnumMap<Animations, Animation> animations){
		super(drawable, scaling, pawn, animations);
	}

}
