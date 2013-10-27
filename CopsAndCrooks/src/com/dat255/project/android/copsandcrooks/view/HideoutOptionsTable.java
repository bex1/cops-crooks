package com.dat255.project.android.copsandcrooks.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.HideoutTile;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * The hideout options table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class HideoutOptionsTable extends Table implements PropertyChangeListener {
	
	private final HideoutTile hideout;
	private final Label withLabel, depLabel;
	private Crook visitor;
	private final Table thisTable;
	private final Stage hudStage;

	private static final String withFormat = "WITHDRAW %d $";
	private static final String depFormat = "DEPOSIT %d $";
	private static final String withEmpty = "Nothing to withdraw";
	private static final String depEmpty = "Nothing to deposit";

	/**
	 * Ints the hideout options table. 
	 * 
	 * @param assets the assets of the game
	 * @param hideout the hideout to handle.
	 * @param hudStage the stage to draw the table on.
	 */
	public HideoutOptionsTable(final Assets assets, final HideoutTile hideout, final Stage hudStage) {
		this.hideout = hideout;
		this.hudStage = hudStage;
		thisTable = this;
		hideout.addObserver(this);
		//debug();
		this.setFillParent(true);
		
		TextureAtlas atlas = assets.getAtlas();
		AtlasRegion withReg = atlas.findRegion("game-screen/hud/bag");
		AtlasRegion withClickReg = atlas.findRegion("game-screen/hud/bagClicked");
		Drawable withDraw = new TextureRegionDrawable(withReg);
		Drawable withClickDraw = new TextureRegionDrawable(withClickReg);
		Button withdrawButton = new Button(withDraw, withClickDraw);
		withdrawButton.addListener(withdrawListener);
		withLabel = new Label("", assets.getSkin());
		withLabel.setFontScale(0.7f);
		withLabel.setColor(Color.BLACK);
		
		AtlasRegion depReg = atlas.findRegion("game-screen/hud/deposit");
		AtlasRegion depClickReg = atlas.findRegion("game-screen/hud/depositClick");
		Drawable depDraw = new TextureRegionDrawable(depReg);
		Drawable depClickDraw = new TextureRegionDrawable(depClickReg);
		Button depositButton = new Button(depDraw, depClickDraw);
		depositButton.addListener(depositListener);
		depLabel = new Label("", assets.getSkin());
		depLabel.setFontScale(0.7f);
		depLabel.setColor(Color.BLACK);
		
		AtlasRegion canReg = atlas.findRegion("game-screen/hud/cancel");
		AtlasRegion canClickReg = atlas.findRegion("game-screen/hud/cancelClicked");
		Drawable canDraw = new TextureRegionDrawable(canReg);
		Drawable canClickDraw = new TextureRegionDrawable(canClickReg);
		Button cancelButton = new Button(canDraw, canClickDraw);
		cancelButton.addListener(cancelListener);
		Label cancelLabel = new Label("CANCEL", assets.getSkin());
		cancelLabel.setFontScale(0.7f);
		cancelLabel.setColor(Color.BLACK);
		
		add(withdrawButton).space(0, 0, 10, 20);
		add(withLabel).spaceBottom(10).uniform().left().prefWidth(Values.GAME_VIEWPORT_WIDTH/2);
		row();
		add(depositButton).space(10, 0, 10, 20);
		add(depLabel).spaceBottom(20).spaceTop(10).uniform().left();
		row();
		add(cancelButton).space(10, 0, 0, 20);
		add(cancelLabel).spaceTop(10).uniform().left();
	}
	
	private ClickListener withdrawListener = new ClickListener() {
		@Override
		public void touchUp(InputEvent event, float x, float y,
				int pointer, int button) {
			if (visitor != null && hideout.hasStoredCash(visitor)) {
				hideout.withdrawCash(visitor);
				hudStage.getRoot().removeActor(thisTable);
			}
			super.touchDown(event, x, y, pointer, button);
		}
	};
	
	private ClickListener depositListener = new ClickListener() {
		@Override
		public void touchUp(InputEvent event, float x, float y,
				int pointer, int button) {
			if (visitor != null && visitor.getWallet().getCash() > 0) {
				hideout.depositCash(visitor);
				hudStage.getRoot().removeActor(thisTable);
			}
			super.touchDown(event, x, y, pointer, button);
		}
	};
	
	private ClickListener cancelListener = new ClickListener() {
		@Override
		public void touchUp(InputEvent event, float x, float y,
				int pointer, int button) {
			hideout.cancelInteraction();
			hudStage.getRoot().removeActor(thisTable);
			super.touchDown(event, x, y, pointer, button);
		}
	};

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == hideout) {
			if (evt.getPropertyName() == HideoutTile.PROPERTY_HIDEOUT_INTERACT) {
				Object obj = evt.getNewValue();
				if (obj instanceof Crook) {
					visitor = (Crook)obj;
					int cash = hideout.getStoredCashAmount(visitor);
					if (cash > 0) {
						withLabel.setText(String.format(withFormat, cash));
					} else {
						withLabel.setText(withEmpty);
					}
					cash = visitor.getWallet().getCash();
					if (cash > 0) {
						depLabel.setText(String.format(depFormat, cash));
					} else {
						depLabel.setText(depEmpty);
					}
					hudStage.addActor(thisTable);
				}
			}
		}
	}
}