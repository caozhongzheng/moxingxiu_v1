package com.zyh.volleybox;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.HashMap;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public abstract class VolleyBoxApplication extends Application {

	private RequestQueue mQueue;

	public RequestQueue getRequestQueueInstance() {
		if (mQueue == null)
			mQueue = Volley.newRequestQueue(this);

		return mQueue;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		iniData();
	}

	/**
	 * 异常处理
	 * 
	 * @param e
	 */
	protected abstract void exceptionProcessor(Exception e);

	// public void getData() {
	// System.out.println("------------>");
	// final String url = "http://www.videolib.com.cn/app/indexCmd_ad.aspx";
	// try {
	// String data = DataManager.getDataString(getRequestQueueInstance(), url,
	// "1");
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// }
	// }

	// public ArrayList<HomeBanner> getDataObject(final boolean isRefresh,
	// Object... params) {
	// String key = "banner";
	// final String url = "http://www.videolib.com.cn/app/indexCmd_ad.aspx";
	//
	// return getDataObject(new TypeToken<ArrayList<HomeBanner>>() {}.getType(),
	// key, isRefresh, url, params);
	// }

	/**
	 * 初始化本地缓存数据
	 */
	protected abstract void iniData();

	/**
	 * get获取网络和本地数据
	 * 
	 * @param type
	 * @param key
	 * @param isRefresh
	 * @param url
	 * @param params
	 * @return
	 */
	protected <T extends Serializable> T getDataObject(Type type, final String key, final boolean isRefresh, final String url, final Object... params) {
		T data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				data = DataManager.getDataObject(getRequestQueueInstance(), type, url, params);
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * post获取网络和本地数据
	 * 
	 * @param type
	 * @param key
	 * @param isRefresh
	 * @param url
	 * @param params
	 * @return
	 */
	protected <T extends Serializable> T postDataObject(Type type, final String key, final boolean isRefresh, final String url,
			final HashMap<String, String> params) {
		T data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				data = DataManager.postDataObject(getRequestQueueInstance(), type, url, params);
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 读取本地缓存数据
	 * 
	 * @param key
	 * @return
	 */
	protected <T> T readData(final String key) {
		if (FileUtil.isExistDataFile(key, this)) {
			T myresearch = (T) FileUtil.readData(key, this);
			return myresearch;
		} else {
			return null;
		}
	}

	/**
	 * 写入本地缓存数据
	 * 
	 * @param key
	 * @param t
	 */
	protected <T extends Serializable> void writeData(final String key, T t) {
		if (t != null)
			FileUtil.saveData(key, t, this);
	}

}
