package com.moinapp.wuliao.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * 字符串操作类
 * @author Administrator
 *
 */
public class StringsUtil {

	private static final String DEFAULT_CHARACTER = "utf-8";
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(final String str) {
		if (str == null || "".equals(str))
			return true;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * InputStream转String类型
	 * @param inputstream
	 * @return
	 */
	public static String StreamToString(final InputStream inputstream) {
		BufferedReader br=new BufferedReader(new InputStreamReader(inputstream));
		
		StringBuffer sbur = new StringBuffer();
		String temp="";
		try {
			while((temp=br.readLine())!=null)
			{
				sbur.append(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return sbur.toString();
	}
	
	/**
	 * 字符集的编码转换
	 * @param str
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String encoding) throws UnsupportedEncodingException {
		if(encoding == "")
		return new String(str.getBytes(), DEFAULT_CHARACTER);
		else
		return new String(str.getBytes(), encoding);
	}
}
