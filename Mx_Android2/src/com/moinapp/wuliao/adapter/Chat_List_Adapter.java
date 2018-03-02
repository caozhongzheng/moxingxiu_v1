package com.moinapp.wuliao.adapter;


import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.HistoryMessage_Content_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Chat_List_Adapter extends BaseAdapter {

	private M_Application application;
	private Activity activity;
	// 数据集合
	private ArrayList<HistoryMessage_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	private ImageLoader imageLoader;
	final int TYPE_LEFT = 0;
	final int TYPE_RIGHT = 1;

	public Chat_List_Adapter(Activity context, ArrayList<HistoryMessage_Content_Model> listitems) {
		activity = context;
		application = (M_Application) activity.getApplication();
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(activity);
		imageLoader = ImageLoader.getInstance();
	}

	/*
	 * ListView列表的条数 name: getCount
	 * @param
	 * @return
	 */
	public int getCount() {
		return listitems.size();
	}

	public Object getItem(int position) {
		return listitems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	//每个convertView都会调用此方法，获得当前所需的view样式
	@Override
	public int getItemViewType(int position) {
		if (listitems.get(position).getFrom().equals(application.getUid())) {
			return TYPE_LEFT;
		} else {
			return TYPE_RIGHT;
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
		
		HistoryMessage_Content_Model data = listitems.get(position);
		int viewType = getItemViewType(position);
		if (convertView == null) {
			switch (viewType) {
			case TYPE_LEFT:
				convertView = listParentContainer.inflate(R.layout.chat_item_left, null);
				break;
			case TYPE_RIGHT:
				convertView = listParentContainer.inflate(R.layout.chat_item_right, null);
				break;
			}
		} 
		
		ImageView chat_avatar_left = ViewHolder.get(convertView, R.id.chat_user_avatar_left);
		TextView chat_content_left = ViewHolder.get(convertView, R.id.chat_content_left);
		TextView chat_time_left = ViewHolder.get(convertView, R.id.chat_time_left);
		
		ImageView chat_avatar_right = ViewHolder.get(convertView, R.id.chat_user_avatar_right);
		TextView chat_content_right = ViewHolder.get(convertView, R.id.chat_content_right);
		TextView chat_time_right = ViewHolder.get(convertView, R.id.chat_time_right);
		
		switch (viewType) {
		case TYPE_LEFT:
			imageLoader.displayImage(application.getLogin_model().getAvatar(), chat_avatar_left, BitmapUtil.getImageLoaderOption());
			chat_content_left.setText(data.getMessage());
			try {
				chat_time_left.setText(M_Constant.sdf_chat.format(M_Constant.sdf_standard.parse(data.getAddtime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		case TYPE_RIGHT:
			imageLoader.displayImage(data.getFrom_avatar(), chat_avatar_right, BitmapUtil.getImageLoaderOption());
			chat_content_right.setText(data.getMessage());
			try {
				chat_time_right.setText(M_Constant.sdf_chat.format(M_Constant.sdf_standard.parse(data.getAddtime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			break;
		}
		return convertView;
	}
}