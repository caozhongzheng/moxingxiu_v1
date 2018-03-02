package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.NewFriendshipEvent_Content_Model;
import com.moinapp.wuliao.model.NewMessageEvent_Content_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.ToastUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesReceived_List_Adapter extends BaseAdapter {

	private M_Application application;
	private Activity activity;

	// 数据集合
	private List<NewFriendshipEvent_Content_Model> listitems1;
	private List<NewMessageEvent_Content_Model> listitems2;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	private ImageLoader imageLoader;
	final int TYPE_FRIENDSHIP = 0;
	final int TYPE_MESSAGE = 1;
	
	public MessagesReceived_List_Adapter(Activity context, ArrayList<NewFriendshipEvent_Content_Model> listitems1, ArrayList<NewMessageEvent_Content_Model> listitems2) {
		activity = context;
		application = (M_Application) activity.getApplication();
		this.listitems1 = listitems1;
		this.listitems2 = listitems2;
		this.listParentContainer = LayoutInflater.from(activity);
		imageLoader = ImageLoader.getInstance();
	}

	/*
	 * ListView列表的条数 name: getCount
	 * @param
	 * @return
	 */
	public int getCount() {
		return listitems1.size() + listitems2.size();
	}

	public Object getItem(int position) {
		if (position < listitems1.size()) {
			return listitems1.get(position);
		} else {
			return listitems2.get(position);
		}
		
	}

	public long getItemId(int position) {
		return position;
	}
	
	//每个convertView都会调用此方法，获得当前所需的view样式
	@Override
	public int getItemViewType(int position) {
		if (position < listitems1.size()) {
			return TYPE_FRIENDSHIP;
		} else {
			return TYPE_MESSAGE;
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	/*
	 * ListItem的View name: getView
	 * @param
	 * @return
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);
		if (convertView == null) {
			switch (viewType) {
			case TYPE_FRIENDSHIP:
				convertView = listParentContainer.inflate(R.layout.mymessages_friendship_listitem, null);
				break;
			case TYPE_MESSAGE:
				convertView = listParentContainer.inflate(R.layout.mymessages_message_listitem, null);
				break;
			}
		} 
		
		TextView friendship_type = ViewHolder.get(convertView, R.id.friendship_type);
		TextView friendship_nickname = ViewHolder.get(convertView, R.id.friendship_nickname);
		TextView friendship_time = ViewHolder.get(convertView, R.id.friendship_time);
		ImageView friendship_accept = ViewHolder.get(convertView, R.id.friendship_accept);
		ImageView friendship_ignore = ViewHolder.get(convertView, R.id.friendship_ignore);
		
		TextView message_type = ViewHolder.get(convertView, R.id.message_type);
		TextView message_nickname = ViewHolder.get(convertView, R.id.message_nickname);
		TextView message_time = ViewHolder.get(convertView, R.id.message_time);
		TextView message_detail = ViewHolder.get(convertView, R.id.message_detail);
		
		if (position < listitems1.size()) {
			final NewFriendshipEvent_Content_Model data1 = listitems1.get(position);
			final int current_position = position;
			if (data1 != null) {
				friendship_type.setText(activity.getResources().getString(R.string.from));
				friendship_nickname.setText(data1.getNickname());
				friendship_time.setText(M_Constant.sdf_custom.format(Long.parseLong(data1.getAddtime())));
				friendship_accept.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						new AgreeFriend_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data1.getFid(), data1.getUid(), current_position);
					}
				});
				friendship_ignore.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						new DisagreeFriend_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data1.getFid(), data1.getUid(), current_position);
					}
				});
			}
		} else {
			NewMessageEvent_Content_Model data2 = listitems2.get(position - listitems1.size());
			if (data2 != null) {
				message_type.setText(activity.getResources().getString(R.string.from));
				message_nickname.setText(data2.getFrom());
				message_time.setText(M_Constant.sdf_custom.format(Long.parseLong(data2.getAddtime())));
				message_detail.setText(data2.getMessage());
			}
		}
		return convertView;
	}

	public class AgreeFriend_Task extends AsyncTask<Object, Void, Map> {
		private int del_item;
		@Override
		protected Map doInBackground(Object... params) {
			del_item = (Integer) params[2];
			return application.agreeFriend((String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					ToastUtil.makeText(activity, "同意好友添加请求成功", Toast.LENGTH_SHORT);
				} else {
					ToastUtil.makeText(activity, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT);
				}
				listitems1.remove(del_item);
				notifyDataSetChanged();
			}
		}
	}
	
	public class DisagreeFriend_Task extends AsyncTask<Object, Void, Map> {
		private int del_item;
		@Override
		protected Map doInBackground(Object... params) {
			del_item = (Integer) params[2];
			return application.disagreeFriend((String)params[0], (String)params[1]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					ToastUtil.makeText(activity, "拒绝好友添加请求成功", Toast.LENGTH_SHORT);
				} else {
					ToastUtil.makeText(activity, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT);
				}
				listitems1.remove(del_item);
				notifyDataSetChanged();
			}
		}
	}
}
