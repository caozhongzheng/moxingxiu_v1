package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.activity.Expression_Activity;
import com.moinapp.wuliao.activity.Game_Activity;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.activity.Video_Activity;
import com.moinapp.wuliao.model.Ad_Content_Model;
import com.moinapp.wuliao.ui.view.VerticalViewPager;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BannerViewPagerAdapter extends PagerAdapter {

	private Context mContext;

	private ArrayList<Ad_Content_Model> mJsonArray;

	private HashMap<Integer, ImageView> mHashMap;
	private ImageLoader imageLoader;

	public BannerViewPagerAdapter(Context context, ArrayList<Ad_Content_Model> arrays) {
		this.mContext = context;
		this.mJsonArray = arrays;
		mHashMap = new HashMap<Integer, ImageView>();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// ImageView itemView = (ImageView)object;
		// itemView.recycle();
	}

	@Override
	public void finishUpdate(View view) {

	}

	@Override
	public int getCount() {
		return mJsonArray.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		ImageView itemView;
		final Ad_Content_Model data = mJsonArray.get(position);
		if (mHashMap.containsKey(position)) {
			itemView = mHashMap.get(position);
		} else {
			itemView = new ImageView(mContext);
			itemView.setBackgroundColor(Color.WHITE);
			itemView.setScaleType(ScaleType.FIT_XY);
			imageLoader.displayImage(data.getPic(), itemView, BitmapUtil.getImageLoaderOption1());
			mHashMap.put(position, itemView);
			((VerticalViewPager) container).addView(itemView);
		}
		itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if (data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(mContext, bundle, Expression_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(mContext, bundle, News_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(mContext, bundle, Game_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(mContext, bundle, Video_Activity.class);
			}
		});

		return itemView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View view) {

	}
}
