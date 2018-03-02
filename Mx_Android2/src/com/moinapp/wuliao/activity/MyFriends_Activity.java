package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.MyFriends_List_Adapter;
import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.model.Friend_Model;
import com.moinapp.wuliao.task.AsyncTask;

public class MyFriends_Activity extends Base_Activity {

	private M_Application application;
	private ListView myfriends_listview;
	private MyFriends_List_Adapter adapter;
	private ArrayList<Friend_Content_Model> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myfriends_layout);
		application = (M_Application) getApplication();
		initData();
		initView();
		new MyFriends_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true);
	}
	
	private void initData() {
		list = new ArrayList<Friend_Content_Model>();
		adapter = new MyFriends_List_Adapter(this, list);
	}
	
	private void initView() {
		myfriends_listview = (ListView)findViewById(R.id.myfriend_listview);
		myfriends_listview.setAdapter(adapter);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			boolean isDelete = data.getBooleanExtra("isDelete", false);
			if (isDelete) {
				new MyFriends_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true);
			}
		}
	}
	
	private class MyFriends_Task extends AsyncTask<Object, Void, Friend_Model> {
		
		@Override
		protected Friend_Model doInBackground(Object... params) {
			return application.getFriends((Boolean) params[0], application.getUid());
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
