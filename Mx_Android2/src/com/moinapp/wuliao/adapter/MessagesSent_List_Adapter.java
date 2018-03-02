package com.moinapp.wuliao.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.HistoryMessage_Content_Model;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MessagesSent_List_Adapter extends BaseAdapter {

	private Context mContext;

	// 数据集合
	private List<HistoryMessage_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	private ImageLoader imageLoader;
	
	public MessagesSent_List_Adapter(Context context, ArrayList<HistoryMessage_Content_Model> listitems) {
		this.mContext = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(mContext);
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
	
	/*
	 * ListItem的View name: getView
	 * @param
	 * @return
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = listParentContainer.inflate(R.layout.mymessages_message_listitem, null);
		} 
		
		TextView message_type = ViewHolder.get(convertView, R.id.message_type);
		TextView message_nickname = ViewHolder.get(convertView, R.id.message_nickname);
		TextView message_time = ViewHolder.get(convertView, R.id.message_time);
		TextView message_detail = ViewHolder.get(convertView, R.id.message_detail);
		
		HistoryMessage_Content_Model data = listitems.get(position);
		if (data != null) {
			message_type.setText(mContext.getResources().getString(R.string.to));
			message_nickname.setText(data.getTo_nickname());
			try {
				message_time.setText(M_Constant.sdf_custom.format(M_Constant.sdf_standard.parse(data.getAddtime())));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			message_detail.setText(data.getMessage());
		}
		
		return convertView;
	}

}
