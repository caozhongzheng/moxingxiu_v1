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
import com.moinapp.wuliao.adapter.MyComments_Adapter;
import com.moinapp.wuliao.model.MyComments_Content_Model;
import com.moinapp.wuliao.model.MyComments_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class MyComments_Activity extends Base_Activity {

	private String id, nickname;
	private PullToRefreshListView mycomments_listview;
	private MyComments_Adapter adapter;
	private M_Application application;
	private int page = 1;
	private int pagesize = 10;
	private ArrayList<MyComments_Content_Model> data_list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.mycomments_layout);
		application = (M_Application) getApplication();
		
		initData();
		initView();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getData(id, page, false, true);
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
		id = bundle.getString("uid");
		nickname = bundle.getString("nickname");
		
		data_list = new ArrayList<MyComments_Content_Model>();
		adapter = new MyComments_Adapter(this, data_list, nickname);
	}
	
	private void initView() {
		mycomments_listview = (PullToRefreshListView)findViewById(R.id.mycomments_listview);
		mycomments_listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getData(id, 1, false, true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				page ++;
				getData(id, page, true, true);
			}
		});
		
		mycomments_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MyComments_Content_Model data = data_list.get(position - 1);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if(data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(MyComments_Activity.this, bundle, Expression_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(MyComments_Activity.this, bundle, News_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(MyComments_Activity.this, bundle, Game_Activity.class);
				else if(data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(MyComments_Activity.this, bundle, Video_Activity.class);
			}
		});
		
		mycomments_listview.setAdapter(adapter);
	}
	
	private void getData(String id, int page, boolean isMore, boolean isRefresh) {
		new MyComments_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isMore, isRefresh, id, page + "");
	}

	private class MyComments_Task extends AsyncTask<Object, Void, MyComments_Model> {
		private boolean isRefresh;
		private boolean isMore;

		@Override
		protected MyComments_Model doInBackground(Object... params) {
			MyComments_Model data = null;
			isMore = (Boolean) params[0];
			isRefresh = (Boolean) params[1];
			data = application.getMyComments(isRefresh, (String)params[2], (String)params[3], pagesize + "");
			return data;
		}

		@Override
		protected void onPostExecute(MyComments_Model result) {
			if (!isMore) {
				data_list.clear();
				page = 1;
			} else {
			}

			if(result != null && result.getList() != null) {
				if(result.getList().size() != 0)
				data_list.addAll(result.getList());
				else
				Toast.makeText(MyComments_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
			}

			mycomments_listview.onRefreshComplete();
			adapter.notifyDataSetChanged();
		}
	}
}
