package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.activity.Chatting_Activity;
import com.moinapp.wuliao.adapter.MessagesSent_List_Adapter;
import com.moinapp.wuliao.model.HistoryMessage_Content_Model;
import com.moinapp.wuliao.model.HistoryMessage_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MessageSent_Fragment extends Base_Fragment {
	
	private M_Application application;
	private ListView listview;
	private MessagesSent_List_Adapter adapter;
	private ArrayList<HistoryMessage_Content_Model> list_sent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.message_sent_fragment_layout, container, false);
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
		getSendHistoryMessage(1, false);
	}
	
	private void initData() {
		list_sent = new ArrayList<HistoryMessage_Content_Model>();
		adapter = new MessagesSent_List_Adapter(getActivity(), list_sent);
	}
	
	private void initView() {
		listview = (ListView) getView().findViewById(R.id.message_sent_lv);
		View footerView = getActivity().getLayoutInflater().inflate(R.layout.listview_loadmore_footer, null);
		ImageView loadMore = (ImageView) footerView.findViewById(R.id.gift_loadmore);
		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				HistoryMessage_Content_Model item_data = list_sent.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("fid", item_data.getTo());
				bundle.putString("avatar", item_data.getTo_avatar());
				AppTools.toIntent(getActivity(), bundle, Chatting_Activity.class);
			}
		});
	}
	
	public void getSendHistoryMessage(int page, boolean isMore) {
		new MySendHistoryMessage_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "0", page + "", "10", isMore);
	}
	
	private class MySendHistoryMessage_Task extends AsyncTask<Object, Void, HistoryMessage_Model> {
		private boolean isMore;
		@Override
		protected HistoryMessage_Model doInBackground(Object... params) {
			isMore = (Boolean) params[3];
			return application.getUserSendHistoryMessage(true, application.getUid(), (String) params[0], (String) params[1], (String) params[2]);
		}

		@Override
		protected void onPostExecute(HistoryMessage_Model result) {
			if (result != null && result.getList() != null) {
				if (isMore) {
					if (result.getList().size() != 0) {
						list_sent.addAll(result.getList());
						adapter.notifyDataSetChanged();
					} else {
						Toast.makeText(getActivity(), R.string.more_end, Toast.LENGTH_SHORT).show();
					}
				} else {
					list_sent.clear();
					list_sent.addAll(result.getList());
					listview.setAdapter(adapter);
				}
			}
		}
	}
}
