/**
 * 
 */
package com.parentpal.app.android;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 11, 2013 11:17:56 AM
 *
 */
public class ParentpalActivityAboutUs extends ParentpalBaseActivity {
	
	private WebView aboutUsWebView;
	
	/* (non-Javadoc)
	 * @see com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_about_us);
		aboutUsWebView = (WebView) findViewById(R.id.aboutUsWebView);
		aboutUsWebView.loadUrl("file:///android_asset/parentpal_aboutus.html");
	}

	public void sendEmail(){
		 try {
             final Intent emailIntent = new Intent(
                           android.content.Intent.ACTION_SEND);
             emailIntent.setType("plain/text");
             emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                           new String[] { "poddar.ankur08@gmail.com" });
             emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ParentPal Debug log");
             
             File mFile = new File(getExternalFilesDir(null) + "/ParentPalLogFile.txt");
             
             if (mFile.exists()) {
            	 Uri URI = Uri.fromFile(mFile);
                 
                 if (URI != null) {
                        emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
                 }				
			}
             
             emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Please find attahed Debug Log file");
             this.startActivity(Intent.createChooser(emailIntent,"Sending email..."));

       } catch (Throwable t) {
             Toast.makeText(this,
                           "Request failed try again: " + t.toString(),Toast.LENGTH_LONG).show();
       }
	}
}
