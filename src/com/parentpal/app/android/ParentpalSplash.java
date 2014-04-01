package com.parentpal.app.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

/**
 *
 * Application Splash screen. Display the splash for the SPLASH_DISPLAY_TIME
 * milli seconds and Navigate it to Login Screen.
 * 
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 03, 2013 1:18:30 PM
 *
 */
public class ParentpalSplash extends Activity {
	
	private final int SPLASH_DISPLAY_TIME = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_splash);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent intent = new Intent(ParentpalSplash.this,ParentpalWelcomeActivity.class);
				startActivity(intent);
				finish();

			}
		}, SPLASH_DISPLAY_TIME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parentpal_splash, menu);
		return true;
	}
}
