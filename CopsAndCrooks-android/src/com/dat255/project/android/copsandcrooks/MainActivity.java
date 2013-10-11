package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import com.dat255.project.android.copsandcrooks.network.GameClient;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        GameClient.getInstance().start();
        GameClient.getInstance().setClientID(Installation.id(getApplicationContext()));
        
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
        
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}