package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * This is an abstraction of the activity class. It only contains a showMessage
 * method used to displayed for example an error-message.
 * 
 * @author Group 25, course DAT255 at Chalmers Uni.
 *
 */
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
