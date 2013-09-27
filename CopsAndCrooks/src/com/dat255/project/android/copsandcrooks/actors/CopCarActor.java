package com.dat255.project.android.copsandcrooks.actors;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.CopCar;

public class CopCarActor extends MovableActor{

	public CopCarActor(final TextureRegionDrawable drawable, final Scaling scaling, final CopCar pawn, 
			final EnumMap<Animations, Animation> animations){
		super(drawable, scaling, pawn, animations);
		this.addListener(new ClickListener(){
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO select ?
				pawn.gotSelected();
				return super.touchDown(event, x, y, pointer, button);
			}
		});
	}

}
