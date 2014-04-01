/**
 * 
 */
package com.parentpal.app.android;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.parentpal.connection.HttpsConnection;
import com.parentpal.connection.MyNetwork;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 03, 2013 2:12:20 PM
 *
 */
public class ParenpalActivityScanBarcode extends ParentpalBaseActivity implements OnClickListener{

	private ImageView imgScanBarCode;
	private final int SCAN_BARCODE_CODE = 101;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle
	 * )
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_scanbarcode);
		
		initilizeComponent();
		
		((ParentpalTabActivity)getParent()).getHomeImageView().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View mView, MotionEvent event) {
				// TODO Auto-generated method stub
				
				((ParentpalTabActivity)getParent()).getTabHost().setCurrentTab(1);
				
				return false;
			}
		});
		
		((ParentpalTabActivity)getParent()).getScanNewBarcodeImageView().setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View mView, MotionEvent event) {
				// TODO Auto-generated method stub
				
				((ParentpalTabActivity)getParent()).getTabHost().setCurrentTab(1);
				sendIntentToScanBarcode();
				
				return false;
			}
		});
	}

	/**
	 * 
	 */
	private void initilizeComponent() {
		// TODO Auto-generated method stub
		imgScanBarCode = (ImageView) findViewById(R.id.imgScanBarCode);
		imgScanBarCode.setOnClickListener(this);
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.imgScanBarCode:
			
			sendIntentToScanBarcode();
			
			break;

		default:
			break;
		}
	}
	
	private void sendIntentToScanBarcode() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(ParenpalActivityScanBarcode.this, ParentpalScanditScannner.class);
		startActivityForResult(intent, SCAN_BARCODE_CODE);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SCAN_BARCODE_CODE) {
			if (resultCode == RESULT_OK) {
				String resultBarcode = data.getStringExtra("resultBarcode");

				
				new GetUPCResponse().execute(resultBarcode);
				
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
			}
		}
	}
	
	
	private class GetUPCResponse extends AsyncTask<Object, Void, String>{

		private String resultBarcode;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showWait();
		}
		
		@Override
		protected String doInBackground(Object... params) {
			// TODO Auto-generated method stub
			
			MyNetwork mNetwork = new MyNetwork(ParenpalActivityScanBarcode.this);
			
			if (mNetwork.isConnected()) {
				HttpsConnection httpConnection = HttpsConnection.createHttpConnection(ParenpalActivityScanBarcode.this);
				
				List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		        nameValuePair.add(new BasicNameValuePair("key", "c1Hj_jqwpvceZHLimjUwEXMgAoU_G2im0whVwBnTg3J"));
		        
				resultBarcode = (String)params[0];
				
				return httpConnection.getResponseFromNameValuePairGetRequest("https://api.scandit.com/v2/products", resultBarcode, nameValuePair);
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
				
				// {"basic":{"name":"AC","category":"electronics"}}

				//Toast.makeText(ParenpalActivityScanBarcode.this, "Barcode Scan API Result "+ result, Toast.LENGTH_SHORT).show();
				
				try {
					String productName = null;
					String productCategory = null;
					
					JSONObject resultObject = new JSONObject(result);
					
					JSONObject basicObject = resultObject.getJSONObject("basic");
					
					if (basicObject.has("name")) {
						productName = basicObject.getString("name");
					}
					
					if (basicObject.has("category")) {
						productCategory = basicObject.getString("category");
					}
					
					Intent intent = new Intent(ParenpalActivityScanBarcode.this, ParenpalActivityScanSearchResult.class);
					intent.putExtra("productName", productName);
					intent.putExtra("productCategory", productCategory);
					intent.putExtra("productBarCode", resultBarcode);
					
					startActivity(intent);
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//Toast.makeText(ParenpalActivityScanBarcode.this, "Search Result " + result, Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(ParenpalActivityScanBarcode.this, "Some error please try again later.", Toast.LENGTH_SHORT).show();
			}
			
			hideWait();
		}
    	
    }

}
