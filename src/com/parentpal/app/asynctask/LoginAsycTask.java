/**
 * 
 */
package com.parentpal.app.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.parentpal.app.util.Utility;
import com.parentpal.connection.MyNetwork;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Dec 23, 2013 11:21:18 PM
 *
 */
public class LoginAsycTask extends AsyncTask<Object, Void, String> {
	
	ServerResponseCallback registrationCallBack;
	Context mContext;

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected String doInBackground(Object... objects) {
		// TODO Auto-generated method stub
		
		mContext = (Context) objects[0];
		registrationCallBack = (ServerResponseCallback) objects[0];
		
		
		MyNetwork connection = new MyNetwork ((Context)objects[0]);
		
		if (connection.isConnected()) {
			
			List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("username",(String) objects[1]));
			nameValuePair.add(new BasicNameValuePair("password",(String) objects[2]));

			return connection.httpPOST(Utility.API_LOGIN_PAGE, nameValuePair);
			
		}
		
		return null;
	}
	
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if (result != null) {
			
			//{"success":1,"message":"You are successfully registered with ParentPal","customerID":"7"}

			try {
				
				JSONObject resObject = new JSONObject(result);
				
				if (resObject.has("success")) {
					
					if (resObject.getInt("success") == 1) {
						registrationCallBack.RequestResponseCallBack(true, "");			
						Utility.setStringPreference(mContext, Utility.KEY_CUST_ID, resObject.getString("uid"));
					}else {
						if (resObject.has("message")) {
							registrationCallBack.RequestResponseCallBack(false, resObject.getString("message"));
						}else {
							registrationCallBack.RequestResponseCallBack(false, "Some error, Please try again later.");
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				registrationCallBack.RequestResponseCallBack(false, "Some error, Please try again later.");
			}
		}else {
			
			registrationCallBack.RequestResponseCallBack(false, "Network unavailable, Please check your network settings.");
			
		}
		
	}

}
