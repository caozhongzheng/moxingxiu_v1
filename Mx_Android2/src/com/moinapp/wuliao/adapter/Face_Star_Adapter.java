package com.moinapp.wuliao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.moinapp.wuliao.R;

public class Face_Star_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private int num;

	// listitem的视图容器
	private LayoutInflater listParentContainer;


	public Face_Star_Adapter(Context context, int num) {
		this.context = context;
		this.listParentContainer = LayoutInflater.from(context);
		this.num = num;
	}

	/*
	 * ListView列表的条数 name: getCount
	 * 
	 * @param
	 * 
	 * @return
	 */
	public int getCount() {
		return num;
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
			convertView = listParentContainer.inflate(R.layout.face_star_listitem_layout, null);
		}

		return convertView;
	}
}
