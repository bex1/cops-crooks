package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.domainmodel.Wallet;

public class ScoreBoardTable extends Table implements PropertyChangeListener {
	
private final Map<Wallet, Label> scoreLabels;
	
	public ScoreBoardTable(Assets assets, Collection<? extends IPlayer> players) {
		scoreLabels = new HashMap<Wallet, Label>();
		
		this.setFillParent(true);
		
		Label label = new Label("Scoreboard", assets.getSkin());
		label.setFontScale(1f);
		label.setAlignment(Align.center);
		label.setColor(Color.BLACK);
		add(label).pad(30).colspan(2);
		
		row();

		for (IPlayer player : players) {
			IMovable pawn = player.getCurrentPawn();
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
			nameLabel.setColor(Color.BLACK);
			add(nameLabel).expandX().space(10, 0, 10, 0).fillX();

			Label scoreLabel = new Label(String.format("%-6d%n", wallet.getCash()), assets.getSkin());
			scoreLabel.setFontScale(0.8f);
			scoreLabel.setAlignment(Align.left);
			scoreLabel.setColor(Color.GREEN);
			add(scoreLabel).expandX().space(10, 0, 10, 0).fillX().spaceLeft(100);

			scoreLabels.put(wallet, scoreLabel);
			row();
		}
		add().expand(0, 1);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getSource();
		if (scoreLabels.containsKey(obj)) {
			if (evt.getPropertyName() == Wallet.PROPERTY_CASH) {
				Wallet wallet = (Wallet)obj;
				Label score = scoreLabels.get(wallet);
				score.setText(String.format("%-6d%n", wallet.getCash()));
			}
		}
	}

}
