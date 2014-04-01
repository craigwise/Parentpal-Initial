/**
 * 
 */
package com.parentpal.app.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.parentpal.app.util.Utility;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 03, 2013 1:50:20 PM
 *
 */
public abstract class ParentpalBaseActivity extends Activity{

	private ProgressDialog mPD;
	protected Activity mActivity;
	protected Activity mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = this;
		mContext = this;

		int content_laout_id = getLayoutXML();
		if (content_laout_id != -1) {
			setContentView(content_laout_id);
		}
	}

	/**
	 * Display a Simple Alert Dialog with the given parameters
	 * 
	 * @param title
	 *            Title of the Alert Dialog
	 * @param message
	 *            Message to be display
	 */
	protected void ShowSimpleDialog(final String title, final String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	/**
	 * Display Non Cancelable Progress Dialog
	 * 
	 * @param txt
	 *            Text message to be Display with Progress Dialog
	 */
	public void showWait(String txt) {
		mPD = new ProgressDialog(this);
		mPD.setCancelable(false);
		mPD.setCanceledOnTouchOutside(false);
		mPD.setMessage(txt);
		mPD.show();
	}

	/**
	 * Show a Simple Progress Dialog with default text "Please Wait..."
	 */

	public void showWait() {
		showWait("Please wait...");
	}

	/**
	 * Hide the progress dialog if isShowing
	 */
	public void hideWait() {
		if (mPD != null && mPD.isShowing())
			mPD.cancel();
	}

	public int getLayoutXML() {
		return -1;
	}

	
	@Override
	protected void onDestroy() {
		// Unregister since the activity is about to be closed.
		
		super.onDestroy();
	}
	

	/**
	 * Delete Application Database with the values.
	 * 
	 * @param context
	 * @return true if application database deleted successfully
	 */
	
	public boolean clearData(Context context) {
		Utility.ClearPrefrence(context);
		return context.deleteDatabase(Utility.DATABASE_NAME);
	}

}
