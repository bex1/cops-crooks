package com.dat255.project.android.copsandcrooks.actors;

import java.util.Collection;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dat255.project.android.copsandcrooks.domainmodel.HideoutTile;
import com.dat255.project.android.copsandcrooks.domainmodel.IPlayer;
import com.dat255.project.android.copsandcrooks.screens.HideoutStatusTable;
import com.dat255.project.android.copsandcrooks.utils.Point;
import com.dat255.project.android.copsandcrooks.utils.Values;

public class HideoutActor extends Actor {
	private final HideoutStatusTable statusTable;
	
	public HideoutActor(final HideoutTile hideout, final Collection<? extends IPlayer> players, final Stage hudStage) {
		Point p = hideout.getPosition();
		this.setBounds(p.x * Values.TILE_WIDTH, p.y * Values.TILE_HEIGTH, Values.TILE_WIDTH, Values.TILE_HEIGTH);
		
		statusTable = new HideoutStatusTable(hideout, players);
		this.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (hudStage != null) {
					hudStage.addActor(statusTable);
				}
				super.touchDown(event, x, y, pointer, button);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				statusTable.remove();
				super.touchDown(event, x, y, pointer, button);
			}
		});
	}
}