/**
 * 
 */
package com.parentpal.app.android;

import java.util.HashMap;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parentpal.app.database.SqliteDatabaseHelper;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 03, 2013 2:12:20 PM
 *
 */
public class ParenpalActivityScanSearchResult extends ParentpalBaseActivity implements OnClickListener{

	private String productName = "";
	private String productCategory = "";
	private String productBarCode = "";
	
	private TextView txtProductName;
	private TextView txtProductCategory;
	
	private Button btnAddToFav;
	
	private SqliteDatabaseHelper dbHelper;
	private SQLiteDatabase sqlDB = null;
	
	private boolean isAvailableinDB = false;
	
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
		setContentView(R.layout.activity_parentpal_scan_result);
		
		dbHelper = new SqliteDatabaseHelper(ParenpalActivityScanSearchResult.this);
		
		initilizeComponent();

		isAvailableinDB = getFavouriteStatus();
		
		if (isAvailableinDB) {
			btnAddToFav.setText("Remove from Favorite");
		}else {
			btnAddToFav.setText("Add to Favorite");
		}
	}

	private boolean getFavouriteStatus() {
		// TODO Auto-generated method stub
		
		Cursor mCursor = null;
		
		try {
			sqlDB = dbHelper.getWritableDatabase();
		
			mCursor = sqlDB.query("tableProductHistory", null, "productBarCode = ?", new String[]{productBarCode}, null, null, null);
			
			if (mCursor.moveToNext()) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally{
			if (mCursor != null) {
				mCursor.close();
				mCursor = null;
			}
			
			if (sqlDB != null) {
				sqlDB.close();
				sqlDB = null;
			}
		}
	}

	/**
	 * 
	 */
	private void initilizeComponent() {
		// TODO Auto-generated method stub
		
		Intent intent = getIntent();
		
		if (intent != null) {
			
			productBarCode = intent.getStringExtra("productBarCode");
			productName = intent.getStringExtra("productName");
			productCategory = intent.getStringExtra("productCategory");
			
			if (productName == null) {
				productName = "NA";
			}
			
			if (productCategory == null) {
				productCategory = "NA";
			}
			
		}
	
		txtProductCategory = (TextView) findViewById(R.id.txtProductCategory);
		txtProductName = (TextView) findViewById(R.id.txtProductName);
		
		String textProduct = "<font color=#000000>Product Name - </font> <font color=#ffcc00>" + productName + "</font>";
		txtProductName.setText(Html.fromHtml(textProduct));
		
		textProduct = "<font color=#000000>Product Category - </font> <font color=#ffcc00>" + productCategory + "</font>";
		txtProductCategory.setText(Html.fromHtml(textProduct));
		
		btnAddToFav = (Button) findViewById(R.id.btnAddToFav);
		btnAddToFav.setOnClickListener(this);
		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.btnAddToFav:
			
				if (isAvailableinDB) {
					try {
						sqlDB = dbHelper.getWritableDatabase();
						
						sqlDB.delete("tableProductHistory", "productBarCode = ?", new String[]{productBarCode});
						
						btnAddToFav.setText("Add to Favorite");
						isAvailableinDB = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if (sqlDB != null) {
							sqlDB.close();
							sqlDB = null;
						}
					}
				}else {
					try {
						sqlDB = dbHelper.getWritableDatabase();
						
						HashMap<String, String> dataMap = new HashMap<String, String>();
						
						dataMap.put("productBarCode", productBarCode);
						dataMap.put("productCategory", productCategory);
						dataMap.put("productName", productName);
						
						dbHelper.insertOrUpdateDataMap(sqlDB, "tableProductHistory", dataMap);
						
						btnAddToFav.setText("Remove from Favorite");
						isAvailableinDB = true;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally{
						if (sqlDB != null) {
							sqlDB.close();
							sqlDB = null;
						}
					}
				}
			
			break;

		default:
			break;
		}
	}
}
