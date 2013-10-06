package com.dat255.project.android.copsandcrooks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class GameBrowseActivity extends Activity {
	
	ListView gameListView;
	GameItemAdapter gameItemAdapter;
	
	public static final String FROM_LOBBY = "FROM_LOBBY";
	public static final String GAME_ITEM = "GAME_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_browse);
		
		gameListView = (ListView) findViewById(R.id.gameListView);
		gameItemAdapter = new GameItemAdapter(this, new ArrayList<GameItem>());
		gameListView.setAdapter(gameItemAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_browse, menu);
		return true;
	}
	
	//for testing
	public void newItem(View v){
		GameItem gi = new GameItem("new item", 5);
		gameItemAdapter.add(gi);
	}
	
	public void refreshGameList(View v){
		//TODO retrieve the games from the server and update the list
	}
	
	public void itemAnswer(GameItem gameItem){
		if(!gameItem.hasGameStarted()){
			enterLobby(gameItem);
		}
	}
	
	public void enterLobby(GameItem gameItem){
		Intent intent = new Intent(this, LobbyActivity.class);
		intent.putExtra(FROM_LOBBY, true);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
	}
	
	

}
