package com.moinapp.wuliao.bean;

import com.google.gson.JsonParseException;
import com.moinapp.wuliao.M_Exception;
import com.zyh.volleybox.JsonUtil;

import java.lang.reflect.Type;

public class DataDetail_Bean<T> {

	private T msg;

	public T getData() {
		return msg;
	}

	public void setData(T msg) {
		this.msg = msg;
	}

	/**
	 * @param data
	 * @return
	 * @throws WHT_Exception
	 */
	public static <T> T dataParser(String data, Type type) throws M_Exception {
		DataDetail_Bean<T> reigst = new DataDetail_Bean<T>();
		try {
			T myanswerlist = (T) JsonUtil.DataToObject(data, type);
			reigst.setData(myanswerlist);
		} catch (JsonParseException e) {
			throw M_Exception.dataParser(e);
		}

		return (T) reigst;
	}
}
