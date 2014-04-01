package com.parentpal.connection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.parentpal.app.util.Utility;

/**
 * Application Network Connection class able to create the HTTP post connection
 * with the given page URl and namevalue pair.
 * 
 * @author Krishnakant <me.krishnakant@outlook.com>
 * 
 */
public class MyNetwork {

	Context context;

	public MyNetwork(Context context) {
		this.context = context;
	}

	/**
	 * Able to determine that is the Device is connected with network or not.
	 * 
	 * @return true - if Device is connected with network. false - if Device is
	 *         not connected with network.
	 */
	public boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * Able to create the Http Post connection with the given page URL along
	 * with post data.
	 * 
	 * @param page
	 *            Page URL with connection has to be implemented.
	 * @param nameValuePair
	 *            Post data to be post in form of namevaluepair.
	 * @return HTTP Connection result.
	 */

	public String httpPOST(String page, List<NameValuePair> nameValuePair) {

		String response = null;
		int CONNECTION_TIMEOUT = 120000;
		int WAIT_RESPONSE_TIMEOUT = 120000;

		System.setProperty("http.keepAlive", "true");

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpClient httpClient = new DefaultHttpClient(params);

		HttpParams httpParameters = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECTION_TIMEOUT);
		HttpConnectionParams
				.setSoTimeout(httpParameters, WAIT_RESPONSE_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParameters, true);

		HttpPost httpPost;

		try {
			httpPost = new HttpPost(Utility.API_BASE_URL + page);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		try {
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			InputStream is = entity.getContent();
			response = readStream(is);
			Log.i("Response", response);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return response;
	}

	public String doHttpPost(String page, List<NameValuePair> nameValuePair) {
		String resultString = null;

		URL url = null;
		HttpURLConnection urlConnection = null;
	
		try {
			url = new URL(Utility.API_BASE_URL + page);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");

			// send the POST out
			PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
			out.print(new UrlEncodedFormEntity(nameValuePair));
			out.close();

			int response = urlConnection.getResponseCode();
			// if resonse = HttpURLConnection.HTTP_OK = 200, then it worked.

			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			resultString = readStream(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}

		return resultString;
	}


	/**
	 * Convert the InP=putStrean to the String
	 * 
	 * @param in
	 *            Input Stream retrieve from the server connection.
	 * @return String Converted Input Stream
	 */

	private String readStream(InputStream in) {
		BufferedReader reader = null;
		String response = "";
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				response += line;
			}
		} catch (IOException e) {
			Utility.Print("Network "+ "Error");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Utility.Print("Network " + response);
		return response.equals("") ? null : response;
	}
}
