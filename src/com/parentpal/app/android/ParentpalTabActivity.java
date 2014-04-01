/**
 * 
 */
package com.parentpal.app.android;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.parentpal.app.util.Utility;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 * 
 *         Parentpal Oct 08, 2013 1:04:29 PM
 * 
 */
public class ParentpalTabActivity extends TabActivity {

	private TabHost tabHost;
	
	private ImageView imgHome;
	private ImageView imgScanNewBarcode;

	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_tab);
		// showTabsAtBottom();

		tabHost = getTabHost();
		
		setTabs();
		
		GCMRegistrar.checkDevice(getApplicationContext());
		GCMRegistrar.checkManifest(getApplicationContext());
		final String regId = GCMRegistrar
				.getRegistrationId(getApplicationContext());
		if (regId.equals("")) {
			GCMRegistrar.register(getApplicationContext(), Utility.SENDER_ID);
		} else {
			Log.e("GCMDemoMainActivity", "Already registered");
		}

		imgHome = (ImageView) findViewById(R.id.imgHome);
		imgScanNewBarcode = (ImageView) findViewById(R.id.imgScanNewBarcode);
		
		tabHost.setCurrentTab(1);
	}

	private void setTabs() {
		addTab("Favourite", R.drawable.ic_favourt_list, ParentpalActivityFavouriteBarcode.class);

		addTab("Scan New Barcode", R.drawable.ic_scan_new_code,
				ParenpalActivityScanBarcode.class);
		addTab("About Us", R.drawable.icon_logo, ParentpalActivityAboutUs.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {

		
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		// tabHost.getTabWidget().getChildTabViewAt(0).setTag(c.getSimpleName());

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);

		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);

		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.v("From Back", ParentpalTabActivity.class.getSimpleName());
	}

	@Override
	public void finish() {

		Log.v("From finish", ParentpalTabActivity.class.getSimpleName());
		showAppQuitAlertDialog("Do you want to quit from app?");
	}

	/****
	 * 
	 * @param message
	 */
	private void showAppQuitAlertDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ParentpalTabActivity.this);
		builder.setTitle("Info!");
		builder.setIcon(R.drawable.icon_logo);
		builder.setMessage(message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ParentpalTabActivity.super.finish();
				dialog.dismiss();
				Runtime.getRuntime().gc();
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Runtime.getRuntime().gc();
			}
		});
		AlertDialog msg = builder.create();
		msg.show();
	}

	public ImageView getHomeImageView() {
		
		if (imgHome == null) {
			imgHome = (ImageView) findViewById(R.id.imgHome);
		}
		
		return imgHome;
	}

	public ImageView getScanNewBarcodeImageView() {
		if (imgScanNewBarcode == null) {
			imgScanNewBarcode = (ImageView) findViewById(R.id.imgScanNewBarcode);
		}
		
		return imgScanNewBarcode;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (GCMRegistrar.isRegistered(getApplicationContext())) {
			GCMRegistrar.onDestroy(getApplicationContext());	
		}
		super.onDestroy();
	}
}