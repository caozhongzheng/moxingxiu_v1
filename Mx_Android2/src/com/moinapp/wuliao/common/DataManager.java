package com.moinapp.wuliao.common;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.bean.DataDetail_Bean;
import com.moinapp.wuliao.util.HttpUtil;
import com.moinapp.wuliao.util.MD5EncodeUtil;


public class DataManager {
	
	public static <T> T getDetail_Data(String url, String hexStr, Type type) throws M_Exception {
		List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
		params1.add(new BasicNameValuePair(M_Constant.DATA, hexStr + MD5EncodeUtil.MD5Encode(hexStr.getBytes())));
		return DataDetail_Bean.dataParser(HttpUtil.doPost(url, params1), type);
	}
	
	public static <T> T getDetail_Data(String data, Type type) throws M_Exception {
		return DataDetail_Bean.dataParser(data, type);
	}
	
	public static String getDetailString_Data(String url, String hexStr) throws M_Exception {
		List<BasicNameValuePair> params1 = new ArrayList<BasicNameValuePair>();
		params1.add(new BasicNameValuePair(M_Constant.DATA, hexStr + MD5EncodeUtil.MD5Encode(hexStr.getBytes())));
		return HttpUtil.doPost(url, params1);
	}
}
