package com.moinapp.wuliao.activity;

import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.model.UserInformation_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserData_Activity extends Base_Activity {
	private M_Application application;
	private ImageLoader imageLoader;
	private Friend_Content_Model friend_data;
	private String fid, friend_avatar, friend_nickname;
	private boolean isFriend = false;
	private TextView state_adding_tv;
	private LinearLayout detail_ll;
	private TextView like_stars, own_mobi, regist_time;
	private ImageView friend_state;
	private TextView tip, sendMessage, sendGift;
	private ImageView userdata_avatar, userdata_gender;
	private TextView userdata_nickname, userdata_address;
	private Dialog delFriend_dialog;
	private boolean isDelete = false;	// 判断是否已经删除了该好友
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userdata_layout);
		initData();
		initView();
		new UserData_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fid);
	}
	
	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = getIntent().getExtras();
		if (bundle.getString("from").equals("myfriend")) {
			friend_data = (Friend_Content_Model) bundle.getSerializable("friend");
			fid = friend_data.getId();
			friend_avatar = friend_data.getAvatar();
			friend_nickname = friend_data.getNickname();
			isFriend = true;
		} else {
			fid = bundle.getString("fid");
			friend_avatar = bundle.getString("friend_avatar");
			friend_nickname = bundle.getString("friend_nickname");
			isFriend = (boolean) bundle.getBoolean("isFriend");
		}
	}
	
	private void initView() {
		userdata_avatar = (ImageView) findViewById(R.id.userdata_avatar);
		userdata_nickname = (TextView) findViewById(R.id.userdata_nickname);
		userdata_gender = (ImageView) findViewById(R.id.userdata_gender);
		userdata_address = (TextView) findViewById(R.id.userdata_address);
		state_adding_tv = (TextView) findViewById(R.id.state_adding);
		friend_state = (ImageView) findViewById(R.id.friend_state);
		detail_ll = (LinearLayout) findViewById(R.id.detail_ll);
		like_stars = (TextView) findViewById(R.id.like_stars);
		own_mobi = (TextView) findViewById(R.id.own_mobi);
		regist_time = (TextView) findViewById(R.id.regist_time);
		sendMessage = (TextView) findViewById(R.id.send_message);
		sendGift = (TextView) findViewById(R.id.send_gift);
		tip = (TextView) findViewById(R.id.tip);
		
		if (application.getUid().equals(fid)) {
			friend_state.setVisibility(View.INVISIBLE);
			detail_ll.setVisibility(View.VISIBLE);
			tip.setVisibility(View.INVISIBLE);
			sendMessage.setClickable(false);
			sendGift.setClickable(false);
		} else {
			if (isFriend) {
				friend_state.setBackgroundResource(R.drawable.userdata_isfriend);
				detail_ll.setVisibility(View.VISIBLE);
				tip.setVisibility(View.INVISIBLE);
			} else {
				friend_state.setBackgroundResource(R.drawable.userdata_add_friend);
				detail_ll.setVisibility(View.INVISIBLE);
				tip.setVisibility(View.VISIBLE);
			}
		}
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			if (isDelete) {
				Intent i = new Intent();
				i.putExtra("isDelete", isDelete);
				setResult(Activity.RESULT_OK, i);
				finish();
			} else {
				finish();
			}
			break;
		case R.id.send_message:
			Bundle bundle1 = new Bundle();
			bundle1.putString("fid", fid);
			bundle1.putString("friend_avatar", friend_avatar);
			AppTools.toIntent(this, bundle1, Chatting_Activity.class);
			break;
		case R.id.send_gift:
			Bundle bundle2 = new Bundle();
			bundle2.putString("fid", fid);
			bundle2.putString("friend_avatar", friend_avatar);
			bundle2.putString("friend_nickname", friend_nickname);
			AppTools.toIntent(this, bundle2, SendGifts_Activity.class);
			break;
		case R.id.friend_state:
			if (isFriend) {
				initDelFriendDialog();
			} else {
				new AddFriend_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, application.getUid(), fid, "");
			}
			break;
		case R.id.delete_friend_cancel:
			delFriend_dialog.dismiss();
			break;
		case R.id.delete_friend_sure:
			new DelFriend_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, application.getUid(), fid);
			break;
		}
	}
	
	private void initDelFriendDialog() {
		View delete_friend_view = LayoutInflater.from(this).inflate(R.layout.delete_friend_dialog, null);
		delFriend_dialog = new Dialog(this, R.style.retrieve_dialog);
		delFriend_dialog.setContentView(delete_friend_view);
		delFriend_dialog.setCancelable(true);
		delFriend_dialog.show();
	}
	
	private class UserData_Task extends AsyncTask<Object, Void, UserInformation_Model> {

		@Override
		protected UserInformation_Model doInBackground(Object... params) {
			return application.queryUserInformation(true, (String) params[0]);
		}

		@Override
		protected void onPostExecute(UserInformation_Model result) {
			if (result != null && result.getId() != null) {
				imageLoader.displayImage(result.getAvatar(), userdata_avatar, BitmapUtil.getImageLoaderOption());
				userdata_nickname.setText(result.getNickname());
				if (result.getSex().equals("男")) {
					userdata_gender.setBackgroundResource(R.drawable.mystar_sex_man);
				} else if (result.getSex().equals("女")) {
					userdata_gender.setBackgroundResource(R.drawable.mystar_sex_women);
				} else {
					
				}
				userdata_address.setText(result.getAddr());
				like_stars.setText(result.getLikestar());
				own_mobi.setText(result.getIntegral());
				Long timestamp = Long.parseLong(result.getCreate_time());	
				regist_time.setText(M_Constant.sdf_custom.format(new Date(timestamp * 1000)));
			}
		}
	}
	
	private class DelFriend_Task extends AsyncTask<Object, Void, Map> {
		@Override
		protected Map doInBackground(Object... params) {
			return application.deleteFriend((String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					Toast.makeText(UserData_Activity.this, "删除好友成功！", Toast.LENGTH_SHORT).show();
					delFriend_dialog.dismiss();
					friend_state.setBackgroundResource(R.drawable.userdata_add_friend);
					detail_ll.setVisibility(View.INVISIBLE);
					tip.setVisibility(View.VISIBLE);
					isFriend = false;
					isDelete = true;
				} else {
					Toast.makeText(UserData_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT).show();
				}
			}

		}
	}
	
	public class AddFriend_Task extends AsyncTask<Object, Void, Map> {
		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
				try {
					data = application.addFriend((String)params[0], (String)params[1], (String)params[2]);
				} catch (M_Exception e) {
					e.printStackTrace();
				}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					Toast.makeText(UserData_Activity.this, "添加好友发送成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(UserData_Activity.this, "添加好友已发送，等待对方处理", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	
}
