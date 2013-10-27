package com.dat255.project.android.copsandcrooks.view;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.model.ISelectablePawn;

/**
 * A cop actor in cops and crooks. 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class CopActor extends PawnActor{
	
	/**
	 * Inits a CopActor.
	 * 
	 * @param assets The assets for cops and crooks
	 * @param drawable The first drawable of this actor
	 * @param scaling The scaling used for the drawable
	 * @param pawn The selectable pawn that this actor represents
	 * @param animations The animations for the actor.
	 */
	public CopActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling, final ISelectablePawn pawn, 
			final EnumMap<Animations, Animation> animations){
		super(assets, drawable, scaling, pawn, animations);
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
