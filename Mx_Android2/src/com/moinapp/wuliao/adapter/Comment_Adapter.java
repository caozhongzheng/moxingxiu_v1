package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.Map;

import u.aly.da;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
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
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.model.Reply_Content_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ToastUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Comment_Adapter extends BaseAdapter {

	private M_Application application;
	private LayoutInflater li;
	private ArrayList<Reply_Content_Model> list;
	private ImageLoader imageLoader;
	private Activity activity;
	private String uid;
	private InotifyReply nr;
	private IdeleteReply del;

	public Comment_Adapter(Activity context, ArrayList<Reply_Content_Model> list, InotifyReply nr, IdeleteReply del) {
		activity = context;
		this.li = LayoutInflater.from(activity);
		this.list = list;
		imageLoader = ImageLoader.getInstance();
		application = (M_Application) activity.getApplication();
		uid = application.getUid();
		this.nr = nr;
		this.del = del;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = li.inflate(R.layout.comment_item, null);
		}

		ImageView user_icon = ViewHolder.get(convertView, R.id.comment_user_icon);
		TextView user_name = ViewHolder.get(convertView, R.id.comment_username);
		TextView reply_time = ViewHolder.get(convertView, R.id.comment_time);
		TextView reply_content = ViewHolder.get(convertView, R.id.comment_content);
		final TextView agree_num = ViewHolder.get(convertView, R.id.comment_agree);
		final TextView disagree_num = ViewHolder.get(convertView, R.id.comment_disagree);
		TextView reply = ViewHolder.get(convertView, R.id.comment_reply);
		TextView reply_delete = ViewHolder.get(convertView, R.id.comment_delete);
		TextView add_friend = ViewHolder.get(convertView, R.id.add_friend);

		final Reply_Content_Model data = list.get(position);
		
//		final String state = data.getIsFriend();
//		if (!application.getUid().equals("0")) {
//			if (data.getIsFriend().equals("0")) {
//				add_friend.setVisibility(View.VISIBLE);
//			} 
//			
//			if (uid.equals(data.getUid())) {
//				add_friend.setVisibility(View.INVISIBLE);
//			}
//		}
		
		if (!application.getUid().equals("0")) {
			if (data.getUid().equals(application.getUid())) {
				reply_delete.setVisibility(View.VISIBLE);
			}
		}

		imageLoader.displayImage(data.getAvatar(), user_icon, BitmapUtil.getImageLoaderOption());
		user_name.setText(data.getNickname());
		reply_time.setText(data.getAddtime());
		reply_content.setText(data.getContent());
		agree_num.setText("(" + data.getGood_num() + ")");
		disagree_num.setText("(" + data.getBad_num() + ")");
		final String fid_to = data.getUid();

		if (application.getUid() != null) {

		}
		agree_num.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (uid != null && !"0".equals(uid) && !"".equals(uid)) {
					new ReplySet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, data.getId(), "good", agree_num,
							Integer.parseInt(data.getGood_num()));
					// agree_num.setText("(" +
					// (Integer.parseInt(data.getGood_num()) + 1) + ")");
				} else {
					ToastUtil.makeText(activity, R.string.not_login_comment, Toast.LENGTH_SHORT);
				}
			}
		});
		disagree_num.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (uid != null && !"0".equals(uid) && !"".equals(uid)) {
					new ReplySet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, data.getId(), "bad", disagree_num,
							Integer.parseInt(data.getGood_num()));
					// disagree_num.setText("(" +
					// (Integer.parseInt(data.getGood_num()) + 1) + ")");
				} else {
					ToastUtil.makeText(activity, R.string.not_login_comment, Toast.LENGTH_SHORT);
				}
			}
		});
		reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nr.notifyReply(data.getUid(), data.getNickname(), data.getContent());
			}
		});
		reply_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				del.deleteReply(data.getId());;
			}
		});
		return convertView;
	}
	
	public interface InotifyReply {
		public void notifyReply(String reply_id, String reply_nickname, String reply_content);
	}

	private void good_and_bad(TextView agree_num, int i) {

	}
	
	public class ReplySet_Task extends AsyncTask<Object, Void, Map> {
		private TextView count_text;
		private String type;
		private int num;

		@Override
		protected Map doInBackground(Object... params) {
			type = (String) params[2];
			count_text = (TextView) params[3];
			num = (Integer) params[4];
			Map data = null;
			try {
				data = application.replySet(params[0], params[1], params[2]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					if (type.endsWith("good")) {
						count_text.setText("(" + (num + 1) + ")");
						Drawable d = activity.getResources().getDrawable(R.drawable.agree_filled);  
						d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());  
						count_text.setCompoundDrawables(d, null, null, null); 
					} else {
						count_text.setText("(" + (num + 1) + ")");
						Drawable d = activity.getResources().getDrawable(R.drawable.disagree_filled);  
						d.setBounds(0, 0, d.getMinimumWidth(), d.getMinimumHeight());  
						count_text.setCompoundDrawables(d, null, null, null); 
					}
				} else {
					ToastUtil.makeText(activity, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT);
				}
			}
		}
	}

	public class AddFriend_Task extends AsyncTask<Object, Void, Map> {
		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
				data = application.addFriend((String) params[0], (String) params[1], (String) params[2]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					ToastUtil.makeText(activity, "添加好友发送成功", Toast.LENGTH_SHORT);
				} else {
					ToastUtil.makeText(activity, "添加好友已发送，等待对方处理", Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	public interface IdeleteReply {
		public void deleteReply(String reply_id);
	}

}