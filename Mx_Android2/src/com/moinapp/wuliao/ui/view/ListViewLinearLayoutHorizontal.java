package com.moinapp.wuliao.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 自定义的ListView类型控件，解决了ScrollView与ListView的滑动冲突问题
 * @author Administrator
 *
 */
public class ListViewLinearLayoutHorizontal extends LinearLayout{

	private BaseAdapter adapter;
    private OnClickListener onClickListener;

    public ListViewLinearLayoutHorizontal(Context context) {
            super(context);
    }

    public ListViewLinearLayoutHorizontal(Context context, AttributeSet attrs) {
            super(context, attrs);
            setOrientation(HORIZONTAL);
    }

    /*
     * 绑定布局
     */
    public void bindLinearLayout() {
            int count = adapter.getCount();
            removeAllViews();
            for (int i = 0; i < count; i++) {
                    View v = adapter.getView(i, null, null);
                    v.setOnClickListener(this.onClickListener);
                    v.setTag(i);
                    addView(v, i);
            }
    }

    public BaseAdapter getAdapter() {
            return adapter;
    }

    public void setAdapter(BaseAdapter adapter) {
            this.adapter = adapter;
            bindLinearLayout();
    }

    public OnClickListener getOnClickListener() {
            return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
    }


}
