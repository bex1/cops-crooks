package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        GameClient.getInstance().setClientID(Installation.id(getApplicationContext()));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        	new CommunicateTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
        else
        	new CommunicateTask(this).execute();
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}