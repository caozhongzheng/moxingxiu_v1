package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.MyLikes_Adapter;
import com.moinapp.wuliao.model.MyStar_Content_Model;
import com.moinapp.wuliao.model.MyStar_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class MyLikes_Activity extends Base_Activity {

	private String uid;
	private PullToRefreshListView mylikes_listview;
	private MyLikes_Adapter adapter;
	private M_Application application;
	private int page = 1;
	private int pagesize = 10;
	private ArrayList<MyStar_Content_Model> data_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mylikes_layout);
		application = (M_Application) getApplication();
		
		initData();
		initView();
		getData(uid, page, false, true);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		}
	}

	private void initData() {
		Bundle bundle = getIntent().getExtras();
		uid = bundle.getString("uid");
		
		data_list = new ArrayList<MyStar_Content_Model>();
		adapter = new MyLikes_Adapter(this, data_list);
	}
	
	private void initView() {
		mylikes_listview = (PullToRefreshListView)findViewById(R.id.mylikes_listview);
		mylikes_listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getData(uid, 1, false, true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page ++;
				getData(uid, page, false, true);
			}
		});
		
		mylikes_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyStar_Content_Model data = data_list.get(position - 1);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if(data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(MyLikes_Activity.this, bundle, Expression_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(MyLikes_Activity.this, bundle, News_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(MyLikes_Activity.this, bundle, Game_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(MyLikes_Activity.this, bundle, Video_Activity.class);
			}
		});
		
		mylikes_listview.setAdapter(adapter);
	}
	
	private void getData(String id, int page, boolean isMore, boolean isRefresh) {
		new MyLikes_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isMore, isRefresh, id, page);
	}
	
	private class MyLikes_Task extends AsyncTask<Object, Void, MyStar_Model> {
		private boolean isRefresh;
		private boolean isMore;

		@Override
		protected MyStar_Model doInBackground(Object... params) {
			MyStar_Model data = null;
			isMore = (Boolean) params[0];
			isRefresh = (Boolean) params[1];
			data = application.getMyLikes(isRefresh, (String)params[2], params[3] + "", pagesize + "");
			return data;
		}

		@Override
		protected void onPostExecute(MyStar_Model result) {
			if (!isMore) {
				data_list.clear();
				page = 1;
			} else {
			}

			if(result != null && result.getList() != null) {
				if(result.getList().size() != 0)
				data_list.addAll(result.getList());
				else
				Toast.makeText(MyLikes_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
			}
			

			mylikes_listview.onRefreshComplete();
			adapter.notifyDataSetChanged();
		}

	}
}
