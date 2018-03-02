package com.moinapp.wuliao.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.zyh.volleybox.FileUtil;


/**
 * 图形操作类
 * 
 * @author Administrator
 * 
 */
public class BitmapUtil {

	
	
	public static String BITMAP_CACHE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/KW/CACHE";
	public static String IMAGELOAD_CACHE = "KW/CACHE";

	static {
		if (AppTools.existsSDCARD()) {
			BITMAP_CACHE = FileUtil.createFolder(BITMAP_CACHE);
		}
	}
	
	public static DisplayImageOptions getImageLoaderOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.default_icon) //设置图片在下载期间显示的图片  
		.showImageForEmptyUri(R.drawable.default_icon)
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.cacheInMemory(false)// 是否緩存都內存中  
        .cacheOnDisk(true)// 是否緩存到sd卡上
        .bitmapConfig(Bitmap.Config.RGB_565)
		.build();//设置图片Uri为空或是错误的时候显示的图片  
		return options;
	}
	
	public static DisplayImageOptions getImageLoaderOption1() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.cacheInMemory(false)// 是否緩存都內存中  
		.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.bitmapConfig(Bitmap.Config.RGB_565)
        .cacheOnDisk(true)// 是否緩存到sd卡上
		.build();//设置图片Uri为空或是错误的时候显示的图片  
		return options;
	}

	/**
	 * Bitmap圆角转化
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap, int roundPx) {

		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 通过Url获取Bitmap
	 * 
	 * @param context
	 * @param url
	 * @return
	 * @throws TswException
	 */
	public static Bitmap getBitmapByUrl(Context context, String url) throws M_Exception {
		Bitmap bitmap = null;
		InputStream inputstream = null;
		try{
		 inputstream = HttpUtil.httpGetInputStream(context, url);
          bitmap = BitmapFactory.decodeStream(inputstream);
		}catch(Exception e){
			
		}

		try {
			if (null != inputstream) {
				inputstream.close();
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 通过绝对地址获取Bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(BITMAP_CACHE + File.separator + filePath, null);
	}

	/**
	 * 从绝对地址获取bitmap
	 * 
	 * @param filePath
	 * @param opts
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {

		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeFile(filePath, opts);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	/**
	 * 缓存图片到本地sdcard
	 * 
	 * @param context
	 * @param bitmap
	 * @param filePath
	 * @param quality
	 * @throws IOException
	 */
	public static void saveBitmapToSDCard(Context context, Bitmap bitmap, String filePath, int quality) throws IOException {
		if (bitmap != null) {
			String md5_path = MD5EncodeUtil.MD5Encode(filePath.getBytes());
			File file = new File(BITMAP_CACHE);
			if (!file.exists())
				file.mkdirs();
			FileOutputStream fos = new FileOutputStream(BITMAP_CACHE + File.separator + md5_path);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
		}
	}
	
	public static String saveBitmapToSDCardString(Context context, Bitmap bitmap, String filePath, int quality) throws IOException {
		if (bitmap != null) {
			String md5_path = MD5EncodeUtil.MD5Encode(filePath.getBytes());
			File file = new File(BITMAP_CACHE);
			if (!file.exists())
				file.mkdirs();
			FileOutputStream fos = new FileOutputStream(BITMAP_CACHE + File.separator + md5_path);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, quality, stream);
			byte[] bytes = stream.toByteArray();
			fos.write(bytes);
			fos.close();
			
			return BITMAP_CACHE + File.separator + md5_path;
		} else {
			return "";
		}
		
	}

	/**
	 * 放大缩小图片
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		Bitmap newbmp = null;
		if (bitmap != null) {
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			Matrix matrix = new Matrix();
			float scaleWidht = ((float) w / width);
			float scaleHeight = ((float) h / height);
			matrix.postScale(scaleWidht, scaleHeight);
			newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
		return newbmp;
	}

	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * Bitma转תDrawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

}
