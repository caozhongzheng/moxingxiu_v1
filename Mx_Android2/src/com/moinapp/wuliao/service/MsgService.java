package com.moinapp.wuliao.service;

import java.util.ArrayList;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.activity.Chatting_Activity;
import com.moinapp.wuliao.model.NewMessageEvent_Content_Model;
import com.moinapp.wuliao.model.NewMessageEvent_Model;
import com.moinapp.wuliao.task.AsyncTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MsgService extends Service {
	private M_Application application;
	public static final String ACTION = "com.mx.app.service.MsgService";
	// 新消息的回调接口
	private OnNewMessageListener onNewMessageListener; 
	private int count = 0;
	
	public void setOnNewMessageListener(OnNewMessageListener onNewMessageListener) {
		this.onNewMessageListener = onNewMessageListener;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		application = (M_Application) getApplication();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		getReceiveHistoryMessage();
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
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
			ArrayList<NewMessageEvent_Content_Model> list_message = new ArrayList<NewMessageEvent_Content_Model>();
			if (result != null && result.getList() != null) {
				list_message.addAll(result.getList());
				if (list_message.size() > 0) {
					count++;
//					onNewMessageListener.onNewMessage(count);
					Intent intent = new Intent(Chatting_Activity.NewMsg_ACTION);
					// 发送广播
					sendBroadcast(intent);
				}
			} 
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public interface OnNewMessageListener {
		void onNewMessage(int count);
	}

}
