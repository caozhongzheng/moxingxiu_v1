package com.moinapp.wuliao.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.Chat_List_Adapter;
import com.moinapp.wuliao.model.HistoryMessage_Content_Model;
import com.moinapp.wuliao.model.HistoryMessage_Model;
import com.moinapp.wuliao.service.MsgService;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.PollingUtils;

public class Chatting_Activity extends Base_Activity {

	private M_Application application;
	private PullToRefreshListView chat_listview;
	private Chat_List_Adapter adapter;
	private ArrayList<HistoryMessage_Content_Model> list;
	private int page = 1;
	private final String pagesize = "20";
	private EditText chat_content;
	private String fid, friend_avatar;
	private ImageView chat_send;
	private String content;
	private Timer timer;
	public static String NewMsg_ACTION = "新消息来了";
	
//	public Handler MessageHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 6) {
//				getHistoryMessage(1, false);
//			}
//		};
//	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);
		
		registerBroadcastReceiver();
		initData();
		initView();
		
		getHistoryMessage(page, false);
		PollingUtils.startPollintService(this, 10, MsgService.class, MsgService.ACTION);
		
//		timer.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				getReceiveHistoryMessage();
//			}
//		}, 0, 10 * 1000);
	}
	
	private void initData() {
		timer = new Timer();
		application = (M_Application) getApplication();
		list = new ArrayList<HistoryMessage_Content_Model>();
		adapter = new Chat_List_Adapter(this, list);
		Bundle bundle = getIntent().getExtras();
		fid = bundle.getString("fid");
		friend_avatar = bundle.getString("friend_avatar");
	}
	
	private void initView() {
		chat_listview = (PullToRefreshListView)findViewById(R.id.chat_lv);
		chat_listview.setMode(Mode.BOTH);
		chat_listview.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				page++;
				getHistoryMessage(page, true);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				chat_content.requestFocus();
				InputMethodManager imm = (InputMethodManager) chat_content.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
				
				final Handler mHandler = new Handler() {
					@Override
					public void handleMessage(Message arg0) {
						chat_listview.onRefreshComplete();
					}
				};
				
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						// 必须要异步才会关
						Message msg = mHandler.obtainMessage();
						msg.sendToTarget();
					}
				}, 100);
			}
		});
		chat_listview.setAdapter(adapter);
		chat_content = (EditText) findViewById(R.id.chat_content);
	}
	
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(NewMsg_ACTION)) {
				getHistoryMessage(1, false);
			}
		}
	};
	
	public void registerBroadcastReceiver() {
		IntentFilter mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(NewMsg_ACTION);
		// 注册广播
		registerReceiver(mBroadcastReceiver, mIntentFilter);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.chat_send:
			content = chat_content.getText().toString();
			if (!content.equals("")) {
				new SendMessage_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, fid, content);
				new MySendHistoryMessage_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, application.getUid(), fid, "1", "1");
			}
			break;
		case R.id.title_right:
			list.clear();
			adapter.notifyDataSetChanged();
			page = 0;
			break;
		}
	}
	
	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(chat_content.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		chat_content.clearFocus();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PollingUtils.stopPollintService(this, MsgService.class, MsgService.ACTION);
//		timer.cancel();
	}
	
	
	private void getHistoryMessage(int page, boolean isMore) {
		new MyHistoryMessage_Task().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, application.getUid(), fid, page + "", pagesize, isMore);
	}
	
	private class SendMessage_Task extends AsyncTask<Object, Void, Map> {
		@Override
		protected Map doInBackground(Object... params) {
			return application.sendMessage(application.getUid(), (String) params[0], (String) params[1]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if(result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					// 发送成功
					chat_content.setText("");
					collapseSoftInputMethod();
				} else {
					// 发送失败
					Toast.makeText(Chatting_Activity.this, "发送失败", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	
	private class MyHistoryMessage_Task extends AsyncTask<Object, Void, HistoryMessage_Model> {
		private boolean isMore;
		@Override
		protected HistoryMessage_Model doInBackground(Object... params) {
			isMore = (Boolean) params[4];
			return application.getUserFriendMessage(true, params);
		}

		@Override
		protected void onPostExecute(HistoryMessage_Model result) {
			if (result != null && result.getList() != null) {
				if (isMore) {
					if (result.getList().size() != 0) {
						Collections.reverse(result.getList());
						list.addAll(0, result.getList());
					} else {
						Toast.makeText(Chatting_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
					}
				} else {
					list.clear();
					list.addAll(result.getList());
					Collections.reverse(list);
	//				Collections.sort(list, new MyComparator());
				}
				adapter.notifyDataSetChanged();
				chat_listview.onRefreshComplete();
				if (page == 1) {
					chat_listview.getRefreshableView().setSelection(result.getList().size());
				}
			}
		}
	}
	
	
	private class MySendHistoryMessage_Task extends AsyncTask<Object, Void, HistoryMessage_Model> {
		@Override
		protected HistoryMessage_Model doInBackground(Object... params) {
			return application.getUserSendHistoryMessage(true, params);
		}

		@Override
		protected void onPostExecute(HistoryMessage_Model result) {
			if (result != null && result.getList() != null) {
				list.addAll(result.getList());
				adapter.notifyDataSetChanged();
			}
		}
	}
	
//	class MyComparator implements Comparator {
//		public int compare(Object obj1, Object obj2) {
//			HistoryMessage_Content_Model m1 = (HistoryMessage_Content_Model) obj1;
//			HistoryMessage_Content_Model m2 = (HistoryMessage_Content_Model) obj2;
//			Long time1 = Long.parseLong(m1.getAddtime());
//			Long time2 = Long.parseLong(m2.getAddtime());
//			
//			if (time1 >= time2) {
//				return 1;
//			} else {
//				return -1;
//			}
//		}
//	}
	
//	private void getReceiveHistoryMessage() {
//		new MyReceiveHistoryMessage_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//	}
//	
//	private class MyReceiveHistoryMessage_Task extends AsyncTask<Object, Void, NewMessageEvent_Model> {
//		@Override
//		protected NewMessageEvent_Model doInBackground(Object... params) {
//			return application.getUserUnreadMessage(application.getUid());
//		}
//
//		@Override
//		protected void onPostExecute(NewMessageEvent_Model result) {
//			ArrayList<NewMessageEvent_Content_Model> list_message = new ArrayList<NewMessageEvent_Content_Model>();
//			if (result != null && result.getList() != null) {
//				list_message.addAll(result.getList());
//				if (list_message.size() > 0) {
//					Message msg = MessageHandler.obtainMessage();
//					msg.what = 6;
//					MessageHandler.sendMessage(msg);
//				}
//			} 
//		}
//	}

}
