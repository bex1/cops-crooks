package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.network.GameClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class OptionsActivity extends Activity {
	
	CheckBox soundCheckBox;
	EditText nameEditText;
	
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		soundCheckBox = (CheckBox) findViewById(R.id.soundCheckBox);
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		
		name = GameClient.getInstance().getPlayerName();
		nameEditText.setText(name);
		
		soundCheckBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(soundCheckBox.isChecked()){
                	//TODO: mute sound
                }else{
                	
                }
            }
        });
		
		nameEditText.setOnKeyListener(new OnKeyListener(){

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				name = nameEditText.getText().toString();
				GameClient.getInstance().setPlayerName(name);
				return false;
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}
}
