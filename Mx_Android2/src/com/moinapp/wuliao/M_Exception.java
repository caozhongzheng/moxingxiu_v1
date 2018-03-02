package com.moinapp.wuliao;

import android.content.Context;


public class M_Exception extends Exception {

	/**
	 * 应用程序异常处理和捕获类
	 * 
	 * @author Administrator
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int HTTP_ERROR = 1;
	private static final int NETWORK_ERROR = 2;
	private static final int DATA_PARSER_ERROR = 3;
	private static final int FILE_ERROT = 4;

	private int type;
	private int code;

	public M_Exception(int type, int code, Exception exception) {
		super(exception);

		this.type = type;
		this.code = code;
	}

	/**
	 * 异常提示
	 * @param context
	 */
	public void exception_Prompt(Context context) {
		switch (type) {
		case HTTP_ERROR:
//			String msg = String.format(context.getResources().getString(R.string.http_error), code);
//			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			break;
		case NETWORK_ERROR:
//			Toast.makeText(context, R.string.network_connection_fails, Toast.LENGTH_SHORT).show();
			break;
		case DATA_PARSER_ERROR:
//			Toast.makeText(context, R.string.data_parser_error, Toast.LENGTH_SHORT).show();
			break;
		case FILE_ERROT:
//			Toast.makeText(context, R.string.file_error, Toast.LENGTH_SHORT).show();
			break;
		}
	}

	/**
	 * http异常
	 * @param code
	 * @return
	 */
	public static M_Exception http(int code) {
		return new M_Exception(HTTP_ERROR, code, null);
	}

	/**
	 * 网络异常
	 * @param e
	 * @return
	 */
	public static M_Exception network(Exception e) {
		return new M_Exception(NETWORK_ERROR, 0, e);
	}

	/**
	 * 数据解析异常
	 * @param e
	 * @return
	 */
	public static M_Exception dataParser(Exception e) {
		return new M_Exception(DATA_PARSER_ERROR, 0, e);
	}

	/**
	 * 文件异常
	 * @param e
	 * @return
	 */
	public static M_Exception file(Exception e) {
		return new M_Exception(FILE_ERROT, 0, e);
	}
}
