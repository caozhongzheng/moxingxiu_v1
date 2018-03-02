package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.activity.Function_Activity;
import com.moinapp.wuliao.model.Friend_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.Mx_ViewPager;
import com.moinapp.wuliao.R;

public class MyHistory_Fragment extends Base_Fragment {

	private Mx_ViewPager mystar_viewpager;
	private ArrayList<Fragment> fragment_list;
	private String uid;
	private ImageView unopengift_icon, unopenmessage_icon;
	private Function_Activity activity;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.mystar_layout, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initView();
//		new MySendHistoryNum_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "uid");
//		new MyFriends_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "uid");
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (Function_Activity) activity;
	}
	
	private void initData() {
		uid = activity.uid;
		fragment_list = new ArrayList<Fragment>();
		fragment_list.add(new MyStar_Fragment(M_Constant.DEFAULT, uid));
	}
	
	private void initView() {
		mystar_viewpager = (Mx_ViewPager) getActivity().findViewById(R.id.mystar_viewpager);
		mystar_viewpager.setOffscreenPageLimit(4);
		mystar_viewpager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
			
			@Override
			public int getCount() {
				return fragment_list.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return fragment_list.get(arg0);
			}
		});
		
//		unopengift_icon = (ImageView)findViewById(R.id.unopengift_icon);
//		unopenmessage_icon = (ImageView)findViewById(R.id.unopenmessage_icon);
	}
	
	private class MyFriends_Task extends AsyncTask<Object, Void, Friend_Model> {
		@Override
		protected Friend_Model doInBackground(Object... params) {
			return activity.application.getFriends(true, activity.application.getUid());
		}

		@Override
		protected void onPostExecute(Friend_Model result) {
			if(result != null && result.getList() != null) {
				activity.friend_num.setText("" + Integer.parseInt(result.getTotal()));
			}
		}
	}
	
	private class MySendHistoryNum_Task extends AsyncTask<Object, Void, Integer> {
		@Override
		protected Integer doInBackground(Object... params) {
			return activity.application.getUserMessageNum(activity.application.getUid());
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result >= 0)
				activity.message_num.setText(result + "");
		}
	}
}
