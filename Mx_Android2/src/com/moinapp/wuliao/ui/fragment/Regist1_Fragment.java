package com.moinapp.wuliao.ui.fragment;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.activity.Agreement_Activity;
import com.moinapp.wuliao.activity.Fragment_skip;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.RegularUtil;
import com.moinapp.wuliao.R;
import com.thinkland.sdk.sms.SMSCaptcha;
import com.thinkland.sdk.util.BaseData.ResultCallBack;

public class Regist1_Fragment extends Base_Fragment {

	private M_Application application;
	private Fragment_skip callback;
	private EditText phone_et, code_et, password_et, email_et;
	private LinearLayout reget_code;
	private TextView get_code, countdown_tv, agreement, next;
	private CheckBox agreement_cb;
	private ImageView phone_ok, password_ok, email_ok;
	private String phone_str, code_str, password_str, email_str;
	private SMSCaptcha smsCaptcha;
	private int recLen = 0;
	private int click_space = 10;

	private final int NULL = 0, ERROR = -1, CORRECT = 1;
	private int phone_state = NULL, code_state = NULL, password_state = NULL, email_state = NULL;

	Handler handler = new Handler() {
		public void handleMessage(final Message msg) {
			if (msg.arg1 == 1) { // 手机号不存在，可以注册
				// Toast.makeText(getActivity(), (String) msg.obj,
				// Toast.LENGTH_SHORT).show();
				getSmsCode();
			} else {
				Toast.makeText(getActivity(), R.string.mobileregist_tip1, Toast.LENGTH_SHORT).show();
				phone_et.requestFocusFromTouch();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.regist1_layout, container, false);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (Fragment_skip) activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		application = (M_Application) getActivity().getApplication();
		initView();
	}

	/**
	 * 获取验证码
	 */
	private void getSmsCode() {
		smsCaptcha = SMSCaptcha.getInstance();
		if (recLen == 0) {
			recLen = 60;
			handler.postDelayed(runnable, 1000);
			smsCaptcha.sendCaptcha(phone_str, new ResultCallBack() {
				@Override
				public void onResult(int arg0, String arg1, String arg2) {
					if (arg0 != 0) {
						Toast.makeText(getActivity(), R.string.getsms_fail, Toast.LENGTH_SHORT).show();
					} 
//					else {
//						// 验证码发送成功
//						handler.postDelayed(runnable, 1000);
//					}
				}
			});
		}
	}

	/**
	 * @param code
	 *            验证验证码
	 */
	private void sendSmsCode(String code) {
		smsCaptcha = SMSCaptcha.getInstance();
		smsCaptcha.commitCaptcha(phone_str, code, new ResultCallBack() {

			@Override
			public void onResult(int arg0, String arg1, String arg2) {
				if (arg0 == 0) {
					callback.skip(1, phone_str, password_str, email_str);
				} else {
					Toast.makeText(getActivity(), R.string.getsms_code_error, Toast.LENGTH_SHORT).show();
					code_et.requestFocus();
				}
			}
		});
	}

	private void initView() {
		phone_et = (EditText) getActivity().findViewById(R.id.phone);
		code_et = (EditText) getActivity().findViewById(R.id.code);
		password_et = (EditText) getActivity().findViewById(R.id.password);
		email_et = (EditText) getActivity().findViewById(R.id.email);
		agreement_cb = (CheckBox) getActivity().findViewById(R.id.agreement_cb);
		agreement = (TextView) getActivity().findViewById(R.id.agreement);
		next = (TextView) getActivity().findViewById(R.id.next);

		phone_ok = (ImageView) getActivity().findViewById(R.id.phone_ok);
		get_code = (TextView) getActivity().findViewById(R.id.get_code);
		password_ok = (ImageView) getActivity().findViewById(R.id.password_ok);
		email_ok = (ImageView) getActivity().findViewById(R.id.email_ok);

		reget_code = (LinearLayout) getActivity().findViewById(R.id.reget_code);
		countdown_tv = (TextView) getActivity().findViewById(R.id.countdown);

		phone_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				phone_str = arg0.toString();
				if (phone_str.length() == 0) {
					phone_state = NULL;
					phone_ok.setVisibility(View.INVISIBLE);
				} else if (!RegularUtil.isCellphone(phone_str)) {
					phone_state = ERROR;
					phone_ok.setVisibility(View.INVISIBLE);
				} else {
					phone_state = CORRECT;
					phone_ok.setVisibility(View.VISIBLE);
				}
			}
		});
		code_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				code_str = arg0.toString();
				if (code_str.length() == 0) {
					code_state = NULL;
				} else {
					code_state = ERROR;
				}
			}
		});
		password_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				password_str = arg0.toString();
				if (password_str.length() == 0) {
					password_state = NULL;
					password_ok.setVisibility(View.INVISIBLE);
				} else {
					password_state = CORRECT;
					password_ok.setVisibility(View.VISIBLE);
				}
			}
		});
		email_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				email_str = arg0.toString();
				if (email_str.length() == 0) {
					email_state = NULL;
					email_ok.setVisibility(View.INVISIBLE);
				} else if (!RegularUtil.isEmail(email_str)) {
					email_state = ERROR;
					email_ok.setVisibility(View.INVISIBLE);
				} else {
					email_state = CORRECT;
					email_ok.setVisibility(View.VISIBLE);
				}
			}
		});
		get_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				phone_str = phone_et.getText().toString();
				if (phone_str.length() == 0) {
					Toast.makeText(getActivity(), "手机号不能为空", Toast.LENGTH_SHORT).show();
				} else if (!RegularUtil.isCellphone(phone_str)) {
					Toast.makeText(getActivity(), "手机号格式有误", Toast.LENGTH_SHORT).show();
				} else {
					new Thread(new Runnable() {
						@Override
						public void run() {
							Map result = null;
							try {
								result = (Map) application.checkPhone(phone_str);
							} catch (M_Exception e) {
								e.printStackTrace();
							}
							Message msg = handler.obtainMessage();
							if (result != null) {
								double dd = Double.parseDouble(String.valueOf(result.get("result")));
								msg.arg1 = (int) dd;
								msg.obj = result.get("msg");
								handler.sendMessage(msg);
							}
						}
					}).start();
				}
			}
		});
		reget_code.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getSmsCode();
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (phone_state == CORRECT && code_state != NULL && password_state == CORRECT && email_state == CORRECT && agreement_cb.isChecked()) {
					if (!"".equals(code_str)) {
						sendSmsCode(code_str);
					}
				} else {
					Toast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_SHORT).show();
				}
			}
		});
		agreement.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AppTools.toIntent(getActivity(), Agreement_Activity.class);
			}
		});
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			recLen--;
			countdown_tv.setText("(" + recLen + ")");
			if (recLen > 0) {
				handler.postDelayed(this, 1000);
				get_code.setVisibility(View.INVISIBLE);
				reget_code.setVisibility(View.VISIBLE);
			} else {
				recLen = 0;
				get_code.setVisibility(View.VISIBLE);
				reget_code.setVisibility(View.INVISIBLE);
			}

		}
	};
}
