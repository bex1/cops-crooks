package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.text.Utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;

public class HUDTable extends Table implements PropertyChangeListener {
	private Wallet wallet;
	private Label scoreLabel;
	
	public HUDTable(Assets assets, IPlayer player) {
		this.setFillParent(true);
		
		// Extract the right wallet
		Role role = player.getPlayerRole();
		if (role == Role.Crook) {
			IMovable pawn = player.getCurrentPawn();
			if (pawn instanceof Crook) {
				Crook crook = (Crook)pawn;
				wallet = crook.getWallet();
			}
		} else if (role == Role.Cop) {
			wallet = player.getWallet();
		}
		
		if(wallet != null) {
			wallet.addObserver(this);
			
			AtlasRegion region = assets.getAtlas().findRegion("game-screen/hud/coins-icon");
			Image coin = new Image(region);
			scoreLabel = new Label(String.format("%6d%n", wallet.getCash()), assets.getSkin());
			scoreLabel.setFontScale(0.5f);
			scoreLabel.setAlignment(Align.right);
			scoreLabel.setColor(Color.BLACK);
			add().expand();
			add(scoreLabel).prefWidth(100).top().right().pad(10);
			add(coin).top().right().pad(10);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wallet) {
			if (evt.getPropertyName() == Wallet.PROPERTY_CASH && scoreLabel != null) {
				scoreLabel.setText(String.format("%6d%n", wallet.getCash()));
			}
		}
	}
}
