package com.moinapp.wuliao.activity;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.util.RegularUtil;

public class EmailRegist_Activity extends Base_Activity {
	
	private M_Application application;
	private EditText mail_et, nickname_et, password_et, repassword_et, phone_et;
	private String email_str, nickname_str, phone_str, password_str1, password_str2, phone_str3;

	Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if (msg.what == 1) {
//				if (msg.arg1 == 1) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map result = null;
							try {
								result = (Map) application.user_Regist(phone_str3, email_str, "abc", password_str1, application.getUuid(), "", "");
							} catch (M_Exception e) {
								e.printStackTrace();
							}
							Message msg1 = Message.obtain();
							double dd = Double.parseDouble(String.valueOf(result.get("result")));
							msg1.what = 2;
							msg1.arg1 = (int)dd;
							msg1.obj = result.get("msg");
							handler.sendMessage(msg1);
						}
					}).start();
					Toast.makeText(EmailRegist_Activity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
					finish();
//				} else {
//					Toast.makeText(EmailRegist_Activity.this, "该邮箱已存在，请重新输入", Toast.LENGTH_SHORT).show();
//					mail_et.requestFocusFromTouch();
//				}
			} else {
				if (msg.arg1 == 1) {
					Toast.makeText(EmailRegist_Activity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(EmailRegist_Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (M_Application) getApplication();
		setContentView(R.layout.emailregist_layout);
		initView();
	}
	
	private void initView() {
		mail_et = (EditText) findViewById(R.id.email);
		nickname_et = (EditText) findViewById(R.id.nickname);
		phone_et = (EditText)findViewById(R.id.phone);
		password_et = (EditText) findViewById(R.id.password1);
		repassword_et = (EditText) findViewById(R.id.password2);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.email_regist:
			email_str = mail_et.getText().toString();
			nickname_str = nickname_et.getText().toString();
			phone_str = phone_et.getText().toString();
			password_str1 = password_et.getText().toString();
			password_str2 = repassword_et.getText().toString();
			phone_str3 = phone_et.getText().toString();
			if (email_str.length() == 0) {
				Toast.makeText(this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
				mail_et.requestFocusFromTouch();
			} else if (nickname_str.length() == 0) {
				Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
				nickname_et.requestFocusFromTouch();
			} else if (phone_str.length() == 0) {
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				phone_et.requestFocusFromTouch();
			} else if (password_str1.length() == 0) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				password_et.requestFocusFromTouch();
			} else if (password_str2.length() == 0) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				repassword_et.requestFocusFromTouch();
			} else if (!RegularUtil.isEmail(email_str)) {
				Toast.makeText(this, "邮箱格式有误，请重新输入", Toast.LENGTH_SHORT).show();
				mail_et.requestFocusFromTouch();
			} else if (!RegularUtil.isCellphone(phone_str)) {
				Toast.makeText(this, "手机格式有误，请重新输入", Toast.LENGTH_SHORT).show();
				phone_et.requestFocusFromTouch();
			} else if (password_str1.length() < 6 || password_str1.length() > 16) {
				Toast.makeText(this, "请输入6-16位密码", Toast.LENGTH_SHORT).show();
				password_et.requestFocusFromTouch();
			} else if (!password_str2.equals(password_str1)) {
				Toast.makeText(this, "两次输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
				repassword_et.requestFocusFromTouch();
			} else {
				collapseSoftInputMethod();
				// 判断该邮箱是否注册过
				new Thread(new Runnable() {
					@Override
					public void run() {
						Map result = null;
						try {
							result = (Map) application.checkEmail(email_str);
						} catch (M_Exception e) {
							e.printStackTrace();
						}
						Message msg = Message.obtain();
//						double dd = Double.parseDouble(String.valueOf(result.get("result")));
						msg.what = 1;
//						msg.arg1 = (int)dd;
//						msg.obj = result.get("msg");
						handler.sendMessage(msg);
						
					}
				}).start();
			}
			break;
		}
	}
	
	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(repassword_et.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		repassword_et.clearFocus();
	}
	
}
