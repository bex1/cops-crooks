package com.dat255.project.android.copsandcrooks;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

public class GameBrowseActivity extends Activity {
	
	ListView gameListView;
	GameItemAdapter gameItemAdapter;
	CheckBox displayActiveGamesCheckBox;
	CommunicateTask task;
	
	@Override
	protected void onStart() {
		task = new CommunicateTask(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
		else
			task.execute();
		super.onStart();
	}

	@Override
	protected void onStop() {
		task.cancel(true);
		super.onStop();
	}

	private boolean displayActiveGames;
	
	public static final String FROM_LOBBY = "FROM_LOBBY";
	public static final String GAME_ITEM = "GAME_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_browse);
		
		gameListView = (ListView) findViewById(R.id.gameListView);
		gameItemAdapter = new GameItemAdapter(this, new ArrayList<GameItem>());
		gameListView.setAdapter(gameItemAdapter);
		
		displayActiveGamesCheckBox = (CheckBox) findViewById(R.id.displayActiveGamesCheckBox);
		displayActiveGamesCheckBox.setOnCheckedChangeListener(activeGamesListener);
		displayActiveGames = displayActiveGamesCheckBox.isChecked();
		
		displayActiveGames = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_browse, menu);
		return true;
	}
	
	/**
	 * Refresh the list of games. Retrieving games from the server and add them
	 * to the list, if the client is eligible to join.
	 */
	public void refreshGameList(){
		if(GameClient.getInstance().getGameItems() != null){
			System.out.println("Game list not null");
			gameItemAdapter.getData().clear();
			for(GameItem gi: GameClient.getInstance().getGameItems()){
				boolean inGame = false;
				for(PlayerItem pi : gi.getPlayers()){
					if (pi.getID() != null) {
						if(pi.getID().equals(GameClient.getInstance().getClientID())){
							//Check to see if the client has joined the game
							inGame = true;
						}
					}
				}
				if(displayActiveGames && inGame){
					//Displays the game if the player is active (has joined) this game
					gameItemAdapter.add(gi);
				}else if(!gi.hasGameStarted() && (gi.getPlayerCap()-gi.getCurrentPlayerCount()) > 0
						&& !inGame){
					//Displays the game in the list if the game hasn't started,
					//isn't full and the player isn't in the game.
					gameItemAdapter.add(gi);
				}				
			}						
		} else {
			System.out.println("Game list is null!");
		}
	}
	
	/**
	 * Called from the GameItemAdapter when a GameItem is clicked in order to
	 * enter a lobby.
	 * @param gameItem the gameitem to enter the lobby with
	 */
	public void itemAnswer(GameItem gameItem){
		enterLobby(gameItem);
	}
	
	/**
	 * Enter a lobby, creating a new LobbyActivity with the selected GameItem.
	 * @param gameItem the GameItem that holds the info about the game
	 */
	public void enterLobby(GameItem gameItem){
		Intent intent = new Intent(this, LobbyActivity.class);
		intent.putExtra(FROM_LOBBY, true);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
	}
	
	private OnCheckedChangeListener activeGamesListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        	//This determines if activegames will be displayed or not.
        	displayActiveGames = isChecked;
        }
    };

}
