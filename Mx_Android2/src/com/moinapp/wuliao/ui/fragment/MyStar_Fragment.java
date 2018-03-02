package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.activity.Expression_Activity;
import com.moinapp.wuliao.activity.Game_Activity;
import com.moinapp.wuliao.activity.MyGifts_Activity;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.activity.Video_Activity;
import com.moinapp.wuliao.adapter.MyStar_Adapter;
import com.moinapp.wuliao.model.MyStar_Content_Model;
import com.moinapp.wuliao.model.MyStar_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.R;
import com.umeng.analytics.MobclickAgent;

public class MyStar_Fragment extends PullToRefreshListFragment {
	
	private M_Application applicaton;
	private String type;
	private String uid;
	private int page = 1;
	private int pagesize = 10;
	private ArrayList<MyStar_Content_Model> list;
	private MyStar_Adapter adapter;
	private Type_Num type_num;

	public MyStar_Fragment() {
	}

	public MyStar_Fragment(String type, String uid) {
		this.type = type;
		this.uid = uid;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicaton = (M_Application) getActivity().getApplication();
		initData();
		this.type_num = (Type_Num) getActivity();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		getPullToRefreshListView().setMode(Mode.BOTH);
		getPullToRefreshListView().setLoadingDrawable(getResources().getDrawable(R.drawable.pulls));
		
		getPullToRefreshListView().setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				page = 1;
				getData(uid, type, 1, pagesize, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				page ++ ;
				getData(uid, type, page, pagesize, true);
			}
		});
		
		setListAdapter(adapter);
		getListView().setDivider(new ColorDrawable(android.R.color.transparent));
		getData(uid, type, 1, pagesize, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initData() {
		list = new ArrayList<MyStar_Content_Model>();
		adapter = new MyStar_Adapter(getActivity(), list);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		MyStar_Content_Model data = list.get(position - 1);
		Bundle bundle = new Bundle();
		bundle.putString("about_id", data.getAbout_id());
		bundle.putString("about_type", data.getAbout_type());
		if(data.getAbout_type().equals(M_Constant.FACE))
			AppTools.toIntent(getActivity(), bundle, Expression_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.NEWS))
			AppTools.toIntent(getActivity(), bundle, News_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.GAME))
			AppTools.toIntent(getActivity(), bundle, Game_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.VIDEO))
			AppTools.toIntent(getActivity(), bundle, Video_Activity.class);
		else if(data.getAction_type().equals(M_Constant.RECEIVE_GIFT)) {
			bundle.putString("nickname", data.getNickname());
			bundle.putString("gender", data.getSex());
			bundle.putString("avatar", data.getAvatar());
			AppTools.toIntent(getActivity(), bundle, MyGifts_Activity.class);
		}
		else if(data.getAction_type().equals(M_Constant.SEND_GIFT)) {
			bundle.putString("nickname", data.getNickname());
			bundle.putString("gender", data.getSex());
			bundle.putString("avatar", data.getAvatar());
			AppTools.toIntent(getActivity(), bundle, MyGifts_Activity.class);
		}
	}

	private void getData(String uid, String type, int page, int pagesize, boolean isMore) {
		new MyStar_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, type, page, pagesize, isMore);
	}

	private class MyStar_Task extends AsyncTask<Object, Void, MyStar_Model> {
		
		private boolean isMore;

		@Override
		protected MyStar_Model doInBackground(Object... params) {
			isMore = (Boolean) params[4];
			return applicaton.getUserMyStar(true, (String)params[0], (String)params[1], params[2] + "", params[3] + "");
		}

		@Override
		protected void onPostExecute(MyStar_Model result) {
			if(result != null && result.getList() != null) {
				
				if(isMore) {
					list.addAll(result.getList());
				} else {
					list.clear();
					list.addAll(result.getList());
				}
				
				type_num.setTypenum(type, result.getList().size() + "");
				adapter.notifyDataSetChanged();
			}
			
			getPullToRefreshListView().onRefreshComplete();
		}
	}
	
	public interface Type_Num {
		public void setTypenum(String type, String num);
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getName()); // 统计页面
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
	}
}
