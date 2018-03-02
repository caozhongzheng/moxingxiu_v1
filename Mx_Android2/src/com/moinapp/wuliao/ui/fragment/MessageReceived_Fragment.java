package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.activity.Chatting_Activity;
import com.moinapp.wuliao.adapter.MessagesReceived_List_Adapter;
import com.moinapp.wuliao.model.NewFriendshipEvent_Content_Model;
import com.moinapp.wuliao.model.NewMessageEvent_Content_Model;
import com.moinapp.wuliao.model.NewMessageEvent_Model;
import com.moinapp.wuliao.model.RecentEvent_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.R;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MessageReceived_Fragment extends Base_Fragment {
	
	private M_Application application;
	private ListView listview;
	private MessagesReceived_List_Adapter adapter;
	private ArrayList<NewFriendshipEvent_Content_Model> list_friendship;
	private ArrayList<NewMessageEvent_Content_Model> list_message;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.message_received_fragment_layout, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (M_Application) getActivity().getApplication();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
		initView();
		getReceiveFriendship();
	}
	
	private void initData() {
		list_friendship = new ArrayList<NewFriendshipEvent_Content_Model>();
		list_message = new ArrayList<NewMessageEvent_Content_Model>();
		adapter = new MessagesReceived_List_Adapter(getActivity(), list_friendship, list_message);
	}
	
	private void initView() {
		listview = (ListView) getView().findViewById(R.id.message_received_lv);
//		View footerView = getActivity().getLayoutInflater().inflate(R.layout.listview_loadmore_footer, null);
//		ImageView loadMore = (ImageView) footerView.findViewById(R.id.gift_loadmore);
//		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
	}
	
	private void getReceiveFriendship() {
		new MyReceiveFriendship_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private class MyReceiveFriendship_Task extends AsyncTask<Object, Void, RecentEvent_Model> {
		@Override
		protected RecentEvent_Model doInBackground(Object... params) {
			return application.getRecentEvent(true, application.getUid(), 0);
		}

		@Override
		protected void onPostExecute(RecentEvent_Model result) {
			if (result != null) {
				if (result.getNewFriendshipEvent() != null && result.getNewFriendshipEvent().getList() != null) {
					list_friendship.clear();
					list_friendship.addAll(result.getNewFriendshipEvent().getList());
				}
				
//				if (result.getNewMessageEvent() != null && result.getNewMessageEvent().getList() != null) {
//					list_message.clear();
//					list_message.addAll(result.getNewMessageEvent().getList());
//				} else {
//					list_message.clear();
//				}
				
				getReceiveHistoryMessage();
			}
		}
	}
	
	private void getReceiveHistoryMessage() {
		new MyReceiveHistoryMessage_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private class MyReceiveHistoryMessage_Task extends AsyncTask<Object, Void, NewMessageEvent_Model> {
		@Override
		protected NewMessageEvent_Model doInBackground(Object... params) {
			return application.getUserUnreadMessage(application.getUid());
		}

		@Override
		protected void onPostExecute(NewMessageEvent_Model result) {
			if (result != null && result.getList() != null) {
				list_message.clear();
				list_message.addAll(result.getList());
			} else {
				list_message.clear();
			}
			adapter.notifyDataSetChanged();
		}
	}
}
