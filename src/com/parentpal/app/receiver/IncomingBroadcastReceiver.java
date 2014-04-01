package com.parentpal.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Nov 01, 2013 10:06:36 AM
 *
 */

public class IncomingBroadcastReceiver extends BroadcastReceiver {

	 // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();
		 
		try {
		     
		    if (bundle != null) {
		         
		        final Object[] pdusObj = (Object[]) bundle.get("pdus");
		         
		        for (int i = 0; i < pdusObj.length; i++) {
		             
		            SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		            String phoneNumber = currentMessage.getDisplayOriginatingAddress();
		             
		            String senderNum = phoneNumber;
		            String message = currentMessage.getDisplayMessageBody();
		 
		            Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
		             
		 
		           // Show alert
		            int duration = Toast.LENGTH_LONG;
		            Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, duration);
		            toast.show();
		             
		        } // end for loop
		      } // bundle is null
		 
		} catch (Exception e) {
		    Log.e("SmsReceiver", "Exception smsReceiver" +e);
		     
		}
	}
}
