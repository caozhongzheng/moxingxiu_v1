package com.zyh.volleybox;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Type;

/**
 * 数据获取及处理
 * @author Administrator
 *
 */
public class DataManager {

	/**
	 * get获取泛型数据
	 * @param mQueue
	 * @param type
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static <T> T getDataObject(RequestQueue mQueue, Type type, final String url, Object... params) throws Exception {
		String mUrl = String.format(url, params);

		RequestFuture<T> future = RequestFuture.newFuture();
		LionGsonRequest<T> re = new LionGsonRequest<T>(mUrl, type, null, future, future);
		mQueue.add(re);

		T response = future.get(20, TimeUnit.SECONDS); // this will block
		return response;
	}
	
	/**
	 * post获取泛型数据
	 * @param mQueue
	 * @param type
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static <T> T postDataObject(RequestQueue mQueue, Type type, final String url, final HashMap<String, String> params) throws Exception {
		RequestFuture<T> future = RequestFuture.newFuture();
		LionGsonRequest<T> re = new LionGsonRequest<T>(Method.POST, url, type, null, future, future) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {

				return params;
			}
			
		};
		mQueue.add(re);

		T response = future.get(20, TimeUnit.SECONDS); // this will block
		return response;
	}

	/**
	 * get获取String类型数据
	 * @param mQueue
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String getDataString(RequestQueue mQueue, final String url, Object... params) throws Exception {
		String mUrl = String.format(url, params);

		RequestFuture<String> future = RequestFuture.newFuture();
		StringRequest re = new StringRequest(mUrl, future, future);
		mQueue.add(re);

		String response = future.get(); // this will block

		return response;
	}
	
	/**
	 * post获取String类型数据
	 * @param mQueue
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String postDataString(RequestQueue mQueue, final String url, final HashMap<String, String> params) throws Exception {
		RequestFuture<String> future = RequestFuture.newFuture();
		StringRequest re = new StringRequest(Method.POST, url, future, future) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
			
		};
		mQueue.add(re);

		String response = future.get(); // this will block

		return response;
	}
}
