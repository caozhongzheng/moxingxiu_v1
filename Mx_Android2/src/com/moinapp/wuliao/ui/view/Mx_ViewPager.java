package com.moinapp.wuliao.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

// 屏蔽左右滑动的viewpager
public class Mx_ViewPager extends ViewPager{

	public Mx_ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return true;
	}
}
