package com.parentpal.app.android;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.Notification.InboxStyle;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.parentpal.app.database.SqliteDatabaseHelper;
import com.parentpal.app.util.Utility;
import com.parentpal.connection.MyNetwork;

public class GCMIntentService extends GCMBaseIntentService{

	private static final String LOG_TAG = GCMIntentService.class.getSimpleName();
	private static final String SENDER_ID = Utility.SENDER_ID; 
	
	private SQLiteDatabase sqliteDatabase;
	private SqliteDatabaseHelper dbHandler;
	
	public GCMIntentService() {
		super(SENDER_ID);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onError(Context mContext, String errorString) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "GCMReceiver onError " + errorString);
	}

	@Override
	protected void onMessage(Context mContext, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "GCMReceiver onMessage ");
		printBundleValues(intent);
		
		String message = intent.getStringExtra("message");
		
		if (message != null) {
			
			dbHandler = new SqliteDatabaseHelper(mContext);
			
			try {
				sqliteDatabase = dbHandler.getWritableDatabase();

				ContentValues mContentValues = new ContentValues();
				mContentValues.put("notificationText", message);

				sqliteDatabase.insert("tblNotification", null, mContentValues);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				if (sqliteDatabase != null) {
					sqliteDatabase.close();
					sqliteDatabase = null;
				}
			}

			if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				// only for Older than Jelly Bean versions
				showNotification(mContext, message);
			} else {
				sendInboxStyleNotification(mContext);
			}				
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onRegistered(Context mContext, String registrationId) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "Device registered: regId " + registrationId);
		
		MyNetwork mNetwork = new MyNetwork(mContext);
		
		if (mNetwork.isConnected()) {

				//new UpdateGCMRegIDAsync(mContext).execute(requst);
		} else {
			// Remove Reg Id from shared Preference
			GCMRegistrar.unregister(mContext);
		}
		
	}

	@Override
	protected void onUnregistered(Context mContext, String unregisterString) {
		// TODO Auto-generated method stub
		Log.i(LOG_TAG, "GCMReceiver onUnregistered " + unregisterString);
		GCMRegistrar.unregister(mContext);
	}
	
	/**
	 * Print the c2dm Payload Key Value Pair.
	 * 
	 * @param intent
	 *            Intent Bundle received from c2dm push Notification
	 */
	private void printBundleValues(Intent intent) {
		Bundle extras = intent.getExtras();
		Set<String> ks = extras.keySet();
		Iterator<String> iterator = ks.iterator(); 
		while (iterator.hasNext()) {
			String key = iterator.next();
			Log.v(key, intent.getStringExtra(key));
		}
	}
	
	@SuppressWarnings("deprecation")
	private void showNotification(Context mContext, String text) {

		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotifMan = (NotificationManager) mContext
				.getSystemService(ns);

		Notification n = new Notification(R.drawable.icon_logo, " ",System.currentTimeMillis());

		n.defaults = Notification.DEFAULT_ALL;

		n.flags |= Notification.FLAG_SHOW_LIGHTS;
		n.flags |= Notification.FLAG_AUTO_CANCEL;

		int notificationID = Utility.getIntPreference(mContext, Utility.NOTIFICATION_ID);

		n.setLatestEventInfo(mContext, "ParentPal", text, getPendingIntent());

		// Use different Notification ID for Multiple Notification on Bar....

		mNotifMan.notify(notificationID, n);

		Utility.setIntPreference(mContext, Utility.NOTIFICATION_ID, notificationID+1);
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void sendInboxStyleNotification(Context mContext) {
		  
		  PendingIntent mPendingIntent = getPendingIntent();
		  Builder builder = new Notification.Builder(mContext)
					.setContentTitle("Notification")
					.setContentText("Rexcheck")
					.setSmallIcon(R.drawable.icon_logo)
					.addAction(android.R.drawable.ic_input_get, "Check Rexcheck", mPendingIntent);
			
			Cursor mCursor = null;
			ArrayList<String> notificationTextList;
			
		try {
			dbHandler = new SqliteDatabaseHelper(mContext);
			sqliteDatabase = dbHandler.getWritableDatabase();

			mCursor = sqliteDatabase.query("tblNotification",
					new String[] { "notificationText" }, null, null, null,
					null, null);

			if (mCursor.moveToFirst()) {
				
				notificationTextList = new ArrayList<String>();
				
				do {
					notificationTextList.add(mCursor.getString(0));
				} while (mCursor.moveToNext());

				InboxStyle notificationInboxStyle = new Notification.InboxStyle(builder);
				Notification notification = null;
				
				for (int i = 0, j = 1; i < notificationTextList.size(); i++, j++) {
					notificationInboxStyle.addLine(notificationTextList.get(i));
					
					if (j == 5 && notificationTextList.size() > 5) {
						notificationInboxStyle.setSummaryText((notificationTextList.size() - 5) + " more");
					}
					notification = notificationInboxStyle.build();
				}
				
				if (notification != null) {
					// Put the auto cancel notification flag
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					NotificationManager notificationManager = getNotificationManager();
					notificationManager.notify(0, notification);					
				}		
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (sqliteDatabase != null) {
				sqliteDatabase.close();
				sqliteDatabase = null;
			}
		}
	}

	public PendingIntent getPendingIntent() {
		Intent intent = new Intent(this, ParentpalTabActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		return PendingIntent.getActivity(this, 0, intent, 0);
	}
	
	public NotificationManager getNotificationManager() {
		  return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}
}