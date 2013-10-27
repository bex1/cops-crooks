package com.dat255.project.android.copsandcrooks.view;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A visual actor for crooks in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class CrookActor extends PawnActor {
	private final Crook crook;
	private Image wanted;

	/**
	 * 
	 * @param assets The assets for cops and crooks
	 * @param drawable The first drawable of this actor
	 * @param scaling The scaling used for the drawable
	 * @param pawn The selectable pawn that this actor represents
	 * @param animations The animations for the actor.
	 */
	public CrookActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling,
			Crook pawn, EnumMap<Animations, Animation> animations) {
		super(assets, drawable, scaling, pawn, animations);
		crook = pawn;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// Draw wanted flag if wanted
		if (crook.isWanted()) {
			wanted.setPosition(getX()+ Values.TILE_WIDTH/2+4, getY()+Values.TILE_HEIGTH + wanted.getHeight()/2+5);
			wanted.draw(batch, parentAlpha);
		}

		super.draw(batch, parentAlpha);
	}

	@Override
	protected void initBackgrounds() {
		super.initBackgrounds();
		// Init wanted flag
		TextureAtlas atlas = assets.getAtlas();
		TextureRegionDrawable selected = new TextureRegionDrawable(atlas.findRegion("game-screen/crook/bounty"));
		wanted = new Image(selected, Scaling.none);
	}
}