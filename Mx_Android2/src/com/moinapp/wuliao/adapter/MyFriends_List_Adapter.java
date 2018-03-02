package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.activity.UserData_Activity;
import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyFriends_List_Adapter extends BaseAdapter {

	private Activity mContext;

	// 数据集合
	private List<Friend_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public MyFriends_List_Adapter(Activity context, ArrayList<Friend_Content_Model> listitems) {
		this.mContext = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(mContext);
		// this.bmpManager = new BitmapManager();
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

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}

	/*
	 * ListItem的View name: getView
	 * @param
	 * @return
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = listParentContainer.inflate(R.layout.myfriends_listitem, null);
		}
		
		ImageView item_avatar = ViewHolder.get(convertView, R.id.friend_avatar);
		TextView item_nickname = ViewHolder.get(convertView, R.id.friend_nickname);
		ImageView item_gender = ViewHolder.get(convertView, R.id.friend_gender);
		TextView item_location = ViewHolder.get(convertView, R.id.friend_location);
		TextView item_detail = ViewHolder.get(convertView, R.id.friend_detail);
		
		final Friend_Content_Model data = listitems.get(position);
		
		imageLoader.displayImage(data.getAvatar(), item_avatar, BitmapUtil.getImageLoaderOption());
		item_nickname.setText(data.getNickname());
		if (data.getSex().equals("女")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_women);
		} else if (data.getSex().equals("男")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_man);
		} 
		item_location.setText(data.getAddr());
		
		item_detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("friend", data);
				bundle.putString("from", "myfriend");
				bundle.putBoolean("isFriend", true);
				AppTools.toIntent(mContext, bundle, UserData_Activity.class, 1);
			}
		});
		return convertView;
	}
}
