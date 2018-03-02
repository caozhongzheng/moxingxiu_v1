package com.moinapp.wuliao.ui.fragment;

import android.support.v4.app.ListFragment;

import com.umeng.analytics.MobclickAgent;

public class Base_ListFragment extends ListFragment {

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName()); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}
}
