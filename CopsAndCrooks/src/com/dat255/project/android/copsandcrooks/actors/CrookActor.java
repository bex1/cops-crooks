package com.dat255.project.android.copsandcrooks.actors;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.screens.Assets;
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

	public CrookActor(final Assets assets, final TextureRegionDrawable drawable, final Scaling scaling,
			Crook pawn, EnumMap<Animations, Animation> animations) {
		super(assets, drawable, scaling, pawn, animations);
		crook = pawn;
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (crook.isWanted()) {
			wanted.setPosition(getX()+ Values.TILE_WIDTH/2+4, getY()+Values.TILE_HEIGTH + wanted.getHeight()/2+5);
			wanted.draw(batch, parentAlpha);
		}

		super.draw(batch, parentAlpha);
	}

	@Override
	protected void initBackgrounds() {
		super.initBackgrounds();
		TextureAtlas atlas = assets.getAtlas();
		TextureRegionDrawable selected = new TextureRegionDrawable(atlas.findRegion("game-screen/crook/bounty"));
		wanted = new Image(selected, Scaling.none);
	}
}
