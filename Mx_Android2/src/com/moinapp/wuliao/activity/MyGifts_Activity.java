package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.MyViewPagerAdapter;
import com.moinapp.wuliao.model.OpenGift_List_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.ui.fragment.GiftReceived_Fragment;
import com.moinapp.wuliao.ui.fragment.GiftReceived_Fragment.Fragment_callback;
import com.moinapp.wuliao.ui.fragment.GiftSent_Fragment;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyGifts_Activity extends Base_FragmentActivity implements Fragment_callback {

	private M_Application application;
	private ImageLoader imageLoader;
	private ViewPager gift_vp;
	private GiftReceived_Fragment fragment1;
	private GiftSent_Fragment fragment2;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	private int page1 = 1;
	private int page2 = 1;
	private Signin_User_Model login_data;

	private ImageView tab_cursor, gift_dialog_icon;
	private TextView gift_received, gift_sent, gift_dialog_name;
	private int current_item = 1;
	private TranslateAnimation translateAnimation;
	private AnimationSet animationSet;
	private Dialog gift_dialog;
	private OpenGift_List_Model dialog_gift_data;
	public static boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.mygifts_layout);
		initData();
		initView();
		initDialog();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if (fragment1 != null)
				fragment1.getGiftReceived(1, false);
			break;
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		switch (current_item) {
		case 1:
			fragment1.getGiftReceived(page1, false);
			break;
		case 2:
			fragment2.getGiftSent(page2, false);
			break;
		}
	}

	private void initDialog() {
		View version_view = LayoutInflater.from(this).inflate(R.layout.gift_dialog_layout, null);

		gift_dialog_icon = (ImageView) version_view.findViewById(R.id.gift_dialog_icon);
		gift_dialog_name = (TextView) version_view.findViewById(R.id.gift_dialog_name);

		gift_dialog = new Dialog(this, R.style.retrieve_dialog);
		gift_dialog.setContentView(version_view);
		gift_dialog.setCancelable(true);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = getIntent().getExtras();
		login_data = (Signin_User_Model) bundle.getSerializable(M_Constant.UID);
	}

	private void initView() {
		gift_received = (TextView) findViewById(R.id.gift_recieved);
		gift_sent = (TextView) findViewById(R.id.gift_sent);
		tab_cursor = (ImageView) findViewById(R.id.gift_tab_cursor);
		gift_vp = (ViewPager) findViewById(R.id.gift_viewpager);

		fragment1 = new GiftReceived_Fragment();
		fragment2 = new GiftSent_Fragment();
		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		gift_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager(), fragmentList));
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		page1 = 1;
		page2 = 1;
	}

	public void onClick(View v) {
		animationSet = new AnimationSet(true);
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.gift_dialog_open:
			// 打开礼包
			Bundle bundle1 = new Bundle();
			bundle1.putSerializable("gift", dialog_gift_data);
			AppTools.toIntent(this, bundle1, OpenGift_Activity.class);

			gift_dialog.dismiss();
			break;
		case R.id.gift_dialog_close:
			gift_dialog.dismiss();
			break;
		case R.id.send_friend_gift:
			Bundle bundle = new Bundle();
			bundle.putString("from", "gift");
			AppTools.toIntent(this, bundle, SelectFriend_Activity.class);
			break;
		case R.id.gift_recieved:
			if (current_item != 1) {
				gift_received.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				gift_sent.setTextColor(getResources().getColor(R.color.title_color));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
						0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 1;
				gift_vp.setCurrentItem(0);
			}
			break;
		case R.id.gift_sent:
			if (current_item != 2) {
				gift_sent.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				gift_received.setTextColor(getResources().getColor(R.color.title_color));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
						0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 2;
				gift_vp.setCurrentItem(1);
			}
			break;
		case R.id.gift_loadmore:
			switch (current_item) {
			case 1:
				page1++;
				fragment1.getGiftReceived(page1, true);
				break;
			case 2:
				page2++;
				fragment2.getGiftSent(page2, true);
				break;
			}
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		isOpen = false;
	}

	@Override
	public void callback(OpenGift_List_Model data) {
		if(!isOpen)
		gift_dialog.show();
		
		dialog_gift_data = data;
		imageLoader.displayImage(data.getAvatar(), gift_dialog_icon, BitmapUtil.getImageLoaderOption());
		gift_dialog_name.setText(data.getNickname());
	}

}
