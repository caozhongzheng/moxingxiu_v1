package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.activity.MyGifts_Activity;
import com.moinapp.wuliao.adapter.GiftsReceived_List_Adapter;
import com.moinapp.wuliao.model.OpenGift_List_Model;
import com.moinapp.wuliao.model.OpenGift_Model;
import com.moinapp.wuliao.task.AsyncTask;

public class GiftReceived_Fragment extends Base_Fragment {
	
	private M_Application application;
	public ListView listview;
	private GiftsReceived_List_Adapter adapter;
	private ArrayList<OpenGift_List_Model> list_received;
	private ArrayList<OpenGift_List_Model> list_1;
	private ArrayList<OpenGift_List_Model> list_2;
	private int selection = 0;
	
	private Fragment_callback callback;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.gift_received_fragment_layout, container, false);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (M_Application) getActivity().getApplication();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callback = (Fragment_callback) activity;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
		initView();
		getGiftReceived(1, false);
	}
	
	private void initData() {
		list_received = new ArrayList<OpenGift_List_Model>();
		list_1 = new ArrayList<OpenGift_List_Model>();
		list_2 = new ArrayList<OpenGift_List_Model>();
		adapter = new GiftsReceived_List_Adapter(getActivity(), list_received);
	}
	
	private void initView() {
		listview = (ListView) getView().findViewById(R.id.gift_received_lv);
		View footerView = getActivity().getLayoutInflater().inflate(R.layout.listview_loadmore_footer, null);
		ImageView loadMore = (ImageView) footerView.findViewById(R.id.gift_loadmore);
		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
	}
	
	public void getGiftReceived(int page, boolean isMore) {
		list_received.clear();
		
		new GiftUnOpen_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, application.getUid());
		new GiftReceived_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, page + "", "10", isMore);
	}
	
	private class GiftUnOpen_Task extends AsyncTask<Object, Void, OpenGift_Model> {
		@Override
		protected OpenGift_Model doInBackground(Object... params) {
			return application.getUnOpenGift(application.getUid());
		}

		@Override
		protected void onPostExecute(OpenGift_Model result) {
			if(result != null && result.getList() != null) {
				list_1.clear();
				list_1.addAll(result.getList());
				list_received.addAll(list_1);
				selection += result.getList().size();
				adapter.notifyDataSetChanged();
				
				if(result.getList().size() > 0) {
					callback.callback(result.getList().get(0));
					MyGifts_Activity.isOpen = true;
				}
			}
		}
	}
	
	private class GiftReceived_Task extends AsyncTask<Object, Void, OpenGift_Model> {
		private boolean isMore;
		@Override
		protected OpenGift_Model doInBackground(Object... params) {
			isMore = (Boolean) params[2];
			return application.getUserReceiveGiftList(application.getUid(), (String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(OpenGift_Model result) {
			if (result != null && result.getList() != null) {
				if (isMore) {
					if (result.getList().size() != 0) {
						list_2.addAll(result.getList());
						list_received.addAll(list_2);
						adapter.notifyDataSetChanged();
					} else {
						list_received.addAll(list_2);
						adapter.notifyDataSetChanged();
						Toast.makeText(getActivity(), R.string.more_end, Toast.LENGTH_SHORT).show();
					}
					listview.setSelection(selection);
					
				} else {
					list_2.clear();
					list_2.addAll(result.getList());
					list_received.addAll(list_2);
					adapter.notifyDataSetChanged();
				}
				selection += list_2.size();
			} 
		}
	}
	
	public interface Fragment_callback {
		public void callback(OpenGift_List_Model data);
	}
}
