package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;
import com.dat255.project.android.copsandcrooks.utils.Utilities;

public class HUDTable extends Table implements PropertyChangeListener {
	private Wallet wallet;
	private Label scoreLabel;
	
	public HUDTable(IPlayer player) {
		this.setFillParent(true);
		
		// Extract the right wallet
		Role role = player.getPlayerRole();
		if (role == Role.Crook) {
			IMovable pawn = player.getCurrentPawn();
			if (pawn instanceof Crook) {
				Crook crook = (Crook)pawn;
				wallet = crook.getWallet();
			}
		} else if (role == Role.Police) {
			wallet = player.getWallet();
		}
		
		if(wallet != null) {
			wallet.addObserver(this);
			
			AtlasRegion region = Utilities.getAtlas().findRegion("game-screen/hud/coins-icon");
			Image coin = new Image(region);
			scoreLabel = new Label(String.format("%06d%n", wallet.getCash()), Utilities.getSkin());
			scoreLabel.setFontScale(0.5f);
			add(scoreLabel).top().left().pad(10);
			add(coin).top().left().padTop(10);
			add().expand();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getSource() == wallet) {
			if (evt.getPropertyName() == Wallet.PROPERTY_CASH && scoreLabel != null) {
				scoreLabel.setText(String.format("%06d%n", wallet.getCash()));
			}
		}
	}
}
