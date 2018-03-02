package com.moinapp.wuliao.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.text.TextUtils;
import android.widget.Toast;


public class UnZipUtil {

	private String  srcPath;
	private String  destPath;
	
	public void setUnZipListener(String path, String unpath, UnZipListener listener)
	{
		srcPath = path;
		if(TextUtils.isEmpty(path))
		{
//			Toast.makeText(this, "请指定一个zip文件路径", Toast.LENGTH_SHORT).show();
			return;
		}
		destPath = unpath;
		if(TextUtils.isEmpty(destPath))
		{
			destPath = srcPath;
		}
		destPath.replace(".zip", "");//去掉后面的zip，以便组成解压的目录。
		
		File srcFile = new File(srcPath);
		if(!srcFile.exists())
		{
//			Toast.makeText(this, "指定的压缩文件不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		
		File destFile = new File(destPath);
		if(!destFile.exists())
		{
			destFile.mkdirs();
		}
		if(destFile.isFile())
		{
//			Toast.makeText(this, "重新输入解压路径，已经有一个相同的文件了，而不是一个目录", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ZipUtil zipp = new ZipUtil(2049);
		zipp.unZip(srcPath,destPath);
		listener.isComplete(true);
//		Toast.makeText(this, "解压完成", Toast.LENGTH_SHORT).show();
	}
}
