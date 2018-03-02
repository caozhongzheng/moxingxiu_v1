package com.moinapp.wuliao.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast操作类
 * 
 * @author Administrator
 * 
 */
public class ToastUtil {

	private static Toast tip;

	/**
	 * @param paramContext
	 * @param paramInt1
	 * @param paramInt2
	 * @return
	 */
	public static void makeText(Context paramContext, int paramInt1, int paramInt2) {

		if (tip != null) {
			tip.setText(paramInt1);
			tip.setDuration(paramInt2);
			tip.show();
		} else {
			tip = Toast.makeText(paramContext, paramInt1, paramInt2);
			tip.show();
		}
	}

	/**
	 * @param paramContext
	 * @param paramCharSequence
	 * @param paramInt
	 * @return
	 */
	public static void makeText(Context paramContext, CharSequence paramCharSequence, int paramInt) {
		if (tip != null) {
			tip.setText(paramCharSequence);
			tip.setDuration(paramInt);
			tip.show();
		} else {
			tip = Toast.makeText(paramContext, paramCharSequence, paramInt);
			tip.show();
		}
	}

}
