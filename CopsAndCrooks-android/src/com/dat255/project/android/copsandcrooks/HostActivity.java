package com.dat255.project.android.copsandcrooks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.dat255.project.android.copsandcrooks.domainmodel.Role;
import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.PlayerItem;

/**
 * This activity represents the host menu. This is where a game is created.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class HostActivity extends AbstractActivity {
	
	private SeekBar playerCapSeekBar;
	private TextView seekBarTextView;
	private EditText gameNameEditText;
	
	private Button hostGameButton;
	
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
	
	private OnSeekBarChangeListener playerCapListener = new OnSeekBarChangeListener(){

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			//Retrieve the playercap from the seekbar and update the seekbartextview
			playerCap = arg0.getProgress() + 2;
			seekBarTextView.setText("[ "+playerCap+" ]");
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			
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
	    	//trim the string to avoid whitespace in gamenames
	    	gameName = gameName.trim();
	    	hostButtonEnabled(!gameName.equals(null) && gameName.length() != 0);
	    	if(!gameName.equals(null) && gameName.length() != 0){
				canHostGame(gameName);
			}
	    }
	};
	
	/**
	 * Return the state of ThisTask.
	 * @return the enum value of ThisTask
	 */
	public ThisTask getThisTask(){
		return thisTask;
	}
	
	/**
	 * Check to see if the desired gamename is available or not.
	 * @param gameName
	 */
	private void canHostGame(String gameName){
		GameItem gameItem = new GameItem(gameName, 0);
		thisTask = ThisTask.checkName;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			new CommunicateTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, gameItem);
		else
			new CommunicateTask(this).execute(gameItem);
	}
	
	/**
	 * Set the clickable and enabled status of the hostbutton.
	 * @param status the new status for the hostbutton
	 */
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
	
	/**
	 * Gets called when hostGameButton is clicked, creating a new game.
	 * This method also adds the client as the host and adds the host to the game.
	 * Finally, a LobbyActivity is started and this activity is finished preventing
	 * a user from going back to the hosting stage.
	 * @param v the view that is calling this method
	 */
	public void hostGame(View v){
		System.out.println("Network: Creating game");
		
		GameItem gameItem = new GameItem(gameName, playerCap);
		gameItem.setHostId(GameClient.getInstance().getClientID());
		
		PlayerItem player;
		if(GameClient.getInstance().getPlayerName() != null)
			player = new PlayerItem(GameClient.getInstance().getPlayerName(), GameClient.getInstance().getClientID());
		else
			player = new PlayerItem("DefaultPlayerName", GameClient.getInstance().getClientID());
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
