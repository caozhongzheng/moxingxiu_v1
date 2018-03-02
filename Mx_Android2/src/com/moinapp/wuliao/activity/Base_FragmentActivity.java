package com.moinapp.wuliao.activity;

import android.support.v4.app.FragmentActivity;

import com.umeng.analytics.MobclickAgent;

public class Base_FragmentActivity extends FragmentActivity {
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}
}
