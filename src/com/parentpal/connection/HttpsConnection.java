package com.parentpal.connection;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.util.Log;

public class HttpsConnection {

	private static HttpsConnection httpConnection = null;
	public static int Time_Out = 30000;
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_XML = "application/xml";

	private HttpsConnection(){
		
	}
	
	public synchronized static HttpsConnection createHttpConnection(Context ctx) {

		if (httpConnection == null) {
			httpConnection = new HttpsConnection();
		}
		return httpConnection;
	}

	private synchronized DefaultHttpClient createHttpsClient() {

		HttpParams httpParameters = getHttpParams();

		HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));

		// added by sanjay on 23 Jan 2013

		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		SingleClientConnManager mgr = new SingleClientConnManager(
				client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
				client.getParams());

		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		return httpClient;
	}

	private synchronized BasicHttpParams getHttpParams() {
		BasicHttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, Time_Out);
		HttpConnectionParams.setSoTimeout(httpParameters, Time_Out * 2);
		return httpParameters;
	}

	public synchronized String getResponseFromHttpPostRequest(String URL,
			String jsonRequest) {
		Log.e("URL:", URL + "");
		Log.e("jsonRequest:", jsonRequest + "");
		String result = null;
		HttpClient httpClient = null;
		HttpContext localContext = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		try {
			// client which actually send request
			httpClient = createHttpsClient();
			// use POST method for data sending

			httpPost = new HttpPost(URL);

			StringEntity stringEntity = new StringEntity(jsonRequest);
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));

			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			localContext = new BasicHttpContext();
			// sending data to erver
			response = httpClient.execute(httpPost, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			InputStream inStream = response.getEntity().getContent();
			result = getResultFromStream(inStream);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				return result;
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public synchronized String getResponseFromHttpHeaderPostRequest(String URL,
			String key, String value) {

		Log.e("URL", URL + "");
		Log.e("key", key + "");
		Log.e("value", value + "");

		String result = null;
		HttpClient httpClient = null;
		HttpContext localContext = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		try {
			// client which actually send request
			httpClient = createHttpsClient();
			// use POST method for data sending

			httpPost = new HttpPost(URL);

			// StringEntity stringEntity = new StringEntity(jsonRequest);
			// stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
			// "application/json"));
			//
			// httpPost.setEntity(stringEntity);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			httpPost.setHeader(key, value);

			localContext = new BasicHttpContext();
			// sending data to erver
			response = httpClient.execute(httpPost, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	private synchronized String getResultFromStream(InputStream stream)
			throws Exception {

		StringBuffer buffer = new StringBuffer();
		int ch = 0;
		while ((ch = stream.read()) != -1)
			buffer.append((char) ch);
		String result = buffer.toString().trim();
		Log.e("Upload Log Response", result);
		return result;
	}

	public static synchronized String getResultFromInputStream(
			InputStream stream) throws Exception {

		StringBuffer buffer = new StringBuffer();
		int ch = 0;
		while ((ch = stream.read()) != -1)
			buffer.append((char) ch);
		String result = buffer.toString().trim();
		Log.e("Upload Log Response", result);
		return result;
	}

	public synchronized String getResponseFromHttpGetRequest(String URL) {

		String result = null;
		try {
			DefaultHttpClient client = createHttpsClient();
			HttpGet getRequest = new HttpGet(URL);
			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public synchronized String getResponseFromHttpUriRequest(String URL) {

		String result = null;
		try {
			DefaultHttpClient client = createHttpsClient();
			HttpUriRequest getRequest = new HttpGet(URL);
			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/***
	 * 
	 * @param URL
	 * @param headerMap
	 * @param requsetParams
	 * @return String
	 */
	public synchronized String getResponseFromHttpHeaderPostRequest(String URL,
			HashMap<String, String> headerMap, final String contentType,
			final String requsetParams) {

		Log.e("URL", URL + "");
		Log.e("headerMap", headerMap + "");
		Log.e("requsetParams", requsetParams + "");

		String result = null;
		HttpClient httpClient = null;
		HttpContext localContext = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		try {
			// client which actually send request
			httpClient = createHttpsClient();
			// use POST method for data sending

			httpPost = new HttpPost(URL);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", contentType);

			Set<String> keys = headerMap.keySet();
			for (String key : keys) {
				String value = headerMap.get(key);
				httpPost.setHeader(key, value);
			}

			StringEntity stringEntity = new StringEntity(requsetParams);
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					contentType));
			httpPost.setEntity(stringEntity);

			localContext = new BasicHttpContext();
			// sending data to erver
			response = httpClient.execute(httpPost, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			InputStream inStream = response.getEntity().getContent();
			result = getResultFromStream(inStream);

			// check the response code is HTTP_OK - 200
			// if (statusCode == 200) {
			// InputStream inStream = response.getEntity().getContent();
			// result = getResultFromStream(inStream);
			// } else {
			// Log.e("Result Not Found From Response:", "StatusCode: "
			// + statusCode);
			// }
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	
	/***
	 * 
	 * @param URL
	 * @param headerMap
	 * @return String
	 */
	public synchronized String getResponseFromNameValuePairGetRequest(String URL, String UPC_Code, List<NameValuePair> nameValuePair) {

		String result = null;
		try {
			DefaultHttpClient client = createHttpsClient();
			
			String parameters = URLEncodedUtils.format(nameValuePair, "UTF-8");
			
			HttpGet getRequest = new HttpGet(URL + "/" + UPC_Code + "?" + parameters);

			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
				Log.i("Search Request Response ", "" + result);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}
	
	
	/***
	 * 
	 * @param URL
	 * @param headerMap
	 * @return String
	 */
	public synchronized String getResponseFromHttpHeaderGetRequest(String URL,
			HashMap<String, String> headerMap) {

		String result = null;
		try {
			DefaultHttpClient client = createHttpsClient();
			HttpGet getRequest = new HttpGet(URL);

			getRequest.setHeader("Accept", "application/json");
			getRequest.setHeader("Content-type", "application/json");

			Set<String> keys = headerMap.keySet();
			for (String key : keys) {
				String value = headerMap.get(key);
				getRequest.setHeader(key, value);
			}

			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/***
	 * 
	 * @param URL
	 * @param jsonRequest
	 * @return String
	 */
	public synchronized String getResponseFromHttpPostXMLRequest(String URL,
			String jsonRequest) {

		String result = null;
		HttpClient httpClient = null;
		HttpContext localContext = null;
		HttpPost httpPost = null;
		HttpResponse response = null;
		try {
			// client which actually send request
			httpClient = createHttpsClient();
			// use POST method for data sending

			httpPost = new HttpPost(URL);

			StringEntity stringEntity = new StringEntity(jsonRequest);
			stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/xml"));

			httpPost.setEntity(stringEntity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/xml");

			localContext = new BasicHttpContext();
			// sending data to erver
			response = httpClient.execute(httpPost, localContext);
			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	/**
	 * @param URL
	 * @param jsonString
	 * @return String result
	 */
	public synchronized String getResponseFromHttpEncodedGetRequest(
			final String URL, final String jsonString) {

		String result = null;
		try {
			String encodedJsonString = URLEncoder.encode(jsonString, "UTF-8");

			DefaultHttpClient client = createHttpsClient();
			HttpGet getRequest = new HttpGet(URL + encodedJsonString);
			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200) {
				InputStream inStream = response.getEntity().getContent();
				result = getResultFromStream(inStream);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		}
		return result;
	}

	public synchronized InputStream getResponseStreamFromHttpGetRequest(
			String URL) {
		InputStream inStream = null;
		try {
			DefaultHttpClient client = createHttpsClient();
			HttpGet getRequest = new HttpGet(URL);
			HttpResponse response = client.execute(getRequest);

			int statusCode = response.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			// check the response code is HTTP_OK - 200
			if (statusCode == 200)
				inStream = response.getEntity().getContent();
			else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

		} catch (Exception e) {
			e.printStackTrace();
			inStream = null;
		}
		return inStream;
	}

	/**
	 * 
	 * @param multipartEntity
	 * @param url
	 * @param contentType
	 *            can be null also
	 * @return String (response)
	 */
	public synchronized String openHttpConnection_post_All_Multipart(
			MultipartEntity multipartEntity, String url, String contentType) {
		try {
			Log.e("URL", url + "");
			String result = null;
			InputStream responseStream = null;
			// Create a new HttpClient and Post Header
			DefaultHttpClient httpclient = createHttpsClient();

			HttpPost httppost = new HttpPost(url);

			// HttpPost httppost = new
			// HttpPost("http://10.10.1.147/HTML5/index.php");

			// httppost.setHeader("Content-Type", header);
			// httppost.setHeader("Content-Type", header);
			if (contentType != null)
				httppost.setHeader("Content-type", contentType);
			httppost.setEntity(multipartEntity);

			// Execute HTTP Post Request
			System.out.println("Connecting...");
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(httppost);

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			Log.i("Status Code for UplOad Log is", "" + statusCode);

			if (statusCode == 200) {
				responseStream = httpResponse.getEntity().getContent();
				result = getResultFromStream(responseStream);
				System.out.println("RESULT" + result);
			} else
				Log.e("Result Not Found From Response:", "StatusCode: "
						+ statusCode);

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}