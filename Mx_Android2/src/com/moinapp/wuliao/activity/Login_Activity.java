package com.moinapp.wuliao.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.model.Login_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.util.AppTools;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.ShareCore;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

public class Login_Activity extends Base_Activity implements PlatformActionListener {

	//-------------------
	private static final int MSG_SMSSDK_CALLBACK = 2;
	private static final int MSG_AUTH_CANCEL = 3;
	private static final int MSG_AUTH_ERROR= 4;
	private static final int MSG_AUTH_COMPLETE = 5;
	//-------------------
	
	private View dialog_view;
	private ImageView retrieve_mobile, retrieve_email;
	private Dialog retrieve_dialog;
	private M_Application application;
	private EditText login_username, login_password;
	private TextView login_submit;
	private String username, password;
	private ArrayList<Platform> platforms;
	
	private boolean username_required = false, password_required = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (M_Application) getApplication();
		setContentView(R.layout.login_layout);

		initDialog();
		initView();
	}

	private void initData() {
		Platform[] tmp = ShareSDK.getPlatformList(this);
		platforms = new ArrayList<Platform>();
		for (Platform p : tmp) {
			String name = p.getName();
			if (ShareCore.isUseClientToShare(name)) {
				continue;
			}
			platforms.add(p);
		}
	}
	
	private void initView() {
		login_username = (EditText) findViewById(R.id.login_username);
		login_password = (EditText) findViewById(R.id.login_password);
		login_submit = (TextView) findViewById(R.id.login_submit);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.regist:
			AppTools.toIntent(this, Regist_Activity.class);
			break;
//		case R.id.login_user_email:
//			AppTools.toIntent(this, EmailRegist_Activity.class);
//			break;
		case R.id.forget_password:
			retrieve_dialog.show();
			break;
		case R.id.login_submit:
			username = login_username.getText().toString();
			password = login_password.getText().toString();
			if (!"".equals(password) && !"".equals(username)) {
				collapseSoftInputMethod();
				login(username, password);
			}
			break;
//		case R.id.login_weibo:
//			//新浪微博
//			Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
//			authorize(sina);
//			break;
		case R.id.login_weixin:
			Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
			authorize(wechat);
			break;
//		case R.id.login_qq:
//			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
//			authorize(qzone);
//			break;
			
		}
	}
	
	private void authorize(Platform plat) {
		if (plat == null) {
			//popupOthers();
			return;
		}
		
		plat.setPlatformActionListener(this);
		//关闭SSO授权
		plat.SSOSetting(true);
		plat.showUser(null);
	}

	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(login_password.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		login_password.clearFocus();
	}

	private void initDialog() {
		dialog_view = LayoutInflater.from(this).inflate(R.layout.login_dialog_layout, null);
		retrieve_mobile = (ImageView) dialog_view.findViewById(R.id.retrieve_by_mobile);
		retrieve_email = (ImageView) dialog_view.findViewById(R.id.retrieve_by_email);
		retrieve_dialog = new Dialog(this, R.style.retrieve_dialog);
		retrieve_dialog.setContentView(dialog_view);
		retrieve_dialog.setCancelable(true);
		retrieve_mobile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {
				AppTools.toIntent(Login_Activity.this, MobileRetrieve_Activity.class);
				retrieve_dialog.dismiss();
			}
		});
		retrieve_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View V) {
				AppTools.toIntent(Login_Activity.this, EmailRetrieve_Activity.class);
				retrieve_dialog.dismiss();
			}
		});
	}

	private void login(final String username, final String password) {
		new Thread() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					Login_Model data = application.user_Login(username, password);
					if (data != null) {
						if (data.getResult().toString().equals("0")) {
							msg.what = -1;
							msg.obj = data.getMsg();
						} else {
							msg.what = 1;
							msg.obj = data.getData();
						}
					}

				} catch (M_Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e.getMessage();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	private void checkToken(final String user_id) {
		new Thread() {

			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					Login_Model data = application.checkToken(user_id);
					if (data != null) {
						if (data.getResult().toString().equals("0")) {
							msg.what = -2;
							msg.obj = data.getMsg();
						} else {
							msg.what = 1;
							msg.obj = data.getData();
						}
					}

				} catch (M_Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e.getMessage();
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -2:
				Toast.makeText(Login_Activity.this, R.string.bing_username_tip, Toast.LENGTH_SHORT).show();
				break;
			case -1:
				Toast.makeText(Login_Activity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Signin_User_Model data = (Signin_User_Model) msg.obj;
				if (data != null) {
					Intent intent = getIntent();
					AppTools.RememberUsernameAndPassword(Login_Activity.this, username, data.getId(), false);
					intent.putExtra(M_Constant.UID, data);
					intent.putExtra("username", username);
					setResult(1, intent);
					finish();
				}
				break;
			case MSG_AUTH_CANCEL: 
				//取消授权
				Toast.makeText(Login_Activity.this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
			break;
			case MSG_AUTH_ERROR: 
				//授权失败
				Toast.makeText(Login_Activity.this, R.string.xinxi_no, Toast.LENGTH_SHORT).show();
			break;
			case MSG_AUTH_COMPLETE:
				//授权成功
				Toast.makeText(Login_Activity.this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
				Object[] objs = (Object[]) msg.obj;
				String platform = (String) objs[0];
				String user_id = (String) objs[1];
				//HashMap<String, Object> res = (HashMap<String, Object>) objs[1];
				saveThirdLoginInfor(platform, user_id);
				/*if (signupListener != null && signupListener.onSignin(platform, res)) {
					SignupPage signupPage = new SignupPage();
					signupPage.setOnLoginListener(signupListener);
					signupPage.setPlatform(platform);
					signupPage.show(activity, null);
				}*/
			break;
		}
		}
	};
	
	private void saveThirdLoginInfor(String platformName, String user_id) {
		if(SinaWeibo.NAME.equals(platformName)) {
			application.setWeibo_token(user_id);
		} else if(Wechat.NAME.equals(platformName)) {
			application.setWeixin_token(user_id);
		} else if(QZone.NAME.equals(platformName)) {
			application.setQq_token(user_id);
		}
		username = user_id;
		checkToken(user_id);
	}

	@Override
	public void onCancel(Platform platform, int action) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_CANCEL);
		}
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// TODO Auto-generated method stub
		if (action == Platform.ACTION_USER_INFOR) {
			Message msg = new Message();
			msg.what = MSG_AUTH_COMPLETE;
			msg.obj = new Object[] {platform.getName(), platform.getDb().getUserId(), arg2};
//			Iterator it = arg2.entrySet().iterator();
//			while(it.hasNext()) {
//				Entry entry = (Entry) it.next();
//				String key = (String) entry.getKey();
//				Object value = entry.getValue();
//			}
			handler.sendMessage(msg);
		}
		
	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		if (action == Platform.ACTION_USER_INFOR) {
			handler.sendEmptyMessage(MSG_AUTH_ERROR);
		}
		t.printStackTrace();
	}

}