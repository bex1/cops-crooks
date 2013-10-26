package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

/**
 * This activity represents the lobby. This is where a player chooses to
 * join and/or start a game. All joined players are displayed here.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class LobbyActivity extends Activity {
	
	private TextView gameNameTextView;
	private TextView playerCapTextView;
	private ListView playerListView;
	private Button startGameButton;
	private Button joinGameButton;
	private CommunicateTask receiveTask, sendTask;
	
	private GameItem gameItem;

	private Task thisTask = Task.none;
	
	public enum Task{
		join,
		start,
		update,
		none
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		Intent intent = getIntent();
		gameItem = (GameItem) intent.getSerializableExtra("GAME_ITEM");
		if (gameItem == null){
			gameItem = (GameItem) savedInstanceState.getSerializable("GameItem");		
		}
		GameClient.getInstance().setChosenGameItem(gameItem);	
		
		gameNameTextView = (TextView) findViewById(R.id.gameNameDisplayTextView);
		playerCapTextView = (TextView) findViewById(R.id.playerCapDisplayTextView);
		playerListView = (ListView) findViewById(R.id.playerListView);
		startGameButton = (Button) findViewById(R.id.startGameButton);
		joinGameButton = (Button) findViewById(R.id.joinGameButton);
		
		gameNameTextView.setText(gameItem.getName());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("GameItem", gameItem);
	}

	@Override
	protected void onStop() {
		receiveTask.cancel(true);
		sendTask.cancel(false);
		super.onStop();
	}

	@Override
	protected void onStart() {
		receiveTask = new CommunicateTask(this);
		sendTask = new CommunicateTask(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			receiveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
		else
			receiveTask.execute();
		
		updatePlayerList();
		super.onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	
	/**
	 * Update playerListView, setting a new PlayerItemAdapter with playerItems
	 * from the server.
	 */
	public void updatePlayerList(){
		gameItem = GameClient.getInstance().getChosenGameItem();
		playerListView.setAdapter(new PlayerItemAdapter(this.getApplicationContext(), gameItem.getPlayers()));
		
		this.updatePlayerCapTextView();
		this.checkForHost();
	}
	
	/**
	 * Update playerCapTextView.
	 */
	private void updatePlayerCapTextView(){
		playerCapTextView.setText(gameItem.getCurrentPlayerCount() +"/"+ gameItem.getPlayerCap());
	}
	
	/**
	 * Check to see if the GameItem stored in this lobby is full.
	 * @return true if the GameItem is full
	 */
	public boolean isGameFull(){
		return (gameItem.getPlayerCap()-gameItem.getCurrentPlayerCount()) <= 0;
	}
	
	private void checkForHost(){
		if(gameItem != null){
			if(gameItem.getHostId().equals(GameClient.getInstance().getClientID())){ 
				System.out.println("Host for this game item");
				joinGameButton.setClickable(false);
				joinGameButton.setEnabled(false);
				startGameButton.setClickable(gameItem.getCurrentPlayerCount() > 1);
				startGameButton.setEnabled(gameItem.getCurrentPlayerCount() > 1);
			} else {
				System.out.println("Not host for this game item");
				if(!gameItem.hasGameStarted() && !isGameFull()){
					startGameButton.setClickable(false);
					startGameButton.setEnabled(false);
					Boolean forJoinButton = !gameItem.getPlayerNames().contains(GameClient.getInstance().getPlayerName());
					joinGameButton.setClickable(forJoinButton);
					joinGameButton.setEnabled(forJoinButton);
				}else if(gameItem.hasGameStarted()){
					Boolean forStartButton = gameItem.getPlayerNames().contains(GameClient.getInstance().getPlayerName());
					startGameButton.setClickable(forStartButton);
					startGameButton.setEnabled(forStartButton);
					joinGameButton.setClickable(false);
					joinGameButton.setEnabled(false);
				}else{
					startGameButton.setClickable(false);
					startGameButton.setEnabled(false);
					joinGameButton.setClickable(false);
					joinGameButton.setEnabled(false);
				}
			}
		} else {
			System.out.println("Game item is null!");
		}
	}
	
	/**
	 * Start a game when the startGameButton is clicked.
	 * @param v the view that calls this method (startGameButton)
	 */
	public void startGame(View v){
		thisTask = Task.start;
		if(!gameItem.hasGameStarted()){
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
			else
				sendTask.execute(gameItem);
		}
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
		finish();
	}
	
	/**
	 * Join a game when the joinGameButton is clicked, adding the client(player)
	 * to the GameItem. joinGameButton is disabled.
	 * @param v the view that calls this method (joinGameButton)
	 */
	public void joinGame(View v){
		PlayerItem player = new PlayerItem(GameClient.getInstance().getPlayerName(), GameClient.getInstance().getClientID());
		gameItem.addPlayer(player);
		joinGameButton.setEnabled(false);
		joinGameButton.setClickable(false);
		thisTask = Task.join;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
		else
			sendTask.execute(gameItem);
		sendTask = new CommunicateTask(this);
	}

	/**
	 * Change the role of a PlayerItem to Cop. All other players are set to Crooks.
	 * @param item the playerItem to change the role of
	 */
	public void changeRole(PlayerItem item) {
		if(gameItem.getHostId().equals(GameClient.getInstance().getClientID()) && !gameItem.hasGameStarted() && gameItem.getCurrentPlayerCount() >1){
			for(PlayerItem pi : gameItem.getPlayers()){
				pi.setRole(Role.Crook);
			}
			item.setRole(Role.Cop);
			thisTask = Task.update;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
			else
				sendTask.execute(gameItem);
			
			sendTask = new CommunicateTask(this);
		}
	}
	
	/**
	 * Return the current task.
	 * @return thisTask
	 */
	public Task getCurrentTask(){
		return thisTask;
	}
}
