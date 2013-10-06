package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public class OptionsActivity extends Activity {
	
	CheckBox soundCheckBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		
		soundCheckBox = (CheckBox) findViewById(R.id.soundCheckBox);
		
		soundCheckBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(soundCheckBox.isChecked()){
                	//TODO: mute sound
                }else{
                	
                }
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
