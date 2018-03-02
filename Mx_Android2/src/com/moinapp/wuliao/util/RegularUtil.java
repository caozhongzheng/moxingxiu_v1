package com.moinapp.wuliao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {
	
	public static boolean isEmail(String strEmail) {
		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(strEmail);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isCellphone(String strPhone) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(strPhone);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
}
