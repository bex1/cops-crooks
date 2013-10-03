package com.dat255.project.android.copsandcrooks;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class HostActivity extends Activity {
	
	SeekBar playerCapSeekBar;
	TextView seekBarTextView;
	EditText gameNameEditText;
	
	Button hostGameButton;
	
	private int playerCap;
	private String gameName;
	
	@SuppressLint("NewApi") //TODO use a lower requirement
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_host);
		
		playerCapSeekBar = (SeekBar) findViewById(R.id.playerCapSeekBar);
		gameNameEditText = (EditText) findViewById(R.id.gameNameEditText);
		seekBarTextView = (TextView) findViewById(R.id.seekBarTextView);
		hostGameButton = (Button) findViewById(R.id.hostGameButton);
		
		playerCapSeekBar.setOnDragListener(playerCapListener);
		gameNameEditText.setOnKeyListener(gameNameListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.host, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	public OnDragListener playerCapListener = new OnDragListener(){

		@Override
		public boolean onDrag(View arg0, DragEvent arg1) {
			playerCap = playerCapSeekBar.getProgress() + 2;
			seekBarTextView.setText("[ "+playerCap+" ]");
			return false;
		}
	};
	
	public OnKeyListener gameNameListener = new OnKeyListener(){

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			gameName = gameNameEditText.getText().toString();
			
			hostButtonEnabled(gameName.equals(null) || gameName.length() == 0);
			return false;
		}
		
	};
	
	@SuppressLint("NewApi")
	public void hostButtonEnabled(boolean status){
		if(status){
			hostGameButton.setClickable(false);
			hostGameButton.setAlpha(0.25f);
		}else{
			hostGameButton.setClickable(true);
			hostGameButton.setAlpha(1);
		}
	}
	
	public void hostGame(View v){
		//TODO host a game & start a lobby
	}

}
