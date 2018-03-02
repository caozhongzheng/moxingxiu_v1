package com.moinapp.wuliao.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;

public class AlterEmail_Activity extends Base_Activity {
	
	private M_Application application;
	private EditText old_email_et, new_email_et;
	private String old_email_str, new_email_str;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				Toast.makeText(AlterEmail_Activity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(AlterEmail_Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (M_Application) getApplication();
		setContentView(R.layout.alter_email);
		initView();
	}
	
	private void initView() {
		old_email_et = (EditText) findViewById(R.id.old_email);
		new_email_et = (EditText) findViewById(R.id.new_email);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.email_alter_submit:
			old_email_str = old_email_et.getText().toString();
			new_email_str = new_email_et.getText().toString();
			if (old_email_str.length() == 0) {
				Toast.makeText(this, "当前邮箱不能为空", Toast.LENGTH_SHORT).show();
				old_email_et.requestFocusFromTouch();
			} else if (new_email_str.length() == 0) {
				Toast.makeText(this, "新邮箱不能为空", Toast.LENGTH_SHORT).show();
				new_email_et.requestFocusFromTouch();
			} else {
				collapseSoftInputMethod();
			}
			break;
		}
	}
	
	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(new_email_et.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		new_email_et.clearFocus();
	}
	
}
