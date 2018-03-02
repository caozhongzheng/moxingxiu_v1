package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SelectFriend_List_Adapter extends BaseAdapter {

	private Activity mContext;

	// 数据集合
	private List<Friend_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;
	
	private ImageView[] btnArr;
	private Activity activity;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;
	private int temp = -1;
	private int clickItem = -1;

	public SelectFriend_List_Adapter(Activity context, ArrayList<Friend_Content_Model> listitems) {
		this.mContext = context;
		this.activity = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(mContext);
		imageLoader = ImageLoader.getInstance();
		setData();
	}
	
	private void setData() {
		btnArr = new ImageView[listitems.size()];
		for (int i = 0; i < listitems.size(); i++) {
			btnArr[i] = new ImageView(mContext);
//			btnArr[i].setTag(i);
		}
	}
	
	// 设置当前点击的item
	public int getClickItem() {
		return clickItem;
	}
	public void setClickItem(int clickItem) {
		this.clickItem = clickItem;
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
			convertView = listParentContainer.inflate(R.layout.select_friend_listitem, null);
		}
		
		ImageView item_avatar = ViewHolder.get(convertView, R.id.friend_avatar);
		TextView item_username = ViewHolder.get(convertView, R.id.friend_nickname);
		ImageView item_gender = ViewHolder.get(convertView, R.id.friend_gender);
		TextView item_location = ViewHolder.get(convertView, R.id.friend_location);
//		ImageView select = ViewHolder.get(convertView, R.id.friend_select);
		RadioButton select = ViewHolder.get(convertView, R.id.friend_select);
		
		final Friend_Content_Model data = listitems.get(position);
		
		imageLoader.displayImage(data.getAvatar(), item_avatar, BitmapUtil.getImageLoaderOption());
		item_username.setText(data.getNickname());
		if (data.getSex().equals("女")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_women);
		} else if (data.getSex().equals("男")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_man);
		} 
		item_location.setText(data.getAddr());
		
		final int position_v = position;
		select.setId(position);
		select.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					if (temp != -1) {
						RadioButton tempBtn = (RadioButton) activity.findViewById(temp);
						if (tempBtn != null) {
							tempBtn.setChecked(false);
						}
					}
					temp = buttonView.getId();
					
					if (position_v == temp) {
						setClickItem(position_v);
					}
				}
			}
		});
		return convertView;
	}

}
