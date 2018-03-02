package com.zyh.volleybox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.content.Context;

/**
 * 文件操作�?
 * 
 * @author Administrator
 * 
 */
public class FileUtil {

	/**
	 * 读取系统应用目录的数�?
	 * 
	 * @param key
	 * @return
	 */
	public static Serializable readData(String key, Context context) {

		if (!isExistDataFile(key, context))
			return null;

		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(key);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 反序列化失败 - 删除缓存文件
			if (e instanceof InvalidClassException) {
				File data = context.getFileStreamPath(key);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * 缓存数据到系统应用目�?
	 * 
	 * @param key
	 * @param ser
	 * @return
	 */
	public static boolean saveData(String key, Serializable ser, Context context) {

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = context.openFileOutput(key, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 判断系统目录缓存文件是否存在
	 * 
	 * @param key
	 * @param context
	 * @return
	 */
	public static boolean isExistDataFile(String key, Context context) {
		boolean exist = false;
		File data = context.getFileStreamPath(key);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 获取文件�?
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
//		if (StringsUtil.isEmpty(filePath))
		if("".equals(filePath) || null == filePath)
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 创建文件�?
	 * 
	 * @param filepath
	 * @return
	 */
	public static String createFolder(String filepath) {
		File file = new File(filepath);
		if (!file.exists())
			file.mkdirs();

		return file.getPath();
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
}
