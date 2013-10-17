package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.scenes.scene2d.ui.List;
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
	CommunicateTask reciveTask, sendTask;
	
	private GameItem gameItem;
	private PlayerItemAdapter playerListAdapter;
	
	private Task thisTask = Task.none;
	
	public enum Task{
		join,
		start,
		update,
		none;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lobby);

		Intent intent = getIntent();
		gameItem = (GameItem) intent.getSerializableExtra("GAME_ITEM");
		GameClient.getInstance().setChosenGameItem(gameItem);
		
		gameNameTextView = (TextView) findViewById(R.id.gameNameDisplayTextView);
		playerCapTextView = (TextView) findViewById(R.id.playerCapDisplayTextView);
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

		updatePlayerList();

		checkForHost();
		
		reciveTask = new CommunicateTask(this);
		sendTask = new CommunicateTask(this);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			reciveTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
		else
			reciveTask.execute();
	}

	@Override
	protected void onStop() {
		reciveTask.cancel(true);
		sendTask.cancel(false);
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lobby, menu);
		return true;
	}
	

	public void updatePlayerList(){
		gameItem = GameClient.getInstance().getChosenGameItem();
		playerListAdapter = new PlayerItemAdapter(this.getApplicationContext(), gameItem.getPlayers());
		playerListView.setAdapter(playerListAdapter);
		
		this.updatePlayerCapTextView();
		this.checkForHost();
	}
	
	private void updatePlayerCapTextView(){
		playerCapTextView.setText(gameItem.getCurrentPlayerCount() +"/"+ gameItem.getPlayerCap());
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
				if(!gameItem.hasGameStarted()){
					System.out.println("Not host for this game item");
					startGameButton.setClickable(false);
					startGameButton.setEnabled(false);
				}
			}
		} else {
			System.out.println("Game item is null!");
		}
	}
	
	public void startGame(View v){
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void joinGame(View v){
		PlayerItem player = new PlayerItem(GameClient.getInstance().getPlayerName(), Installation.id(getApplicationContext()));
		gameItem.addPlayer(player);
		thisTask = Task.join;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
		else
			sendTask.execute(gameItem);
		sendTask = new CommunicateTask(this);
		
		joinGameButton.setEnabled(false);
	}

	public void changeRole(PlayerItem item) {
		if(gameItem.getHostId().equals(Installation.id(getApplicationContext())) && !gameItem.hasGameStarted()){
			for(PlayerItem pi : playerListAdapter.getData()){
				pi.setRole(Role.Crook);
			}
			item.setRole(Role.Cop);
			playerListAdapter.notifyDataSetChanged();
			thisTask = Task.update;
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				sendTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
			else
				sendTask.execute(gameItem);
			sendTask = new CommunicateTask(this);
		}
	}
	
	public Task getCurrentTask(){
		return thisTask;
	}
}
