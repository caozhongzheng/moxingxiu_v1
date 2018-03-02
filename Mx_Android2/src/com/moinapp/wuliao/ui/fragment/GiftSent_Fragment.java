package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.GiftsSent_List_Adapter;
import com.moinapp.wuliao.model.OpenGift_List_Model;
import com.moinapp.wuliao.model.OpenGift_Model;
import com.moinapp.wuliao.task.AsyncTask;

public class GiftSent_Fragment extends Base_Fragment {
	
	private M_Application application;
	private ListView listview;
	private GiftsSent_List_Adapter adapter;
	private ArrayList<OpenGift_List_Model> list_sent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.gift_sent_fragment_layout, container, false);
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
		getGiftSent(1, false);
	}
	
	private void initData() {
		list_sent = new ArrayList<OpenGift_List_Model>();
		adapter = new GiftsSent_List_Adapter(getActivity(), list_sent);
	}
	
	private void initView() {
		listview = (ListView) getView().findViewById(R.id.gift_sent_lv);
		View footerView = getActivity().getLayoutInflater().inflate(R.layout.listview_loadmore_footer, null);
		ImageView loadMore = (ImageView) footerView.findViewById(R.id.gift_loadmore);
		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
	}
	
	public void getGiftSent(int page, boolean isMore) {
		new GetGiftSent_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, page + "", "10", isMore);
	}
	
	private class GetGiftSent_Task extends AsyncTask<Object, Void, OpenGift_Model> {
		private boolean isMore;
		@Override
		protected OpenGift_Model doInBackground(Object... params) {
			isMore = (Boolean) params[2];
			return application.getUserSendGiftList(application.getUid(), (String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(OpenGift_Model result) {
			if(result != null && result.getList() != null) {
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
