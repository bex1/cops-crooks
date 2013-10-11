package com.dat255.project.android.copsandcrooks;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.ModelFactory;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.screens.Assets;
import com.dat255.project.android.copsandcrooks.utils.Values;

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
		
		Map<String, Role> test = new HashMap<String, Role>();
		test.put("Tjuv1", Role.Crook);
		test.put("Tjuv2", Role.Crook);
		test.put("Polis", Role.Cop);
	
		GameModel game = ModelFactory.getInstance().loadGameModel(GameFactory.getInstance().getInteract(), test, "spel");
		
		new LwjglApplication(new CopsAndCrooks(game), cfg);
	}
}
