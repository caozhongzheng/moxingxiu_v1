package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.SelectFriend_List_Adapter;
import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.model.Friend_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class SelectFriend_Activity extends Base_Activity {

	private M_Application application;
	private ListView myfriends_listview;
	private SelectFriend_List_Adapter adapter;
	private ArrayList<Friend_Content_Model> list;
	private int type = 0;
	private final int TYPE_GIFT = 1, TYPE_MESSAGE = 2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.select_friend_layout);
		initData();
		initView();
		
		new MyFriends_Task().execute(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private void initData() {
		application = (M_Application) getApplication();
		Bundle bundle = getIntent().getExtras();
		if (bundle.getString("from").equals("gift")) {
			type = TYPE_GIFT;
		} else if (bundle.getString("from").equals("message")) {
			type = TYPE_MESSAGE;
		}
		list = new ArrayList<Friend_Content_Model>();
		adapter = new SelectFriend_List_Adapter(this, list);
	}
	
	private void initView() {
		myfriends_listview = (ListView)findViewById(R.id.select_friend_listview);
		myfriends_listview.setAdapter(adapter);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			if (adapter.getClickItem() != -1) {
				Friend_Content_Model data = list.get(adapter.getClickItem());
				if (type == TYPE_GIFT) {
					Bundle bundle = new Bundle();
					bundle.putString("fid", data.getId());
					bundle.putString("friend_avatar", data.getAvatar());
					bundle.putString("friend_nickname", data.getNickname());
					AppTools.toIntent(this, bundle, SendGifts_Activity.class);
				} else if (type == TYPE_MESSAGE) {
					Bundle bundle = new Bundle();
					bundle.putString("fid", data.getId());
					bundle.putString("avatar", data.getAvatar());
					AppTools.toIntent(this, bundle, Chatting_Activity.class);
				}
			}
			break;
		}
	}
	
	private class MyFriends_Task extends AsyncTask<Object, Void, Friend_Model> {

		@Override
		protected Friend_Model doInBackground(Object... params) {
			return application.getFriends(true, application.getUid());
		}

		@Override
		protected void onPostExecute(Friend_Model result) {
			if(result != null && result.getList() != null) {
				list.clear();
				list.addAll(result.getList());
				
				adapter.notifyDataSetChanged();
			}
		}
	}
}
