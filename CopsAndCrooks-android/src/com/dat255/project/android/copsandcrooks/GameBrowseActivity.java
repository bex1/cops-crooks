package com.dat255.project.android.copsandcrooks;

import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.List;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

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
		if(GameClient.getInstance().getGameItems() != null){
			System.out.println("Game list not null");
			gameItemAdapter.getData().clear();
			for(GameItem gi: GameClient.getInstance().getGameItems()){
				gameItemAdapter.add(gi);
			}						
		} else {
			System.out.println("Game list is null!");
		}
	}
	
	public void refreshGameList(View v){
		new Thread(new Runnable(){
			public void run(){
				GameClient.getInstance().requestGameItemsFromServer();
//				List<GameItem> gameItems = null;
//				try{
//					gameItems = GameClient.getInstance().requestGameItemsFromServer();
//				}catch (InterruptedException e){
					// refresh failed
//				}

//				if(gameItems != null){
//					for(GameItem gi: gameItems){
//						gameItemAdapter.add(gi);
//					}
//				}
			}
		}).start();
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
