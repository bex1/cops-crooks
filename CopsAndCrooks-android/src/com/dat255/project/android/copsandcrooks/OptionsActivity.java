package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.utils.PreferencesManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OptionsActivity extends Activity {
	
	private CheckBox soundCheckBox;
	private EditText nameEditText;
	private SeekBar volumeSeekBar;
	private EditText editTextIP;
	
	private String name;
	private String ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		soundCheckBox = (CheckBox) findViewById(R.id.soundCheckBox);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		editTextIP = (EditText) findViewById(R.id.serverIPTextEdit);
		volumeSeekBar = (SeekBar) findViewById(R.id.volumeSeekBar);
		
		PreferencesManager prefs = PreferencesManager.getInstance();
		prefs.setSoundEnabled(soundCheckBox.isChecked());
		prefs.setVolume(volumeSeekBar.getProgress() / 100f);
		
		name = GameClient.getInstance().getPlayerName();
		nameEditText.setText(name);
		
		ip = GameClient.getInstance().getServerIP();
		editTextIP.setText(ip);
		
		volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				PreferencesManager.getInstance().setVolume(progress / 100f);
			}
		});
		
		soundCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	boolean soundOn = soundCheckBox.isChecked();
            	PreferencesManager prefs = PreferencesManager.getInstance();
            	prefs.setSoundEnabled(soundOn);
            }
        });
		
		nameEditText.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				name = nameEditText.getText().toString();
				if(!name.equals(""))
					GameClient.getInstance().setPlayerName(name);
				
				return false;
			}
		});
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		
		name = nameEditText.getText().toString();
		if(!name.equals(""))
			GameClient.getInstance().setPlayerName(name);
		
		String oldServerIP = GameClient.getInstance().getServerIP();
		String newServerIP = editTextIP.getText().toString();

		if(!oldServerIP.equals(newServerIP)){
			GameClient.getInstance().setServerIP(newServerIP);
			GameClient.getInstance().stopClient();
		}
		
		//Store the name and serverIP in SharedPreferences.
		SharedPreferences preferences = getSharedPreferences("options", MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString("NAME", name);
		editor.putString("IP", newServerIP);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}
}
