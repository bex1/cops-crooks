package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public void enterGame(View v){
		//TODO should start the lobbyactivity instead
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}
	
	public void openInstructions(View v){
		Intent intent = new Intent(this, InstructionsActivity.class);
		startActivity(intent);
	}
	
	public void openOptions(View v){
		Intent intent = new Intent(this, OptionsActivity.class);
		startActivity(intent);
	}
	
	public void openSearchGame(View v){
		Intent intent = new Intent(this, GameBrowseActivity.class);
		startActivity(intent);
	}

}
