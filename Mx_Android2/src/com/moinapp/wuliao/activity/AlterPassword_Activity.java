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

public class AlterPassword_Activity extends Base_Activity {
	
	private M_Application application;
	private EditText old_password_et, new_password_et, confirm_password_et;
	private String old_password_str, new_password_str, confirm_password_str;
	private String username;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				Toast.makeText(AlterPassword_Activity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(AlterPassword_Activity.this, "修改失败", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (M_Application) getApplication();
		setContentView(R.layout.alter_password);
		initData();
		initView();
	}
	
	private void initData() {
		username = getIntent().getStringExtra("username");
	}
	
	private void initView() {
		old_password_et = (EditText) findViewById(R.id.old_password);
		new_password_et = (EditText) findViewById(R.id.new_password);
		confirm_password_et = (EditText) findViewById(R.id.confirm_password);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.password_alter_submit:
			old_password_str = old_password_et.getText().toString();
			new_password_str = new_password_et.getText().toString();
			confirm_password_str = confirm_password_et.getText().toString();
			if (old_password_str.length() == 0) {
				Toast.makeText(this, "当前不能为空", Toast.LENGTH_SHORT).show();
				old_password_et.requestFocusFromTouch();
			} else if (new_password_str.length() == 0) {
				Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
				new_password_et.requestFocusFromTouch();
			} else if (confirm_password_str.length() == 0) {
				Toast.makeText(this, "确认密码不能为空", Toast.LENGTH_SHORT).show();
				confirm_password_et.requestFocusFromTouch();
//			} else if (!RegularUtil.isEmail(email_str)) {
//				Toast.makeText(this, "邮箱格式有误，请重新输入", Toast.LENGTH_SHORT).show();
//				mail_et.requestFocusFromTouch();
			} else if (!new_password_str.equals(confirm_password_str)) {
				Toast.makeText(this, "两次输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
				confirm_password_et.requestFocusFromTouch();
			} else {
				collapseSoftInputMethod();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Map result = null;
						try {
							result = (Map) application.user_ChangePassword(username, old_password_str, new_password_str);
						} catch (M_Exception e) {
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						double dd = Double.parseDouble(String.valueOf(result.get("result")));
						msg.arg1 = (int)dd;
						msg.obj = result.get("msg");
						handler.sendMessage(msg);
					}
				}).start();
			}
			break;
		}
	}
	
	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(confirm_password_et.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		confirm_password_et.clearFocus();
	}
	
}
