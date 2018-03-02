package com.moinapp.wuliao.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.common.UpdateManager;
import com.moinapp.wuliao.model.Version_Model;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.ToastUtil;

public class Setting_Activity extends Base_Activity {
	private M_Application application;
	private String uid;
	private Dialog version_dialog;
	private Dialog update_dialog;
	private Dialog loginOut_dialog;
	private Version_Model appinfo;
	private TextView current_version;
	private String versionName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		application = (M_Application) getApplication();
		setContentView(R.layout.setting_layout);
		initData();
		initDialog();
		initUpdateDialog();
		initLoginOutDialog();
		
		versionName = AppTools.getVersionName(this);
	}
	
	private void initData() {
		application = (M_Application) getApplication();
		uid = application.getUid();
	}
	
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.about_item:
			AppTools.toIntent(this, About_Activity.class);
			break;
		case R.id.update_item:
			version_dialog.show();
			getAppUpdateData();
			break;
		case R.id.feedback_item:
			AppTools.toIntent(this, Feedback_Activity.class);
			break;
		case R.id.version_sure:
			version_dialog.dismiss();
			break;
		case R.id.login_out_item:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				loginOut_dialog.show();
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.loginout_sure:
			application.login_out();
			loginOut_dialog.dismiss();
			setResult(4);
			finish();
			break;
		case R.id.loginout_cancel:
			loginOut_dialog.dismiss();
			break;
		case R.id.alter_update_sure:
			UpdateManager.getUpdateManager().checkAppUpdate(Setting_Activity.this, appinfo.getApkFileUrl(), appinfo.getVersionNameAndroid(), appinfo.getAppname());
			break;
		case R.id.alter_update_cancel:
			update_dialog.dismiss();
			break;
		}
	}
	
	private void initDialog() {
		View version_view = LayoutInflater.from(this).inflate(R.layout.version_dialog, null);
		current_version = (TextView)version_view.findViewById(R.id.current_version);
		version_dialog = new Dialog(this, R.style.retrieve_dialog);
		version_dialog.setContentView(version_view);
		version_dialog.setCancelable(true);
	}
	
	private void initUpdateDialog() {
		update_dialog = new Dialog(this, R.style.retrieve_dialog);
		View version_view = LayoutInflater.from(this).inflate(R.layout.alter_update, null);
		TextView update_version = (TextView)version_view.findViewById(R.id.current_version);
		update_version.setText(versionName);
		update_dialog.setContentView(version_view);
		update_dialog.setCancelable(true);
	}
	
	private void initLoginOutDialog() {
		View version_view = LayoutInflater.from(this).inflate(R.layout.alter_loginout, null);
		loginOut_dialog = new Dialog(this, R.style.retrieve_dialog);
		loginOut_dialog.setContentView(version_view);
		loginOut_dialog.setCancelable(true);
	}
	
	private void getAppUpdateData() {
		final Handler hander = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					appinfo = (Version_Model) msg.obj;
					update_dialog.show();
					break;
				case -1:
					((M_Exception) msg.obj).exception_Prompt(Setting_Activity.this);
					break;
				case -2:
					Toast.makeText(Setting_Activity.this, R.string.network_connection_fails, Toast.LENGTH_SHORT).show();
					break;
				case 3:
					current_version.setText(versionName);
					version_dialog.show();
//					Toast.makeText(Setting_Activity.this, R.string.app_version, Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};

		new Thread() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					int currentversion = AppTools.getVersionCode(Setting_Activity.this);
					Version_Model appinfo = application.getVersion();
						if (appinfo.getVersionCodeAndroid() > currentversion) {
							msg.what = 1;
							msg.obj = appinfo;
						} else {
							msg.what = 3;
						}
				} catch (Exception e) {
					msg.what = -2;
				}
				hander.sendMessage(msg);
			}
		}.start();
	}
	
	
}
