package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * This activity represents the instructions.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class InstructionsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructions);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.instructions, menu);
		return true;
	}
}
