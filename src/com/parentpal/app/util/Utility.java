
/**
 * 
 */
package com.parentpal.app.util;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Application utility class.Able to store the application data in the local
 * preference.
 * @author Ankur Poddar <poddar.ankur08@gmail.com>
 *
 * Parentpal
 * Oct 05, 2013 5:19:40 PM
 *
 */

public class Utility {

	public static final String APPLICATION_PREF_NAME = "ParentpalAppPreference";
	public static final String DATABASE_NAME = "parentpalDatabase";

	public static final String API_BASE_URL = "http://revabhagirathigoraksha.org/parentPal/api/";
	public static final String API_LOGIN_PAGE = "login.php";
	public static final String API_REGISTRATION_PAGE = "register.php";
	
	public static final String KEY_MOBILE_NUMBER = "regMobileNumber";
	public static final String KEY_IS_DISPLAY_ALERT = "isDisplayAlert";
	public static final String KEY_IS_MOBILE_VERIFY = "isMobileNumberVerified";
	public static final String KEY_CUST_ID = "customerID";
	public static final String KEY_UESRNAME = "userName";
	public static final String KEY_PASSWORD = "userPassword";
	public static final String KEY_IS_REMEMBER = "isRemember";
	
	public static final String SENDER_ID = "641278881758";
	public static final String NOTIFICATION_ID = "mNotificationID";
	
	private static final boolean isLogEnabled = true;
	public static final String COPY_LOGFILE_TO_FOLDER = "mailLogfiletoDeveloper";

	public static void Print(String message) {
		Log.d("Parentpal Application", message);
	}
	
	public static void PrintLog(String tag, String message) {
		Log.d("Parentpal Application " + tag, message);
	}

	public static boolean setStringPreference(Context context, String name,
			String Value) {

		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, Value);
		return editor.commit();
	}

	public static boolean setBooleanPreference(Context context, String name,
			Boolean Value) {

		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(name, Value);
		return editor.commit();
	}

	public static boolean setFloatPreference(Context context, String name,
			Float Value) {

		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putFloat(name, Value);
		return editor.commit();
	}

	public static boolean setIntPreference(Context context, String name,
			int Value) {

		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(name, Value);
		return editor.commit();
	}

	public static boolean setLongPreference(Context context, String name,
			Long Value) {

		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(name, Value);
		return editor.commit();
	}

	public static String getStringPreference(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		String preferenceValue = preferences.getString(name, null);
		return preferenceValue;
	}

	public static boolean getBooleanPreference(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		boolean preferenceValue = preferences.getBoolean(name, false);
		return preferenceValue;
	}

	public static Long getLongPreference(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		long preferenceValue = preferences.getLong(name, -1);
		return preferenceValue;
	}

	public static int getIntPreference(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		int preferenceValue = preferences.getInt(name, -1);
		return preferenceValue;
	}

	public static Float getFloatPreference(Context context, String name) {
		SharedPreferences preferences = context.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		float preferenceValue = preferences.getFloat(name, -1);
		return preferenceValue;
	}
	
	public static boolean ClearPrefrence (Context mContext){
		SharedPreferences preferences = mContext.getSharedPreferences(
				APPLICATION_PREF_NAME, Context.MODE_PRIVATE);
		return preferences.edit().clear().commit();
	}
	
/*	public synchronized static boolean print1(Context context, String str) {
		if (str == null) {
			//System.out.println("Utility print String Null");
			return false;
		}
		
		System.out.println(str);
		
		if (context == null) {
			return false;
		}
		
		if (!isLogEnabled) {
			return false;
		}

		System.out.println(str);
		
		if (str.equalsIgnoreCase(COPY_LOGFILE_TO_FOLDER)) {

			return CopyLogFile(context, context.getExternalFilesDir(null)  + "/ParentPalLogFile.txt");

		} else {

			try {

				File file = new File(context.getExternalFilesDir(null) + "/ParentPalLogFile.txt");
				
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				BufferedWriter bos;
				FileWriter f_writer;

				f_writer = null;
				f_writer = new FileWriter(context.getExternalFilesDir(null) + "/ParentPalLogFile.txt" , true);

				bos = null;
				bos = new BufferedWriter(f_writer);
				
				String logText = get_date_and_time(System.currentTimeMillis()) + "   --	" + str;

				if (logText != null) {
					bos.append(logText + "\n");					
				}
				
				bos.newLine();
				bos.flush();
				bos.close();
				
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		//}
	}
	
	
		public synchronized static boolean CopyLogFile(Context context,
			final String path) {

		// System.out.println("Inside Copy Log File Function");

		File dirFolder = new File(context.getExternalFilesDir(null) + "/temp");

		if (!dirFolder.exists()) {
			dirFolder.mkdirs();
		}

		final File file = new File(path);

		if (file.exists()) {

			// System.out.println("Log File Exists");

			String to = context.getExternalFilesDir(null) +"/temp";
			try {
				if (copyFile(context, path, to)) {

					System.out.println("Log File Delete From Device "
							+ file.delete());
					return true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Utility.print(context, "Log File Unable to Copy.");

			return false;
		} else {
			Utility.print(context, "File Does Not Exist return False");
			return false;
		}
	}

	public static boolean copyFile(Context context, String from, String to) throws IOException {
		File sd = Environment.getExternalStorageDirectory();
		
		FileChannel src = null;
		FileChannel dst = null;
		
		try {
			if (sd.canWrite()) {

				// System.out.println("Start Copying Log File");

				// Log.e("COPY FILE", "From : " + from);
				// Log.e("COPY FILE", "To : " + to);

				int end = from.toString().lastIndexOf("/");
				String sourceDirName = from.toString().substring(0, end);
				String sourceFileName = from.toString().substring(end + 1,
						from.length());
				File source = new File(sourceDirName, sourceFileName);

				// Log.e("COPY FILE", "Destination Dir Name : " + to);
				// Log.e("COPY FILE", "sourceFileName File : " +
				// sourceFileName);

				String destinationFileName = System.currentTimeMillis() + "_"
						+ from.toString().substring(end + 1, from.length());

				File destination = new File(to, destinationFileName);
				if (source.exists()) {
					src = new FileInputStream(source).getChannel();
					dst = new FileOutputStream(destination)
							.getChannel();
					Log.e("COPY FILE ",
							"Bytes Copy "
									+ dst.transferFrom(src, 0, src.size()));
				}
			}

			Utility.print(context, "Copy Log File Return True.");

			return true;
		} catch (Exception e) {
			Utility.print(context, "IO Operation Exception " + e.getMessage());
			Utility.print(context, "Copy Log File Return false.");
			return false;
		}finally{
			
			if (src != null) {
				src.close();
				src = null;
			}
			
			if (dst != null) {
				dst.close();
				dst = null;
			}
			
		}
	}
*/
	public static String get_date_and_time(long timestamp) {
		Calendar mCalendar = Calendar.getInstance();
		
		String str_date = (mCalendar.get(Calendar.YEAR)) + "/"
				+ (mCalendar.get(Calendar.MONTH) + 1) + "/" + mCalendar.get(Calendar.DATE);

		String str_time = mCalendar.get(Calendar.HOUR_OF_DAY)
				+ ":" + mCalendar.get(Calendar.MINUTE)
				+ ":" + mCalendar.get(Calendar.SECOND);

		return (str_date + " " + str_time + "#" + mCalendar.getTimeZone()
				.getDisplayName(false, TimeZone.SHORT, Locale.US));

	}
}
