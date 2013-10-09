package com.dat255.project.android.copsandcrooks.screens;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * A class representing text displayed when is in prison.
 *
 */
public class IsInPrisonTable extends Table {

	public IsInPrisonTable(Assets assets){
		this.setFillParent(true);
		
		final Label isInPrisonLabel = new Label("You are in Prison for 3 turns \n     roll a 6 to escape", assets.getSkin());
		
		isInPrisonLabel.setColor(Color.BLACK);
		add(isInPrisonLabel);
	}

	
}
