/**
 * 
 */
package com.parentpal.app.database;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 05, 2013 1:47:18 PM
 *
 */
public class SqliteDatabaseHelper extends SQLiteOpenHelper{

	
	private static final String DATABASE_NAME = "parentpalDatabase";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public SqliteDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase sqlDB) {
		// TODO Auto-generated method stub
		
		String tableProductHistory = "CREATE  TABLE tableProductHistory (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
				"productBarCode TEXT NOT NULL  UNIQUE , productName TEXT, productCategory TEXT)";
		sqlDB.execSQL(tableProductHistory);
		
		String tableQuery = "CREATE TABLE tblNotification (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , notificationText TEXT NOT NULL )";
		sqlDB.execSQL(tableQuery);
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase sqlDB, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	/***
	  * @param sqlDB
	  * @param tableName
	  * @param dataMap
	  */
	public void insertOrUpdateDataMap(SQLiteDatabase sqlDB,
			final String tableName, Map<String, String> dataMap) {

		Set<String> keySet = dataMap.keySet();
		StringBuffer columnNames = new StringBuffer();
		columnNames.append("( ");

		StringBuffer values = new StringBuffer();
		values.append(" ( ");
		for (String columnName : keySet) {
			columnNames.append(columnName + ",");
			String columnValue = dataMap.get(columnName);
			columnValue = columnValue.replace("'", "''");
			values.append("'" + columnValue + "'" + ",");
		}
		columnNames.deleteCharAt(columnNames.lastIndexOf(","));
		values.deleteCharAt(values.lastIndexOf(","));

		columnNames.append(" ) ");
		values.append(" ) ");

		String rawQuery = "INSERT OR REPLACE INTO " + tableName + columnNames
				+ " VALUES " + values;
		// //Log.i("insertOrUpdateDataMap, rawQuery", rawQuery);
		sqlDB.execSQL(rawQuery);
	}	
}
