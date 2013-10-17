package com.dat255.project.android.copsandcrooks;

import com.dat255.project.android.copsandcrooks.network.GameClient;
import com.dat255.project.android.copsandcrooks.network.GameItem;
import com.dat255.project.android.copsandcrooks.network.Network;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MenuActivity extends AbstractActivity {
	
	private CommunicateTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);

        //Retrieve the player's name and set it in the client
        SharedPreferences preferences = getSharedPreferences("options", MODE_PRIVATE);
		
        if(preferences.getString("NAME", "invalid").equals("invalid")){
        	firstTimeSetup();
        }else{
        	GameClient.getInstance().setPlayerName(preferences.getString("NAME", "default"));
        }
        
        if(!preferences.getString("IP", "invalid").equals("invalid"))
         	GameClient.getInstance().setServerIP(preferences.getString("IP", "default"));  
        task = new CommunicateTask(this);
        
	}
	
	
	
	@Override
	protected void onResume() {
		if(task.getStatus() != AsyncTask.Status.RUNNING){
	        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	        	task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new GameItem[0]);
	        else
	        	task.execute();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		task.cancel(true);
		super.onDestroy();
	}



	public void storeVariable(String variableID, String variableValue){
		SharedPreferences preferences = getSharedPreferences("options", MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();

		editor.putString(variableID, variableValue);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public void firstTimeSetup(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("First time setup");
		alert.setMessage("Enter name:");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				if(name.length() <= 0){
					firstTimeSetup();
				}
				storeVariable("NAME", name);
				GameClient.getInstance().setPlayerName(name);
				storeVariable("IP", Network.DEFAULT_IP);
				GameClient.getInstance().setServerIP(Network.DEFAULT_IP);
			}
		});

		alert.show();
	}
	
	public void enterGame(View v){
		Intent intent = new Intent(this, HostActivity.class);
		startActivity(intent);
	}
	
	public void openInstructions(View v){
		Intent intent = new Intent(this, InstructionsActivity.class);
		startActivity(intent);
	}
	
	public void openOptions(View v){
		Intent intent = new Intent(this, OptionsActivity.class);
		startActivity(intent);
	}
	
	public void openSearchGame(View v){
		Intent intent = new Intent(this, GameBrowseActivity.class);
		startActivity(intent);
	}

}
