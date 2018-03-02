package com.moinapp.wuliao.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

public class FileUtil {
	
	public static void writeFileData(Context context, String fileName, String message){
		try {
			FileOutputStream fout = context.openFileOutput(fileName, context.MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readFileData(Context context, String fileName) {
		File data = context.getFileStreamPath(fileName);
		if (!data.exists()) {
			return null;
		}
		
		String res = "";
		try {
			FileInputStream fis = context.openFileInput(fileName);
			int length = fis.available();
			byte[] buffer = new byte[length];
			fis.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fis.close();
		} catch(Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
