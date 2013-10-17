package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public abstract class AbstractActivity extends Activity {
	
	public void showError(String text){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

}
