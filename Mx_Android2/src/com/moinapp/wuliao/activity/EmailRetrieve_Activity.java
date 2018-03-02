package com.moinapp.wuliao.activity;

import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.RegularUtil;
import com.moinapp.wuliao.util.ToastUtil;

public class EmailRetrieve_Activity extends Base_Activity {

	private M_Application application;
	private EditText email_et, code_et;
	private ImageView email_ok;
	private boolean email_state = false;
	private TextView get_code, countdown_tv;
	private LinearLayout reget_code;
	private String email_str;
	private int click_space = 5;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 0) {	// {"result":0,"msg":"已经存在"}
				new SendEmailCaptcha_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email_str);
			} else {	// {"result":1,"msg":"ok"}
				ToastUtil.makeText(EmailRetrieve_Activity.this, "该邮箱未注册，请重新输入", Toast.LENGTH_SHORT);
				email_et.requestFocusFromTouch();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (M_Application) getApplication();
		setContentView(R.layout.retrieve_password_email);
		initView();
	}
	
	private void initView() {
		email_et = (EditText) findViewById(R.id.email);
		email_ok = (ImageView) findViewById(R.id.email_ok);
		code_et = (EditText) findViewById(R.id.code);
		get_code = (TextView) findViewById(R.id.get_code);
		
		reget_code = (LinearLayout) findViewById(R.id.reget_code);
		countdown_tv = (TextView) findViewById(R.id.countdown);
		
		email_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				email_str = arg0.toString();
				if (email_str.length() == 0) {
					email_state = false;
					email_ok.setVisibility(View.INVISIBLE);
				} else if (!RegularUtil.isEmail(email_str)) {
					email_state = false;
					email_ok.setVisibility(View.INVISIBLE);
				} else {
					email_state = true;
					email_ok.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.get_code:
			click_space = 5;
			email_str = email_et.getText().toString();
			if (!RegularUtil.isEmail(email_str)) {
				ToastUtil.makeText(this, "邮箱格式有误，请重新输入", Toast.LENGTH_SHORT);
				email_et.requestFocus();
			} else {
				get_code.setClickable(false);
				handler.postDelayed(disClick_runnable, 1000);
				new Thread(new Runnable() {
					@Override
					public void run() {
						Map result = null;
						try {
							result = (Map) application.checkEmail(email_str);
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
		case R.id.retrieve_email:
			if (email_state) {
				String code = code_et.getText().toString();
				if (!"".equals(code))
					new CheckEmailCaptcha_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email_str, code);
				else
					Toast.makeText(this, R.string.getsms_code_empty, Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "请输入验证邮箱", Toast.LENGTH_SHORT).show();
				email_et.requestFocus();
			}
			break;
		}
	}
	
	class ResetPassword_Task extends AsyncTask<String, Void, Map> {
		@Override
		protected Map doInBackground(String... params) {
			// TODO Auto-generated method stub
			return application.resetPassword("", params[0]);
		}
		
		@Override
		protected void onPostExecute(Map result) {
			if(result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					// 打开成功
					Toast.makeText(EmailRetrieve_Activity.this, "重置密码已发送至邮箱，请注意查收", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(EmailRetrieve_Activity.this, "重置失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	class SendEmailCaptcha_Task extends AsyncTask<String, Void, Map> {
		@Override
		protected Map doInBackground(String... params) {
			return application.sendEmailCaptcha(params[0]);
		}
		
		@Override
		protected void onPostExecute(Map result) {
			if(result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					// 打开成功
					ToastUtil.makeText(EmailRetrieve_Activity.this, "验证码已发送邮箱，请注意查收", Toast.LENGTH_SHORT);
				} else {
					ToastUtil.makeText(EmailRetrieve_Activity.this, R.string.getsms_fail, Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	class CheckEmailCaptcha_Task extends AsyncTask<String, Void, Map> {
		@Override
		protected Map doInBackground(String... params) {
			return application.checkEmailCaptcha(params[0], params[1]);
		}
		
		@Override
		protected void onPostExecute(Map result) {
			if(result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					// 打开成功
					new ResetPassword_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email_str);
				} else {
					ToastUtil.makeText(EmailRetrieve_Activity.this, R.string.getsms_code_error, Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(disClick_runnable);
	};
	
	Runnable disClick_runnable = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			click_space--;
			if (click_space == 0) {
				get_code.setClickable(true);
				handler.removeCallbacks(this);
			}
			if (click_space > 0) {
				handler.postDelayed(this, 1000);
			}
		}
	};
}
