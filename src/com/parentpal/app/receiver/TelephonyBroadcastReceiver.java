/**
 * 
 */
package com.parentpal.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 * 
 *         Parentpal Oct 13, 2013 12:03:08 AM
 * 
 */
public class TelephonyBroadcastReceiver extends BroadcastReceiver {

	private Context mContext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		this.mContext = context;

		Bundle extras = intent.getExtras();
		if (extras != null) {
			String state = extras.getString(TelephonyManager.EXTRA_STATE);

			if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

				// Telephony manager is IDLE

			} else {

				// Call in Progress

			}
		}
	}
}