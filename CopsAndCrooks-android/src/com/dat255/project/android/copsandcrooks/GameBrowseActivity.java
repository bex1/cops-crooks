package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GameBrowseActivity extends Activity {
	
	ListView gameListView;
	ArrayAdapter<String> arrayAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_browse);
		
		gameListView = (ListView) findViewById(R.id.gameListView);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		gameListView.setAdapter(arrayAdapter);
		gameListView.setTextFilterEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_browse, menu);
		return true;
	}
	
	public void newItem(View v){
		//TODO load a game-object with a string that specifies the game for now
		//A more complex browser might be added if time is available
		arrayAdapter.add("New game string");
		arrayAdapter.notifyDataSetChanged();
	}
	
	public void refreshGameList(){
		//TODO retrieve the games from the server and update the list
		gameListView.setAdapter(null);
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		arrayAdapter.notifyDataSetChanged();
	}

}
