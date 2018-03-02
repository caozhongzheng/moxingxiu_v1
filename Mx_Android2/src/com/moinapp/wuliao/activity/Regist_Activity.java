package com.moinapp.wuliao.activity;

import java.util.ArrayList;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.ui.fragment.Regist1_Fragment;
import com.moinapp.wuliao.ui.fragment.Regist2_Fragment;

public class Regist_Activity extends Base_FragmentActivity implements Fragment_skip {
	
	private M_Application application;
	private ViewPager viewPager;
	private ArrayList<Fragment> fragments;
	private String phone, password, email, nickname, gender;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				Toast.makeText(Regist_Activity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
				finish();
			} else {
				Toast.makeText(Regist_Activity.this, "注册失败", Toast.LENGTH_SHORT).show();
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mobileregist_layout);
		application = (M_Application) getApplication();
		initViewPager();
	}
	
	public void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		fragments = new ArrayList<Fragment>();
		Fragment fragment1 = new Regist1_Fragment();
		Fragment fragment2 = new Regist2_Fragment();
		fragments.add(fragment1);
		fragments.add(fragment2);
		
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return fragments.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return fragments.get(arg0);
			}
		});
		
		viewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			switch (viewPager.getCurrentItem()) {
			case 0:
				finish();
				break;
			case 1:
				viewPager.setCurrentItem(0);
				break;
			case 2:
				viewPager.setCurrentItem(1);
				break;
			}
		}
	}

	@Override
	public void skip(int position, String... params) {
		if (position == 1) {
			viewPager.setCurrentItem(position);
			phone = params[0];
			password = params[1];
			email = params[2];
		} else if (position == 2) {
			viewPager.setCurrentItem(position);
			nickname = params[0];
			gender = params[1];
			new Thread(new Runnable() {
				@Override
				public void run() {
					Map result = null;
					try {
						result = (Map) application.user_Regist(phone, email, "", password, application.getUuid(), nickname, gender, "", "");
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
	}
	
}
