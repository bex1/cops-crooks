package com.dat255.project.android.copsandcrooks.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.dat255.project.android.copsandcrooks.model.Crook;
import com.dat255.project.android.copsandcrooks.model.HideoutTile;
import com.dat255.project.android.copsandcrooks.model.IPawn;
import com.dat255.project.android.copsandcrooks.model.IPlayer;

/**
 * The hideout status table of cops and crooks.
 * 
 * A table is a way to layout images, Ui items etc.
 * 
 * The table can then be placed in a scene to be rendered.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class HideoutStatusTable extends Table implements PropertyChangeListener {
	private final HideoutTile hideout;
	private final Map<Crook, Label> moneyLabels;
	
	/**
	 * Inits the hideout status table. 
	 * 
	 * @param assets the assets of the game
	 * @param hideout the hideout to handle.
	 * @param players the players of the game.
	 */
	public HideoutStatusTable(final Assets assets, final HideoutTile hideout, final Collection<? extends IPlayer> players) {
		this.hideout = hideout;
		//this.debug();
		hideout.addObserver(this);
		moneyLabels = new HashMap<Crook, Label>();
		
		this.setFillParent(true);
		
		Label label = new Label("Hideout status", assets.getSkin());
		label.setFontScale(0.8f);
		label.setAlignment(Align.center);
		label.setColor(Color.BLACK);
		add(label).padTop(30).colspan(2);
		
		row();
		

		for (IPlayer player : players) {
			for (IPawn pawn : player.getPawns()) {
				if (pawn instanceof Crook) {
					
					Crook crook = (Crook)pawn;
					Label nameLabel = new Label(player.getName() + ":", assets.getSkin());
					nameLabel.setAlignment(Align.right);
					nameLabel.setFontScale(0.6f);
					nameLabel.setColor(Color.BLACK);
					add(nameLabel).expandX().fillX().uniform().padRight(15);
				
					Label moneyLabel = new Label(String.format("%-6d%n", hideout.getStoredCashAmount(crook)), assets.getSkin());
					moneyLabel.setFontScale(0.6f);
					moneyLabel.setAlignment(Align.left);
					moneyLabel.setColor(Color.BLACK);
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
		if (evt.getSource() == hideout) {
			if (evt.getPropertyName() == HideoutTile.PROPERTY_HIDEOUT_MONEY) {
				Object obj = evt.getNewValue();
				if (moneyLabels.containsKey(obj)) {
					Crook crook = (Crook)obj;
					Label money = moneyLabels.get(crook);
					money.setText(String.format("%-6d%n", hideout.getStoredCashAmount(crook)));
				}
			}
		}
	}
}
