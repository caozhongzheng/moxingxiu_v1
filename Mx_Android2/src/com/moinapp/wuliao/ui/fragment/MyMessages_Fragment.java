package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.activity.Expression_Activity;
import com.moinapp.wuliao.activity.Game_Activity;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.activity.Video_Activity;
import com.moinapp.wuliao.adapter.MyStar_Adapter;
import com.moinapp.wuliao.model.MyStar_Content_Model;
import com.moinapp.wuliao.model.MyStar_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class MyMessages_Fragment extends Base_ListFragment {
	
	private M_Application applicaton;
	private String type;
	private String uid;
	private int page = 1;
	private ArrayList<MyStar_Content_Model> list;
	private MyStar_Adapter adapter;
	private Type_Num type_num;

	public MyMessages_Fragment() {
	}

	public MyMessages_Fragment(String type, String uid) {
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
		
		setListAdapter(adapter);
		getListView().setDivider(new ColorDrawable(android.R.color.transparent));
		getData();
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
		MyStar_Content_Model data = list.get(position);
		Bundle bundle = new Bundle();
		bundle.putString("about_id", data.getId());
		bundle.putString("about_type", data.getAbout_type());
		if(data.getAbout_type().equals(M_Constant.FACE))
			AppTools.toIntent(getActivity(), bundle, Expression_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.NEWS))
			AppTools.toIntent(getActivity(), bundle, News_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.GAME))
			AppTools.toIntent(getActivity(), bundle, Game_Activity.class);
		else if(data.getAbout_type().equals(M_Constant.VIDEO))
			AppTools.toIntent(getActivity(), bundle, Video_Activity.class);
	}

	private void getData() {
		new MyStar_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}

	private class MyStar_Task extends AsyncTask<Object, Void, MyStar_Model> {

		@Override
		protected MyStar_Model doInBackground(Object... params) {
			return applicaton.getUserMyStar(true, "1", type, page + "", "100");
		}

		@Override
		protected void onPostExecute(MyStar_Model result) {
			if(result != null && result.getList() != null) {
				list.clear();
				list.addAll(result.getList());
				
				type_num.setTypenum(type, result.getList().size() + "");
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	public interface Type_Num {
		public void setTypenum(String type, String num);
	}
}
