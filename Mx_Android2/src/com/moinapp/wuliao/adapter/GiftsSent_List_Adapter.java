package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.OpenGift_List_Model;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class GiftsSent_List_Adapter extends BaseAdapter {

	private Context mContext;

	// 数据集合
	private List<OpenGift_List_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public GiftsSent_List_Adapter(Context context, ArrayList<OpenGift_List_Model> listitems) {
		this.mContext = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(mContext);
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
			convertView = listParentContainer.inflate(R.layout.gift_listitem, null);
		}
		
		TextView gift_type = ViewHolder.get(convertView, R.id.gift_type);
		TextView gift_nickname = ViewHolder.get(convertView, R.id.gift_nickname);
		TextView gift_time = ViewHolder.get(convertView, R.id.gift_time);
		TextView gift_detail = ViewHolder.get(convertView, R.id.gift_detail);
		TextView open_gift = ViewHolder.get(convertView, R.id.open_gift);
		
		final OpenGift_List_Model data = listitems.get(position);
		
		gift_type.setText(mContext.getResources().getString(R.string.to));
		gift_nickname.setText(data.getNickname());
		Long timestamp = Long.parseLong(data.getAddtime());	
		gift_time.setText(M_Constant.sdf_custom.format(new Date(timestamp * 1000)));
		gift_detail.setText(data.getAbout_value() + "魔币大礼包！");
		open_gift.setVisibility(View.GONE);
//		open_gift.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("gift", data);
//				AppTools.toIntent(mContext, bundle, OpenGift_Activity.class);
//			}
//		});
		
		return convertView;
	}

}
