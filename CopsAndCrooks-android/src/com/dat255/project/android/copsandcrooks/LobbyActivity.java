package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LobbyActivity extends Activity {
	
	TextView gameNameTextView;
	TextView playerCapTextView;
	ListView playerListView;
	GameItem gameItem;
	ArrayAdapter<String> playerListAdapter;
	
	public static final String GAME_ITEM = "GAME_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);
		
		gameNameTextView = (TextView) findViewById(R.id.gameNameTextView);
		playerCapTextView = (TextView) findViewById(R.id.playerCapTextView);
		playerListView = (ListView) findViewById(R.id.playerListView);
		
		playerListAdapter = new ArrayAdapter<String>(this, 0);
		playerListView.setAdapter(playerListAdapter);
		
		Intent intent = getIntent();
		
		gameItem = (GameItem) intent.getSerializableExtra("GAME_ITEM");
		
		gameNameTextView.setText(gameItem.getName());
		playerCapTextView.setText(gameItem.getCurrentPlayerCount() +"/"+ gameItem.getPlayerCap());
		
		//testing
		gameItem.addPlayer(new PlayerItem("Player #1"));
		gameItem.addPlayer(new PlayerItem("Player #2"));
		gameItem.addPlayer(new PlayerItem("Player #3"));
		
		//TODO add gameItem to server somehow (I don't know...)
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	
	public void updatePlayerList(){
		playerListAdapter.clear();
		for(PlayerItem p: gameItem.getPlayers()){
			playerListAdapter.add(p.getName());
		}
		playerListAdapter.notifyDataSetChanged();
	}
	
	public void startGame(View v){
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
	}
}
