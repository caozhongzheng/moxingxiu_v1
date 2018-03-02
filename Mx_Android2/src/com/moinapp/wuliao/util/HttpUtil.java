package com.moinapp.wuliao.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.moinapp.wuliao.M_Exception;


/**
 * Http操作类
 * 
 * @author Administrator
 * 
 */
public class HttpUtil {
	
	/**
	 * Get请求获取字符串数据
	 * 
	 * @param context
	 * @param url
	 * @return 字符串
	 * @throws M_Exception
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String httpGet(Context context, String url) throws M_Exception {

		try {
			return EntityUtils.toString(httpGetHttpEntity(context, url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Get请求获取流数据
	 * 
	 * @param url
	 * @return
	 * @throws M_Exception
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static InputStream httpGetInputStream(Context context, String url) throws M_Exception {

		try {
			return httpGetHttpEntity(context, url).getContent();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpEntity httpGetHttpEntity(Context context, String url) throws M_Exception {
		
		try {
			// 声明HttpGet对象
			HttpGet httpget = new HttpGet(url);
			// 获取HttpClient对象
			HttpClient httpclient = new DefaultHttpClient();
			// 获取HttpResponse对象
			HttpResponse httpResponse;
			// 请求超时
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
			            // 读取超时
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			
			httpResponse = httpclient.execute(httpget);
			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				return httpResponse.getEntity();
			} else {
				throw M_Exception.http(statusCode);
			}
		} catch(ConnectTimeoutException e) {
			e.printStackTrace();
			throw M_Exception.network(e);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw M_Exception.network(e);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw M_Exception.network(e);
		}
	}

	/**
	 * Post请求获取字符串数据
	 * 
	 * @return
	 * @throws M_Exception
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String httpPost(Context context, String url, List<NameValuePair> params) throws M_Exception {

		try {
			return EntityUtils.toString(httpPostHttpEntity(context, url, params));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author hubaolin
	 * @param postUrl
	 * @param params
	 * @return
	 * @throws M_Exception
	 */

	public static String doPost(String postUrl, List<BasicNameValuePair> params) throws M_Exception {
		String result = null;
		HttpPost httpRequest = null;
		HttpResponse httpResponse = null;
		try {
			httpRequest = new HttpPost(postUrl);
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			throw M_Exception.network(e);
		}
		return result;
	}
	
	public static String doGZipPost(String postUrl, List<BasicNameValuePair> params) throws M_Exception {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("Accept-Encoding", "gzip, deflate");
		try {
			return GZipEntityUtils.toString(httpPostHttpEntity(postUrl, params, hashMap));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}
	
	public static HttpEntity httpPostHttpEntity(String postUrl, List<BasicNameValuePair> params, HashMap<String, String> header_map) throws M_Exception {
		HttpEntity result = null;
		HttpPost httpRequest = null;
		HttpResponse httpResponse = null;
		try {
			httpRequest = new HttpPost(postUrl);
			
			if (header_map != null) {
				Iterator iter = header_map.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					httpRequest.addHeader(entry.getKey().toString(), entry.getValue().toString());
				}
			}
			
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //
				result = httpResponse.getEntity();
			}
		} catch (Exception e) {
			throw M_Exception.network(e);
		}
		return result;
	}

	/**
	 * 
	 * @author hubaolin
	 * @throws M_Exception
	 * @POST 方式请求;
	 * 
	 * @postUrl post请求的URL;
	 * 
	 * @params 求表的参数;
	 */
	public static String doPost(String postUrl, Map<String, String> params) throws M_Exception {
		String result = null;
		HttpPost httpRequest = null;
		HttpResponse httpResponse = null;
		List<BasicNameValuePair> listPara = null;
		if (null != params) {
			listPara = new ArrayList<BasicNameValuePair>();
			Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, String> param = iterator.next();
				listPara.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}
		try {
			httpRequest = new HttpPost(postUrl);
			httpRequest.setEntity(new UrlEncodedFormEntity(listPara, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { //
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			throw M_Exception.network(e);
		}
		return result;

	}

	/**
	 * Post请求获取流数据
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public static InputStream httpPostInputStream(Context context, String url, List<NameValuePair> params) throws M_Exception,
			IllegalStateException, IOException {

		return httpPostHttpEntity(context, url, params).getContent();
	}

	public static HttpEntity httpPostHttpEntity(Context context, String url, List<NameValuePair> params) throws M_Exception {
		
		HttpPost httppost = new HttpPost(url);		
		try {
			HttpEntity Httpentity = new UrlEncodedFormEntity(params, "utf-8");
			httppost.setEntity(Httpentity);
			HttpClient hc = new DefaultHttpClient();
			
			// 请求超时
			hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            // 读取超时
			hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
            

			HttpResponse httpResponse = hc.execute(httppost);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return httpResponse.getEntity();
			} else {
				throw M_Exception.http(statusCode);
			}

		}
		catch(ConnectTimeoutException e) {
			e.printStackTrace();
			throw M_Exception.network(e);
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			throw M_Exception.network(e);
		}
	}
	
	
	/**
	 * Get方式提交数据到服务器
	 * @param path
	 * @param params
	 * @param enc
	 * @return
	 * @throws Exception
	 */
	public static Boolean submitByGet(String path, Map<String, String> params) throws Exception {
		// 拼凑出请求地址
		StringBuilder sb = new StringBuilder(path);
		sb.append("?");
		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry :  params.entrySet()) {
				sb.append(entry.getKey())
				.append("=")
				.append(URLEncoder.encode((String) entry.getValue(), "utf-8"));
				sb.append("&");
			}
		}
		//删除掉最后一个&
		sb.deleteCharAt(sb.length() - 1);
		String str = sb.toString();
		
		URL Url = new URL(str);
		
		HttpURLConnection HttpConn = (HttpURLConnection) Url.openConnection();
		HttpConn.setRequestMethod("GET");
		HttpConn.setReadTimeout(5000);
		// GET方式的请求不用设置什么DoOutPut()之类的吗？
		if (HttpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//			InputStream is = HttpConn.getInputStream();
//			String text = readStream(is);
			return true;
		}
		return false;
	}
	
	
}
