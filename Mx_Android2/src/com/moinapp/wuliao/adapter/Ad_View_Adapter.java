package com.moinapp.wuliao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moinapp.wuliao.R;
import com.moinapp.wuliao.model.Recommend_Content_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class Ad_View_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<Recommend_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public Ad_View_Adapter(Context context, ArrayList<Recommend_Content_Model> listitems) {
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
			convertView = listParentContainer.inflate(R.layout.ad_listitem, null);
		}

		ImageView ad1_iv = ViewHolder.get(convertView, R.id.ad1_iv);
//		TextView ad1_title = ViewHolder.get(convertView, R.id.ad1_title);
//		TextView ad1_download_number = ViewHolder.get(convertView, R.id.ad1_download_number);
//		RatingBar room_ratingbar = ViewHolder.get(convertView, R.id.room_ratingbar);
		
		Recommend_Content_Model data = listitems.get(position);
		
		imageLoader.displayImage(data.getPic(), ad1_iv, BitmapUtil.getImageLoaderOption1());
//		ad1_title.setText(data.getTitle());
//		ad1_download_number.setText(data.getDownload_num());
//		room_ratingbar.setRating(Integer.parseInt(data.getStar_num()));

		return convertView;
	}
}
