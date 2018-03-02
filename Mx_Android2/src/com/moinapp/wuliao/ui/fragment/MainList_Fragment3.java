package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.adapter.Main_Adapter;
import com.moinapp.wuliao.adapter.Main_Adapter2;
import com.moinapp.wuliao.model.MainTimelineItem_Model;
import com.moinapp.wuliao.model.MainTimeline_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class MainList_Fragment3 extends Base_Fragment {

	private String type;
	private PullToRefreshScrollView ms;
	private ListView ls1, ls2;
	private M_Application application;
	private Main_Adapter adapter;
	private Main_Adapter2 adapter2;
	private ArrayList<MainTimelineItem_Model> data_list;
	private ArrayList<MainTimelineItem_Model> left_data_list;
	private ArrayList<MainTimelineItem_Model> right_data_list;
	private int position = 1;
	private int pagesize = 10;

	public MainList_Fragment3(String type) {
		this.type = type;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (M_Application) getActivity().getApplication();
		initData();
	}

	private void initData() {
		data_list = new ArrayList<MainTimelineItem_Model>();
		left_data_list = new ArrayList<MainTimelineItem_Model>();
		right_data_list = new ArrayList<MainTimelineItem_Model>();

		adapter = new Main_Adapter(getActivity(), left_data_list);
		adapter2 = new Main_Adapter2(getActivity(), right_data_list);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView();
		getData(type, position, false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mainlist_fragment_layout3, container, false);
	}

	private void initView() {
		ls1 = (ListView) getActivity().findViewById(R.id.ls1_3);
		ls2 = (ListView) getActivity().findViewById(R.id.ls2_3);

		ms = (PullToRefreshScrollView) getActivity().findViewById(R.id.ms3);
		ms.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// String label =
				// DateUtils.formatDateTime(getApplicationContext(),
				// System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
				// | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				//
				// // Update the LastUpdatedLabel
				// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				getData(type, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				position++;
				getData(type, position, true);
			}
		});

		ls1.setAdapter(adapter);
		ls2.setAdapter(adapter2);

		ls1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainTimelineItem_Model data = left_data_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				AppTools.toIntent(getActivity(), bundle, News_Activity.class);
			}
		});

		ls2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainTimelineItem_Model data = right_data_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				AppTools.toIntent(getActivity(), bundle, News_Activity.class);
			}
		});
	}

	private void getData(String type, int i, boolean isMore) {
		new TimeLine_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isMore, true, type, i);
	}

	private class TimeLine_Task extends AsyncTask<Object, Void, MainTimeline_Model> {

		private boolean isRefresh;
		private boolean isMore;

		@Override
		protected MainTimeline_Model doInBackground(Object... params) {
			MainTimeline_Model data = null;
			isMore = (Boolean) params[0];
			isRefresh = (Boolean) params[1];
			try {
				// application.getDetailData(isRefresh, M_Constant.VIDEO, "1");
				data = application.getTimeLineData(isRefresh, isMore, params[2], params[3], pagesize + "");
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(MainTimeline_Model result) {
			if (result != null) {
				data_list.clear();
				if (!isMore) {
					left_data_list.clear();
					right_data_list.clear();
					position = 1;
				}

				if (result.getTimeline().getList() != null && result.getTimeline().getList().size() != 0) {
					data_list.addAll(result.getTimeline().getList());

					for (int i = 0; i < result.getTimeline().getList().size(); i++) {
						if (i % 2 == 0)
							left_data_list.add(data_list.get(i));
						else
							right_data_list.add(data_list.get(i));
					}
					
					AppTools.setListViewHeight(ls1);
					// AppTools.setListViewHeight(ls2);
					// AppTools.setListView1Height(ls1, left_data_list.size());
					AppTools.setListView2Height(ls2, right_data_list.size());
					// adapter.notifyDataSetChanged();
					// adapter2.notifyDataSetChanged();
				}
			}
			ms.onRefreshComplete();
		}

	}
}
