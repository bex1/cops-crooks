package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public abstract class AbstractActivity extends Activity {
	
	private Toast toast;
	
	public void showMessage(String text){
		try{
			toast.cancel();
		}catch(Exception e){}
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.show();
	}

}
