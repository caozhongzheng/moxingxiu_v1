package com.moinapp.wuliao.activity;


import android.os.Bundle;
import android.view.View;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;

public class About_Activity extends Base_Activity {
	private M_Application application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about_activity_layout);
		application = (M_Application) getApplication();
		
		initData();
		initView();
	}
	
	private void initData() {
		
	}
	
	private void initView() {
		
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		}
	}
}
