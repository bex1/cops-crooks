package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IPawn;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;
import com.dat255.project.android.copsandcrooks.utils.SoundManager.CopsAndCrooksSound;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;

/**
 * The scoreboard table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class ScoreBoardTable extends Table implements PropertyChangeListener {

	private final Map<Wallet, Label> scoreLabels;
	private final TextButton backButton;
	private final ScoreBoardTable instance;

	public ScoreBoardTable(Assets assets, final Collection<? extends IPlayer> players) {
		instance = this;
		scoreLabels = new HashMap<Wallet, Label>();
		this.setTouchable(Touchable.enabled);
		this.setFillParent(true);

		Label label = new Label("Scoreboard", assets.getSkin());
		label.setFontScale(1f);
		label.setAlignment(Align.center);
		label.setColor(Color.WHITE);
		add(label).pad(30).colspan(2);

		backButton = new TextButton("Back", assets.getSkin());
		
		backButton.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				instance.remove();
				super.touchUp(event, x, y, pointer, button);
			}
			
		});

		row();

		for (IPlayer player : players) {
			IPawn pawn = player.getCurrentPawn();
			Wallet wallet;
			if (pawn instanceof Crook) {
				Crook crook = (Crook)pawn;
				wallet = crook.getWallet();
			} else {
				wallet = player.getWallet();
			}
			wallet.addObserver(this);

			Label nameLabel = new Label(player.getName() + ":", assets.getSkin());
			nameLabel.setAlignment(Align.right);
			nameLabel.setFontScale(0.8f);
			nameLabel.setColor(Color.WHITE);
			add(nameLabel).expandX().space(10, 0, 10, 0).fillX();

			Label scoreLabel = new Label(String.format("%-6d%n", wallet.getCash()), assets.getSkin());
			scoreLabel.setFontScale(0.8f);
			scoreLabel.setAlignment(Align.left);
			scoreLabel.setColor(Color.GREEN);
			add(scoreLabel).expandX().space(10, 0, 10, 0).fillX().spaceLeft(100);

			scoreLabels.put(wallet, scoreLabel);
			row();
		}
		add(backButton).size(150, 50).colspan(2).padTop(30).top().expand(0, 1);
	}

	@Override
	protected void drawBackground(SpriteBatch batch, float parentAlpha) {
		super.drawBackground(batch, parentAlpha);

		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getSource();
		if (scoreLabels.containsKey(obj)) {
			if (evt.getPropertyName() == Wallet.PROPERTY_CASH) {
				Wallet wallet = (Wallet)obj;
				if (wallet.getCash() > 0) {
					SoundManager.getInstance().play(CopsAndCrooksSound.CASH);
				}
				Label score = scoreLabels.get(wallet);
				score.setText(String.format("%-6d%n", wallet.getCash()));
			}
		}
	}

}
