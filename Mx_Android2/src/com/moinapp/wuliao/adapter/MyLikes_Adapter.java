package com.moinapp.wuliao.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.MyComments_Content_Model;
import com.moinapp.wuliao.model.MyStar_Content_Model;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyLikes_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<MyStar_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public MyLikes_Adapter(Context context, ArrayList<MyStar_Content_Model> listitems) {
		this.context = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(context);
		// this.bmpManager = new BitmapManager();
//		imageLoader = ImageLoader.getInstance();
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
			convertView = listParentContainer.inflate(R.layout.mylikes_listitem_layout, null);
		}

		TextView mylikes_listitem_time = ViewHolder.get(convertView, R.id.mylikes_listitem_time);
		TextView mylikes_listitem_view_title = ViewHolder.get(convertView, R.id.mylikes_listitem_view_title);
		TextView mylikes_listitem_view_tag = ViewHolder.get(convertView, R.id.mylikes_listitem_view_tag);

		MyStar_Content_Model item = listitems.get(position);
		mylikes_listitem_time.setText(item.getAddtime());
		mylikes_listitem_view_title.setText(item.getTitle());
		
		if(item.getAbout_type().equals(M_Constant.FACE)) {
			mylikes_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_face);
			mylikes_listitem_view_tag.setText("表情");
		}
		else if(item.getAbout_type().equals(M_Constant.NEWS)) {
			mylikes_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_news);
			mylikes_listitem_view_tag.setText("新闻");
		}
		else if(item.getAbout_type().equals(M_Constant.GAME)) {
			mylikes_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_news);
			mylikes_listitem_view_tag.setText("游戏");
		}
		else if(item.getAbout_type().equals(M_Constant.VIDEO)) {
			mylikes_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_face);
			mylikes_listitem_view_tag.setText("视频");
		}
		
//		MainTimelineItem_Model item = listitems.get(position);
//		// bmpManager.loadBitmap(item.getImgPath(),
//		// holder.k_listview_item_image, DensityUtil.dip2px(context,
//		// context.getResources().getDimension(R.dimen.k_listview_item_image_width)),
//		// DensityUtil.dip2px(context,
//		// context.getResources().getDimension(R.dimen.k_listview_item_image_height)));
//		main_adapter_item_title.setText(item.getTitle());
//		main_adapter_item_time.setText(item.getAddtime());
//		String type = item.getAbout_type();
//		if ("news".equals(type))
//			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_xw);
//		else if ("video".equals(type))
//			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_bf);
//		else if ("face".equals(type))
//			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_bq);
//		else if ("game".equals(type))
//			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_yx);
//		imageLoader.displayImage(item.getPic(), main_adapter_item_image, BitmapUtil.getImageLoaderOption());

		return convertView;
	}

}
