package com.moinapp.wuliao.activity;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;

public class Base_Activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		MobclickAgent.setDebugMode(true);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}
}
