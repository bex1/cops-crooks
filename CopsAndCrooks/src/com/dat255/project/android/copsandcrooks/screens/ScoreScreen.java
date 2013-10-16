package com.dat255.project.android.copsandcrooks.screens;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.CopsAndCrooks;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;
import com.dat255.project.android.copsandcrooks.utils.SoundManager;
import com.dat255.project.android.copsandcrooks.utils.SoundManager.CopsAndCrooksSound;

public class ScoreScreen extends AbstractScreen {
	
	private final Map<Wallet, Label> scoreLabels;
	private final TextButton endButton;
	private Table table;

	public ScoreScreen(Assets assets, CopsAndCrooks game, float stageWidth,
			float stageHeight, final Collection<? extends IPlayer> players) {
		super(assets, game, stageWidth, stageHeight);
		table = new Table();
		scoreLabels = new HashMap<Wallet, Label>();
		table.setTouchable(Touchable.enabled);
		table.setFillParent(true);

		Label label = new Label("Scoreboard", assets.getSkin());
		label.setFontScale(1f);
		label.setAlignment(Align.center);
		label.setColor(Color.WHITE);
		table.add(label).pad(30).colspan(2);

		endButton = new TextButton("Back to menu", assets.getSkin());
		
		endButton.addListener(new ClickListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				Gdx.app.exit();
				super.touchUp(event, x, y, pointer, button);
			}
			
		});

		table.row();

		for (IPlayer player : players) {
			IMovable pawn = player.getCurrentPawn();
			Wallet wallet;
			if (pawn instanceof Crook) {
				Crook crook = (Crook)pawn;
				wallet = crook.getWallet();
			} else {
				wallet = player.getWallet();
			}

			Label nameLabel = new Label(player.getName() + ":", assets.getSkin());
			nameLabel.setAlignment(Align.right);
			nameLabel.setFontScale(0.8f);
			nameLabel.setColor(Color.WHITE);
			table.add(nameLabel).expandX().space(10, 0, 10, 0).fillX();

			Label scoreLabel = new Label(String.format("%-6d%n", wallet.getCash()), assets.getSkin());
			scoreLabel.setFontScale(0.8f);
			scoreLabel.setAlignment(Align.left);
			scoreLabel.setColor(Color.GREEN);
			table.add(scoreLabel).expandX().space(10, 0, 10, 0).fillX().spaceLeft(100);

			scoreLabels.put(wallet, scoreLabel);
			table.row();
		}
		table.add(endButton).size(250, 50).colspan(2).padTop(30).top().expand(0, 1);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		super.render(delta);
	}

	@Override
	public void show(){
		super.show();
		stage.addActor(table);
		SoundManager.getInstance().play(CopsAndCrooksSound.CASH);
	}

}
