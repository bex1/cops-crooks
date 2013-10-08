package com.dat255.project.android.copsandcrooks;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.utils.Values;

import javax.swing.*;
import java.awt.*;

public class Main {

	private static JSpinner diceSpinner;

	private static void initDebug(){
		JFrame frame = new JFrame("Debug");
		JPanel panel = new JPanel(new FlowLayout());
		frame.add(panel);
		diceSpinner = new JSpinner(new SpinnerNumberModel(1,1,20,1));
		panel.add(diceSpinner);
		frame.pack();
		frame.setVisible(true);
	}

	public static int getDiceDebugValue(){
		return Integer.parseInt(diceSpinner.getValue().toString());
	}

	public static void main(String[] args) {
		initDebug();

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "CopsAndCrooks";
		cfg.useGL20 = true;
		cfg.width = Values.GAME_VIEWPORT_WIDTH;
		cfg.height = Values.GAME_VIEWPORT_HEIGHT;
		
		new LwjglApplication(new CopsAndCrooks(), cfg);
	}
}
