package com.moinapp.wuliao.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.AutoLayout_Adapter;
import com.moinapp.wuliao.adapter.AutoLayout_Adapter.AutoLayout_Callback;
import com.moinapp.wuliao.adapter.MySearch_Adapter;
import com.moinapp.wuliao.model.MainTimelineItem_Model;
import com.moinapp.wuliao.model.MainTimeline_Model;
import com.moinapp.wuliao.model.SearchHotWords_Content_Model;
import com.moinapp.wuliao.model.SearchHotWords_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.LinearLineWrapLayout;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.StringsUtil;

public class Search_Activity extends Base_Activity implements AutoLayout_Callback {
	private String content;
	private EditText search_edittext;
	private M_Application application;
	private ArrayList<MainTimelineItem_Model> data_list;
	private ListView search_listview;
	private MySearch_Adapter adapter;
	private LinearLayout hotword_layout;
	private LinearLineWrapLayout auto_layout;
	private AutoLayout_Adapter auto_adapter;
	private ArrayList<SearchHotWords_Content_Model> hotwords_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search_layout);
		application = (M_Application) getApplication();

		initData();
		initView();

//		new Search_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, content);
		new GetHotWords_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "0", "10");
	}

	private void initData() {
//		content = getIntent().getExtras().getString("content");

		data_list = new ArrayList<MainTimelineItem_Model>();
		adapter = new MySearch_Adapter(this, data_list);
		
		hotwords_list = new ArrayList<SearchHotWords_Content_Model>();
//		ArrayList<String> aa = new ArrayList<String>();
//		aa.add("唐嫣");
//		aa.add("舒畅");
//		aa.add("李易峰");
//		aa.add("杨幂");
//		aa.add("陈伟霆");
//		aa.add("刘亦菲");
//		aa.add("杨洋");

//		auto_adapter = new AutoLayout_Adapter(this, aa);
	}

	private void initView() {
		hotword_layout = (LinearLayout) findViewById(R.id.hotword_layout);
		auto_layout = (LinearLineWrapLayout)findViewById(R.id.auto_layout);
		
		search_edittext = (EditText) findViewById(R.id.search_edittext);
//		search_edittext.setText(content);

		search_listview = (ListView) findViewById(R.id.search_listview);
		search_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainTimelineItem_Model data = data_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if (data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(Search_Activity.this, bundle, Expression_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(Search_Activity.this, bundle, News_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(Search_Activity.this, bundle, Game_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(Search_Activity.this, bundle, Video_Activity.class);
			}
		});

		search_listview.setAdapter(adapter);
	}
	
	@Override
	public void click(String word) {
		// TODO Auto-generated method stub
		search_edittext.setText(word);
		if (!"".equals(word))
			new Search_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, word);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right_search:
			content = search_edittext.getText().toString();
			if (!StringsUtil.isEmpty(content))
				new Search_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, content);
			break;
		}
	}

	private class Search_Task extends AsyncTask<Object, Void, MainTimeline_Model> {

		@Override
		protected MainTimeline_Model doInBackground(Object... params) {
			MainTimeline_Model data = null;
			try {
				data = application.getSearchData((String) params[0]);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(MainTimeline_Model result) {
			data_list.clear();
			if (result != null) {
				hotword_layout.setVisibility(View.INVISIBLE);
				search_listview.setVisibility(View.VISIBLE);
				if (result.getTimeline().getList() != null && result.getTimeline().getList().size() != 0) {
					data_list.addAll(result.getTimeline().getList());
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private class GetHotWords_Task extends AsyncTask<Object, Void, SearchHotWords_Model> {

		@Override
		protected SearchHotWords_Model doInBackground(Object... params) {
			SearchHotWords_Model data = null;
			try {
				data = application.getHotWords((String) params[0], (String) params[1]);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(SearchHotWords_Model result) {
			if (result != null && result.getList() != null) {
				hotwords_list.clear();
				hotwords_list.addAll(result.getList());
				
				
				auto_adapter = new AutoLayout_Adapter(Search_Activity.this, hotwords_list, Search_Activity.this);
				auto_layout.setAdapter(auto_adapter);
			}
		}
	}
}
