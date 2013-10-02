package com.dat255.project.android.copsandcrooks.screens;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.domainmodel.Crook;
import com.dat255.project.android.copsandcrooks.domainmodel.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.IMovable;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.utils.Utilities;

public class HideoutStatusTable extends Table implements PropertyChangeListener {
	private final HideoutTile hideout;
	private final Map<Crook, Label> moneyLabels;
	
	public HideoutStatusTable(HideoutTile hideout, Collection<? extends IPlayer> players) {
		this.hideout = hideout;
		//this.debug();
		hideout.addObserver(this);
		moneyLabels = new HashMap<Crook, Label>();
		
		this.setFillParent(true);
		
		Label label = new Label("Hideout status", Utilities.getSkin());
		label.setFontScale(0.8f);
		label.setAlignment(Align.center);
		add(label).padTop(30).colspan(2);
		
		row();
		

		for (IPlayer player : players) {
			for (IMovable pawn : player.getPawns()) {
				if (pawn instanceof Crook) {
					
					Crook crook = (Crook)pawn;
					Label nameLabel = new Label(player.getName() + ":", Utilities.getSkin());
					nameLabel.setAlignment(Align.right);
					nameLabel.setFontScale(0.6f);
					add(nameLabel).expandX().fillX().uniform().padRight(15);
				
					Label moneyLabel = new Label(String.format("%-6d%n", 0), Utilities.getSkin());
					moneyLabel.setFontScale(0.6f);
					moneyLabel.setAlignment(Align.left);
					add(moneyLabel).expandX().fillX().uniform().padLeft(50);
					
					moneyLabels.put(crook, moneyLabel);
					row();
				}
			}
		}
		add().expand(0, 1);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Object obj = evt.getSource();
		if (moneyLabels.containsKey(obj)) {
			if (evt.getPropertyName() == HideoutTile.PROPERTY_HIDEOUT_MONEY) {
				Crook crook = (Crook)obj;
				Label money = moneyLabels.get(crook);
				money.setText(String.format("%-6d%n", hideout.getStoredCashAmount(crook)));
			}
		}
	}
}
