package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

public class LobbyActivity extends Activity {
	
	TextView gameNameTextView;
	TextView playerCapTextView;
	ListView playerListView;
	Button startGameButton;
	Button joinGameButton;
	
	GameItem gameItem;
	ArrayAdapter<String> playerListAdapter;
	
	public static final String GAME_ITEM = "GAME_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		Intent intent = getIntent();
		gameItem = (GameItem) intent.getSerializableExtra("GAME_ITEM");
		
		gameNameTextView = (TextView) findViewById(R.id.gameNameTextView);
		playerCapTextView = (TextView) findViewById(R.id.playerCapTextView);
		playerListView = (ListView) findViewById(R.id.playerListView);
		startGameButton = (Button) findViewById(R.id.startGameButton);
		joinGameButton = (Button) findViewById(R.id.joinGameButton);
		
		gameNameTextView.setText(gameItem.getName());
		
		playerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, gameItem.getPlayerNames());
		playerListView.setAdapter(playerListAdapter);
		
		playerCapTextView.setText(gameItem.getCurrentPlayerCount() + "/" + gameItem.getPlayerCap());
		updatePlayerList();
		
		checkForHost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	
	public void updatePlayerList(){
		
	}
	
	public void updatePlayerCapTextView(int players){
		playerCapTextView.setText(players +"/"+ gameItem.getPlayerCap() + "   wat " + playerListAdapter.getCount());
	}
	
	public void checkForHost(){
		if(gameItem != null){
			if(gameItem.getHostId().equals(Installation.id(getApplicationContext()))){ 
				System.out.println("Host for this game item");
				joinGameButton.setClickable(false);
				joinGameButton.setEnabled(false);		
			} else {
				System.out.println("Not host for this game item");
				startGameButton.setClickable(false);
				startGameButton.setEnabled(false);
			}
		} else {
			System.out.println("Game item is null!");
		}
	}
	
	public void startGame(View v){
		Intent intent = new Intent(this, GameActivity.class);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
		finish();
	}
	
	public void showError(String text){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public void joinGame(View v){
		PlayerItem player;
//		if(gameItem.getHostId().equals(Installation.id(getApplicationContext()))){
//			player = new PlayerItem(GameClient.getInstance().getPlayerName(), Role.Cop);
//		}else{
			player = new PlayerItem(GameClient.getInstance().getPlayerName(), Role.Crook);
//		}
		
		GameClient.getInstance().joinGame(gameItem.getID(), player);
	}
}
