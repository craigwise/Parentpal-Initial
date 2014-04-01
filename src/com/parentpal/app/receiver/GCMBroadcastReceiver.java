/**
 * 
 */
package com.parentpal.app.receiver;

import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parentpal.app.util.Utility;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 06, 2013 3:32:18 PM
 *
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {

	public static final String REGISTRATION_ACTION = "com.google.android.c2dm.intent.REGISTRATION";
	public static final String MESSAGE_ACTION = "com.google.android.c2dm.intent.RECEIVE";
	public static final String UNREGISTER_ACTION = "com.google.android.c2dm.intent.UNREGISTER";
	private Context mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */

	public void onReceive(final Context context, final Intent intent) {

		this.mContext = context;
		Utility.Print("GCM onReceive");
		String action = intent.getAction();

		printBundleValues(intent);

		/**
		 * Handle the registration Message
		 */
		if (REGISTRATION_ACTION.equals(action)) {
			handleRegistration(context, intent);
		} else if (MESSAGE_ACTION.equals(action)) {
			Utility.Print("c2dm action:" + action);
		} else if (UNREGISTER_ACTION.equals(action)) {
			Utility.Print("c2dm action:" + action);
		}

		setResultCode(Activity.RESULT_OK);
	}

	private void printBundleValues(Intent intent) {
		Bundle extras = intent.getExtras();
		Set<String> ks = extras.keySet();
		Iterator<String> iterator = ks.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Utility.PrintLog("KEY : " + key,
					"Value : " + intent.getStringExtra(key));
		}
	}

	/**
	 * Handler for registration
	 * 
	 * @param context
	 * @param intent
	 */
	public void handleRegistration(Context context, Intent intent) {

		String registration = intent.getStringExtra("registration_id");

		Utility.Print("Registration:" + registration);

		if (intent.getStringExtra("error") != null) {

			// Registration failed, should try again later.
			Utility.Print("c2dm registration failed");
			String error = intent.getStringExtra("error");
			Utility.PrintLog("c2dm", "ERROR " + error);

		} else if (intent.getStringExtra("unregistered") != null) {

			// unregistration done, new messages from the authorized sender will
			// be rejected
			Utility.PrintLog("c2dm", "unregistered");
			Toast.makeText(context, "unregistered", Toast.LENGTH_SHORT).show();
		} else if (registration != null) {
			Utility.PrintLog("c2dm", registration);

			// Send the registration ID to the 3rd party site that is sending
			// the messages.
			// This should be done in a separate thread.
			// When done, remember that all registration is done.
		}
	}

}
