package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moinapp.wuliao.activity.SendGifts_Activity;
import com.moinapp.wuliao.model.Friend_Content_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendGift_List_Adapter extends BaseAdapter {

	private Activity context;

	// 数据集合
	private List<Friend_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public FriendGift_List_Adapter(Activity context, ArrayList<Friend_Content_Model> listitems) {
		this.context = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(context);
		// this.bmpManager = new BitmapManager();
		imageLoader = ImageLoader.getInstance();
	}

	/*
	 * ListView列表的条数 name: getCount
	 * 
	 * @param
	 * 
	 * @return
	 */
	public int getCount() {
//		return listitems.size();
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
	 * 
	 * @param
	 * 
	 * @return
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = listParentContainer.inflate(R.layout.sendgift_listitem, null);
		}
		
		ImageView item_avatar = ViewHolder.get(convertView, R.id.friend_avatar);
		TextView item_username = ViewHolder.get(convertView, R.id.friend_username);
		ImageView item_gender = ViewHolder.get(convertView, R.id.friend_gender);
		TextView item_location = ViewHolder.get(convertView, R.id.friend_location);
		ImageView item_sendgift = ViewHolder.get(convertView, R.id.item_sendgift);
		
		

		final Friend_Content_Model data = listitems.get(position);
		
		imageLoader.displayImage(data.getAvatar(), item_avatar, BitmapUtil.getImageLoaderOption());
		item_username.setText(data.getNickname());
		if (data.getSex().equals("女")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_women);
		} else if (data.getSex().equals("男")) {
			item_gender.setBackgroundResource(R.drawable.mystar_sex_man);
		} 
		
		item_sendgift.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getId());
				bundle.putString("avatar", data.getAvatar());
				bundle.putString("nickname", data.getNickname());
				bundle.putString("gender", data.getSex());
				Intent intent = new Intent(context, SendGifts_Activity.class);
				intent.putExtras(bundle);
				context.setResult(context.RESULT_OK, intent);
				context.finish();
			}
		});

		return convertView;
	}

}
