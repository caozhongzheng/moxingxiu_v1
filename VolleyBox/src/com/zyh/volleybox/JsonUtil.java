package com.zyh.volleybox;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Json数据处理类
 * @author Administrator
 *
 */
public class JsonUtil {

	
	/**
	 * Json数据转List对象
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List DataToList(String data) {
		if(data != null) {
			Gson gson = new Gson();
			List<Map> map = (List<Map>)gson.fromJson(data, new TypeToken<List<Map>>(){}.getType());
			return map;
		}
		return null;
	}
	
	/**
	 * Json数据转Map对象
	 * @param data
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map DataToMap(String data) {
		if(data != null) {
			Gson gson = new Gson();
			Map map = (Map)gson.fromJson(data, new TypeToken<Map>(){}.getType());
			return map;
		}
		return null;
	}
	
	/**
	 * Json数据转对象
	 * @param data
	 * @param c
	 * @return 
	 */
	public static Object DataToObject(String data, Type c) {
		
		if(data != null) {
			Gson gson = new Gson();
			Object object = gson.fromJson(data, c);
			return object;
		}
		return null;
	}
	
}
