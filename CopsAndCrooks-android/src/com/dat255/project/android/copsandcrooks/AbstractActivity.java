package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public abstract class AbstractActivity extends Activity {
	
	Toast toast;
	
	public void showMessage(String text){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

}
