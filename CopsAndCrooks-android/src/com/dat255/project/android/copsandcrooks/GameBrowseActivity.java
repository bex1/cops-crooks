package com.dat255.project.android.copsandcrooks;

import java.util.ArrayList;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;

public class GameBrowseActivity extends Activity {
	
	ListView gameListView;
	GameItemAdapter gameItemAdapter;
	CheckBox hasStartedCheckBox;
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

	private boolean showHasStarted;
	
	public static final String FROM_LOBBY = "FROM_LOBBY";
	public static final String GAME_ITEM = "GAME_ITEM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_browse);
		
		gameListView = (ListView) findViewById(R.id.gameListView);
		gameItemAdapter = new GameItemAdapter(this, new ArrayList<GameItem>());
		gameListView.setAdapter(gameItemAdapter);
		
		hasStartedCheckBox = (CheckBox) findViewById(R.id.hasStartedCheckBox);
		hasStartedCheckBox.setOnClickListener(hasStartedListener);
		showHasStarted = hasStartedCheckBox.isChecked();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_browse, menu);
		return true;
	}
	
	public void refreshGameList(){
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
	
	public void itemAnswer(GameItem gameItem){
		enterLobby(gameItem);
	}
	
	public void enterLobby(GameItem gameItem){
		Intent intent = new Intent(this, LobbyActivity.class);
		intent.putExtra(FROM_LOBBY, true);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
	}
	
	public OnClickListener hasStartedListener = new OnClickListener(){

		@Override
		public void onClick(View arg0) {
			showHasStarted = hasStartedCheckBox.isChecked();
		}
		
	};

}
