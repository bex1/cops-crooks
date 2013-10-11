package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dat255.project.android.copsandcrooks.network.GameClient;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GameClient.getInstance().start();
        
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}