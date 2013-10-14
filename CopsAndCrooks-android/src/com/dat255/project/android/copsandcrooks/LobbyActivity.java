package com.dat255.project.android.copsandcrooks;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.dat255.project.android.copsandcrooks.domainmodel.GameModel;
import com.dat255.project.android.copsandcrooks.domainmodel.ModelFactory;
import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.map.GameFactory;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;
import com.dat255.project.android.copsandcrooks.screens.Assets;

public class LobbyActivity extends Activity {
	
	TextView gameNameTextView;
	TextView playerCapTextView;
	ListView playerListView;
	Button startGameButton;
	Button joinGameButton;
	
	private GameItem gameItem;
	private PlayerItemAdapter playerListAdapter;

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
		
		joinGameButton.setEnabled(!gameItem.hasGameStarted());
		
		for(String name : gameItem.getPlayerNames()){
			System.out.println(name);
			System.out.println(GameClient.getInstance().getPlayerName());
			if(name.equals(GameClient.getInstance().getPlayerName()))
				joinGameButton.setEnabled(false);
		}
		
		
		gameNameTextView.setText(gameItem.getName());

		playerListAdapter = new PlayerItemAdapter(this, gameItem.getPlayers());
		playerListView.setAdapter(playerListAdapter);
		
		playerCapTextView.setText(gameItem.getPlayers().size()+"/"+ gameItem.getPlayerCap());
		
		checkForHost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	
	public void updatePlayerCapTextView(){
		playerCapTextView.setText(gameItem.getPlayers().size()+"/"+ gameItem.getPlayerCap());
	}
	
	public boolean isGameFull(){
		return gameItem.getPlayerCap()-gameItem.getPlayers().size() <= 0;
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
		GameClient.getInstance().setChosenGameItem(gameItem);
		GameClient.getInstance().startGame(gameItem.getID());
		
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
		if(gameItem.getHostId().equals(Installation.id(getApplicationContext()))){
			player = new PlayerItem(GameClient.getInstance().getPlayerName(), Installation.id(getApplicationContext()));
		}else{
			player = new PlayerItem(GameClient.getInstance().getPlayerName(), Installation.id(getApplicationContext()));
		}
		GameClient.getInstance().joinGame(gameItem.getID(), player);
		joinGameButton.setEnabled(false);
	}

	public void changeRole(PlayerItem item) {
		if(gameItem.getHostId().equals(Installation.id(getApplicationContext()))){
			for(PlayerItem pi : playerListAdapter.getData()){
				pi.setRole(Role.Crook);
			}
			item.setRole(Role.Cop);
			
			playerListAdapter.notifyDataSetChanged();
			
			//REMOVE (testing)
			testAddPlayer();
		}
	}
	
	public void testAddPlayer(){
		if(!isGameFull()){
			gameItem.addPlayer(new PlayerItem("Player #" + (int)((Math.random()*127)), "1"));
			playerListAdapter.notifyDataSetChanged();
			//this should be done when a player joins the game
			updatePlayerCapTextView();
		}
	}
}
