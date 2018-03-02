package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moinapp.wuliao.model.SearchHotWords_Content_Model;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;

public class AutoLayout_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<SearchHotWords_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;
	private AutoLayout_Callback mCallback;


	public AutoLayout_Adapter(Context context, ArrayList<SearchHotWords_Content_Model> listitems, AutoLayout_Callback mCallback) {
		this.context = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(context);
		this.mCallback = mCallback;
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
			convertView = listParentContainer.inflate(R.layout.autolayout_item, null);
		}

		TextView auto_textview = ViewHolder.get(convertView, R.id.auto_textview);
		final String hotWord = listitems.get(position).getWord();
		auto_textview.setText(hotWord);
		auto_textview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mCallback.click(hotWord);
			}
		});
		
		return convertView;
	}
	
	
	/**
	 * 自定义接口，用于回调按钮点击事件到Activity
	 */
	public interface AutoLayout_Callback {
	    public void click(String word);
	}


}
