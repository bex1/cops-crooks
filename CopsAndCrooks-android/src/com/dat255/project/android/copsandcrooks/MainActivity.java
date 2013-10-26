package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dat255.project.android.copsandcrooks.network.GameClient;

/**
 * This is the activity that runs on start.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
public class MainActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
      
        GameClient.getInstance().setClientID(Installation.id(getApplicationContext()));
          
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}