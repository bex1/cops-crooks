package com.dat255.project.android.copsandcrooks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}