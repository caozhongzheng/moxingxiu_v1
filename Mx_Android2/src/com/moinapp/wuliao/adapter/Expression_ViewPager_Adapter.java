package com.moinapp.wuliao.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class Expression_ViewPager_Adapter extends PagerAdapter {
	private List<View> mLists;

	public Expression_ViewPager_Adapter(List<View> mLists) {
		this.mLists = mLists;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mLists.get(position));
		return mLists.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public int getCount() {
		return mLists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}
