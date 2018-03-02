package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.MyViewPagerAdapter;
import com.moinapp.wuliao.ui.fragment.MessageReceived_Fragment;
import com.moinapp.wuliao.ui.fragment.MessageSent_Fragment;
import com.moinapp.wuliao.util.AppTools;


public class MyMessages_Activity extends Base_FragmentActivity {

	private M_Application application;
	private ViewPager message_vp;
	private MessageReceived_Fragment fragment1;
	private MessageSent_Fragment fragment2;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	private int page1 = 1;
	private int page2 = 1;
	private ImageView tab_cursor;
	private TextView message_received, message_sent;
	private int current_item = 1;
	private TranslateAnimation translateAnimation;
	private AnimationSet animationSet;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mymessages_layout);
		initData();
		initView();
	}
	
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		switch (current_item) {
//		case 1:
//			adapter.setType(getResources().getString(R.string.gift_from));
//			getReceiveHistoryMessage();
//			break;
//		case 2:
//			adapter1.setType(getResources().getString(R.string.gift_to));
//			getSendHistoryMessage(0);
//			break;
//		}
//	}
	
	private void initData() {
		application = (M_Application) getApplication();
	}
	
	private void initView() {
		message_received = (TextView) findViewById(R.id.message_received);
		message_sent = (TextView) findViewById(R.id.message_sent);
		tab_cursor = (ImageView) findViewById(R.id.message_tab_cursor);
		message_vp = (ViewPager) findViewById(R.id.message_viewpager);
		
		fragment1 = new MessageReceived_Fragment();
		fragment2 = new MessageSent_Fragment();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		message_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));
	}

	public void onClick(View v) {
		animationSet = new AnimationSet(true);
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			Bundle bundle = new Bundle();
			bundle.putString("from", "message");
			AppTools.toIntent(this, bundle, SelectFriend_Activity.class);
			break;
		case R.id.message_received:
			if (current_item != 1) {
				message_received.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				message_sent.setTextColor(getResources().getColor(R.color.title_color));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 1;
				message_vp.setCurrentItem(0);
			}
			break;
		case R.id.message_sent:
			if (current_item != 2) {
				message_sent.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				message_received.setTextColor(getResources().getColor(R.color.title_color));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 2;
				message_vp.setCurrentItem(1);
			}
			break;
		case R.id.gift_loadmore:
			switch (current_item) {
			case 1:
				break;
			case 2:
				page2++;
				fragment2.getSendHistoryMessage(page2, true);
				break;
			}
			break;
		}
	}
}
