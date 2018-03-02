package com.moinapp.wuliao.activity;


import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.model.OpenGift_List_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class OpenGift_Activity extends Base_Activity {

	private M_Application application;
	private ImageLoader imageLoader;
	private OpenGift_List_Model open_gift;
	private ImageView user_avatar;
	private TextView tv_nickname, tv_value;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gift_open_layout);
		initData();
		initView();
		
		new OpenGift_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,  open_gift.getGifttype(), open_gift.getId());
		getUnOpenGiftNum();
	}
	
	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = getIntent().getExtras();
		open_gift = (OpenGift_List_Model) bundle.getSerializable("gift");
	}
	
	private void initView() {
		user_avatar = (ImageView) findViewById(R.id.user_avatar);
		tv_nickname = (TextView) findViewById(R.id.received_nickname);
		tv_value = (TextView) findViewById(R.id.received_value);
		imageLoader.displayImage(open_gift.getAvatar(), user_avatar, BitmapUtil.getImageLoaderOption());
		tv_nickname.setText(open_gift.getNickname());
		tv_value.setText(open_gift.getAbout_value());
		
	}
	
	private void getUnOpenGiftNum() {
		new UnOpenGiftNum_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "uid");
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.user_avatar:
			Bundle bundle = new Bundle();
			bundle.putString("fid", open_gift.getFrom());
			bundle.putString("friend_avatar", open_gift.getAvatar());
			bundle.putString("friend_nickname", open_gift.getNickname());
			bundle.putBoolean("isFriend", true);
			bundle.putString("from", "opengift");
			AppTools.toIntent(this, bundle, UserData_Activity.class);
			break;
		case R.id.go_wealth:
//			AppTools.toIntent(this, WealthCenter_Activity.class);
			break;
		case R.id.return_gift:
			Bundle bundle1 = new Bundle();
			bundle1.putString("fid", open_gift.getFrom());
			bundle1.putString("friend_avatar", open_gift.getAvatar());
			bundle1.putString("friend_nickname", open_gift.getNickname());
			AppTools.toIntent(this, bundle1, SendGifts_Activity.class);
			break;
		case R.id.send_other_friend:
			Bundle bundle2 = new Bundle();
			bundle2.putString("from", "gift");
			AppTools.toIntent(this, bundle2, SelectFriend_Activity.class);
			break;
		}
	}
	
	private class OpenGift_Task extends AsyncTask<Object, Void, Map> {
		private boolean isMore;
		@Override
		protected Map doInBackground(Object... params) {
			return application.openGift(application.getUid(), (String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if(result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					// 打开成功
//					ToastUtil.makeText(OpenGift_Activity.this, "打开成功", Toast.LENGTH_SHORT);
					setResult(Activity.RESULT_OK);
				} else {
					// 打开失败
//					ToastUtil.makeText(OpenGift_Activity.this, "打开失败", Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	private class UnOpenGiftNum_Task extends AsyncTask<Object, Void, Integer> {
		@Override
		protected Integer doInBackground(Object... params) {
			return application.getUnOpenGiftNum(application.getUid());
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result >= 0)
				application.setUnOpenGiftNum(result);
		}
	}
}
