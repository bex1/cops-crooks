package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel.GameState;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;

public class HUDTable extends Table implements PropertyChangeListener {
	private Wallet wallet;
	private Label scoreLabel;
	private Label waitingLabel;
	private final GameModel model;
	private final Stage hudStage;
	
	public HUDTable(final Assets assets, final IPlayer player, final GameModel model, final Stage hudStage, final Collection<? extends IPlayer> players) {
		this.setFillParent(true);
		this.model = model;
		this.hudStage = hudStage;
		model.addObserver(this);
		
		waitingLabel = new Label("Waiting for your turn", assets.getSkin());
		waitingLabel.setVisible(false);
		waitingLabel.setAlignment(Align.center);
		waitingLabel.setColor(Color.RED);
		
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

			scoreLabel = new Label(String.format("%6d%n", wallet.getCash()), assets.getSkin());
			scoreLabel.setFontScale(0.5f);
			scoreLabel.setAlignment(Align.right);
			scoreLabel.setColor(Color.GREEN);

		}
		final ScoreBoardTable scoreBoard = new ScoreBoardTable(assets, players);
		
		TextButton scoreBoardButton = new TextButton("SCORE", assets.getSkin());
		scoreBoardButton.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				hudStage.addActor(scoreBoard);
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				scoreBoard.remove();
				super.touchUp(event, x, y, pointer, button);
			}
			
		});
		
		AtlasRegion region = assets.getAtlas().findRegion("game-screen/hud/coins-icon");
		Image coin = new Image(region);
		add(scoreBoardButton).padLeft(10).left().expandX();
		add(scoreLabel).prefWidth(100).top().right().pad(10);
		add(coin).minWidth(coin.getWidth()).top().right().pad(10);

		row();
		add(waitingLabel).colspan(3).expandY().top().padTop(30);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getSource();
		if (obj == wallet) {
			if (evt.getPropertyName() == Wallet.PROPERTY_CASH && scoreLabel != null) {
				scoreLabel.setText(String.format("%6d%n", wallet.getCash()));
			}
		} else if (obj == model) {
			if (evt.getPropertyName() == GameModel.PROPERTY_GAMESTATE) {
				if (model.getGameState() == GameState.Waiting) {
					waitingLabel.setVisible(true);
				} else {
					waitingLabel.setVisible(false);
				}
			}
		}
	}
}
