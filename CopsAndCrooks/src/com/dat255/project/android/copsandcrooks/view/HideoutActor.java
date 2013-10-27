package com.dat255.project.android.copsandcrooks.view;

import java.util.Collection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.model.HideoutTile;
import com.dat255.project.android.copsandcrooks.model.IPlayer;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

/**
 * A visual actor for hideouts in the game Cops&Crooks.
 * 
 * An actor is both view and controller in the MVC model.
 * 
 * It can act, respond to input and render itself.
 * 
 * Clickable empty image, which shows a table of hideout status when touch is down.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 */
public class HideoutActor extends Actor {
	private final HideoutStatusTable statusTable;
	
	/**
	 * 
	 * @param assets The assets of Cops & Crooks.
	 * @param hideout The hideout which this actor is connected to.
	 * @param players The players of the game.
	 * @param hudStage The stage where the status table will be added to.
	 */
	public HideoutActor(final Assets assets, final HideoutTile hideout, final Collection<? extends IPlayer> players, final Stage hudStage) {
		Point p = hideout.getPosition();
		this.setBounds(p.x * Values.TILE_WIDTH, p.y * Values.TILE_HEIGTH, Values.TILE_WIDTH, Values.TILE_HEIGTH);
		
		statusTable = new HideoutStatusTable(assets, hideout, players);
		this.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (hudStage != null) {
					//Show table.
					hudStage.addActor(statusTable);
				}
				super.touchDown(event, x, y, pointer, button);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// Remove table
				statusTable.remove();
				super.touchDown(event, x, y, pointer, button);
			}
		});
	}
}
