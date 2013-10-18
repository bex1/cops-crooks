package com.dat255.project.android.copsandcrooks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

public class HostActivity extends AbstractActivity {
	
	SeekBar playerCapSeekBar;
	TextView seekBarTextView;
	EditText gameNameEditText;
	
	Button hostGameButton;
	
	private int playerCap;
	private String gameName;
	
	public static final String GAME_ITEM = "GAME_ITEM";
	
	public static final String defaultText = "Enter a name for the game";
	
	private ThisTask thisTask = ThisTask.none;
	
	public enum ThisTask{
		hostGame,
		checkName,
		none;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		
		playerCapSeekBar = (SeekBar) findViewById(R.id.playerCapSeekBar);
		gameNameEditText = (EditText) findViewById(R.id.gameNameEditText);
		seekBarTextView = (TextView) findViewById(R.id.seekBarTextView);
		hostGameButton = (Button) findViewById(R.id.hostGameButton);
		
		playerCapSeekBar.setOnSeekBarChangeListener(playerCapListener);
		gameNameEditText.addTextChangedListener(textWatcher);
		
		hostGameButton.setClickable(false);
		hostGameButton.setEnabled(false);
		
		playerCap = 2;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.host, menu);
		return true;
	}
	
	public OnSeekBarChangeListener playerCapListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			playerCap = arg0.getProgress() + 2;
			seekBarTextView.setText("[ "+playerCap+" ]");
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	private TextWatcher textWatcher = new TextWatcher() {
		 
	    @Override
	    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	 
	    }
	 
	    @Override
	    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	 
	    }
	 
	    @Override
	    public void afterTextChanged(Editable editable) {
	    	//Retrieve the entered string.
	    	gameName = editable.toString();  
	    	hostButtonEnabled(!gameName.equals(null) && gameName.length() != 0);
	    	if(!gameName.equals(null) && gameName.length() != 0){
				canHostGame(gameName);
			}      
	    }
	};
	
	public ThisTask getThisTask(){
		return thisTask;
	}
	
	private void canHostGame(String gameName){
		GameItem gameItem = new GameItem(gameName, 0);
		thisTask = ThisTask.checkName;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new CommunicateTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
		else
			new CommunicateTask(this).execute(gameItem);
	}
	
	public void hostButtonEnabled(boolean status){
		if(status){
			hostGameButton.setClickable(true);
			hostGameButton.setEnabled(true);
		}else{
			hostGameButton.setClickable(false);
			hostGameButton.setEnabled(false);
			showMessage("This game name already exist");
		}
	}
	
	public void hostGame(View v){
		System.out.println("Network: Creating game");
		
		GameItem gameItem = new GameItem(gameNameEditText.getText().toString(), playerCap);
		gameItem.setHostId(Installation.id(getApplicationContext()));
		System.out.println(Installation.id(getApplicationContext()));
		
		
		PlayerItem player;
		if(GameClient.getInstance().getPlayerName() != null)
			player = new PlayerItem(GameClient.getInstance().getPlayerName(), Installation.id(getApplicationContext()) );
		else
			player = new PlayerItem("DefaultPlayerName", Installation.id(getApplicationContext()));
		player.setRole(Role.Cop);
		gameItem.addPlayer(player);
		thisTask = ThisTask.hostGame;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new CommunicateTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
		else
			new CommunicateTask(this).execute(gameItem);
		
		Intent intent = new Intent(this, LobbyActivity.class);
		intent.putExtra(GAME_ITEM, gameItem);
		startActivity(intent);
		finish();
	}

}
