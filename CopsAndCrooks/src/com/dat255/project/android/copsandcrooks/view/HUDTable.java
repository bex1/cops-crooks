package com.dat255.project.android.copsandcrooks.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.GameModel;
import com.dat255.project.android.copsandcrooks.model.IPawn;
import com.dat255.project.android.copsandcrooks.model.IPlayer;
import com.dat255.project.android.copsandcrooks.model.Role;
import com.dat255.project.android.copsandcrooks.model.Wallet;
import com.dat255.project.android.copsandcrooks.model.GameModel.GameState;
import com.dat255.project.android.copsandcrooks.utils.MusicManager;
import com.dat255.project.android.copsandcrooks.utils.PreferencesManager;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;
import com.dat255.project.android.copsandcrooks.utils.MusicManager.CopsAndCrooksMusic;

/**
 * The hud table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class HUDTable extends Table implements PropertyChangeListener {
	private Wallet wallet;
	private Label scoreLabel;
	private final Label waitingLabel;
	private final GameModel model;
	private final CheckBox checkBox;
	
	/**
	 * Ints the hud table. 
	 * 
	 * @param assets the assets of the game
	 * @param player the playerclient.
	 * @param model the game model.
	 * @param model hudStage, the stage where this table is drawn.
	 * @param players the players of the game.
	 */
	public HUDTable(final Assets assets, final IPlayer player, final GameModel model, final Stage hudStage, final Collection<? extends IPlayer> players) {
		this.setFillParent(true);
		this.model = model;
		model.addObserver(this);
		
		waitingLabel = new Label("Waiting for your turn", assets.getSkin());
		waitingLabel.setVisible(false);
		waitingLabel.setAlignment(Align.center);
		waitingLabel.setColor(Color.BLACK);
		
		Label soundLabel = new Label("Sound", assets.getSkin());
		soundLabel.setAlignment(Align.right);
		soundLabel.setColor(Color.BLACK);
		soundLabel.setFontScale(0.6f);
		
		checkBox = new CheckBox("", assets.getSkin());
		checkBox.getImage().setScale(1.7f);
		checkBox.setChecked(PreferencesManager.getInstance().isSoundEnabled());
		checkBox.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				boolean soundOn = checkBox.isChecked();
				PreferencesManager.getInstance().setSoundEnabled(soundOn);
				MusicManager.getInstance().setEnabled(soundOn);
				SoundManager.getInstance().setEnabled(soundOn);
				if (soundOn) {
					MusicManager.getInstance().play(CopsAndCrooksMusic.GAME);
				}
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		// Extract the right wallet
		Role role = player.getPlayerRole();
		if (role == Role.Crook) {
			IPawn pawn = player.getCurrentPawn();
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
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				hudStage.addActor(scoreBoard);
				super.touchUp(event, x, y, pointer, button);
			}
			
		});
		
		AtlasRegion region = assets.getAtlas().findRegion("game-screen/hud/coins-icon");
		Image coin = new Image(region);
		add(scoreBoardButton).padLeft(10).left();
		add(scoreLabel).prefWidth(100).top().right().pad(10);
		add(coin).minWidth(coin.getWidth()).top().right().pad(10);

		row();
		add(waitingLabel).colspan(3).expandY().bottom().padBottom(50);
		row();
		add();
		add(soundLabel).padBottom(6).right().bottom().expandX();
		add(checkBox).pad(10).left().bottom();
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
				if (model.getGameState() == GameState.WAITING) {
					waitingLabel.setVisible(true);
				} else {
					waitingLabel.setVisible(false);
				}
			}
		}
	}
}
