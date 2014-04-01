/**
 * 
 */
package com.parentpal.app.android;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.parentpal.app.database.SqliteDatabaseHelper;
import com.parentpal.app.util.Utility;

/**
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 15, 2013 11:13:45 AM
 *
 */
public class ParentpalActivityFavouriteBarcode extends ParentpalBaseActivity implements OnClickListener{
	
	private SimpleCursorAdapter listAdapter;
	private Cursor mCursor;
	private SqliteDatabaseHelper dbHelper;
	private SQLiteDatabase sqlDB;
	
	private ListView listViewProductFav;
	
	/* (non-Javadoc)
	 * @see com.parentpal.app.android.ParentpalBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parentpal_favourite_product);
		
		listViewProductFav = (ListView) findViewById(R.id.listViewProductFav);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			dbHelper = new SqliteDatabaseHelper(ParentpalActivityFavouriteBarcode.this);
			sqlDB = dbHelper.getWritableDatabase();
			
			mCursor = sqlDB.query("tableProductHistory", null, null, null, null, null, null);
			
			String databaseColumnID[] = new String[] {"productBarCode","productName","productCategory"};
			int resID[] = new int[] {R.id.txtViewProductBarcode, R.id.txtViewProductName, R.id.txtViewProductCategory};
			
			listAdapter = new SimpleCursorAdapter(ParentpalActivityFavouriteBarcode.this, R.layout.parentpal_fav_product_list_row, mCursor, databaseColumnID , resID, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			listViewProductFav.setAdapter(listAdapter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (sqlDB != null) {
				sqlDB.close();
				sqlDB = null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
