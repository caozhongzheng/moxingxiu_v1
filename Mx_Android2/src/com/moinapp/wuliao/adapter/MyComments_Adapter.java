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
import com.moinapp.wuliao.activity.Expression_Activity;
import com.moinapp.wuliao.activity.Game_Activity;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.activity.Video_Activity;
import com.moinapp.wuliao.model.MyComments_Content_Model;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyComments_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<MyComments_Content_Model> listitems;
	private String nickname;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public MyComments_Adapter(Context context, ArrayList<MyComments_Content_Model> listitems, String nickname) {
		this.context = context;
		this.listitems = listitems;
		this.listParentContainer = LayoutInflater.from(context);
		this.nickname = nickname;
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
			convertView = listParentContainer.inflate(R.layout.mycomments_listitem_layout, null);
		}

		TextView mycomments_listitem_nickname = ViewHolder.get(convertView, R.id.mycomments_listitem_nickname);
		TextView mycomments_listitem_time = ViewHolder.get(convertView, R.id.mycomments_listitem_time);
		TextView mycomments_listitem_content = ViewHolder.get(convertView, R.id.mycomments_listitem_content);
		TextView mycomments_listitem_view_title = ViewHolder.get(convertView, R.id.mycomments_listitem_view_title);
		TextView mycomments_listitem_view_tag = ViewHolder.get(convertView, R.id.mycomments_listitem_view_tag);

		MyComments_Content_Model item = listitems.get(position);
		mycomments_listitem_nickname.setText(nickname);
		mycomments_listitem_time.setText(item.getAddtime());
		mycomments_listitem_content.setText(item.getContent());
		mycomments_listitem_view_title.setText(item.getTitle());
		
		if(item.getAbout_type().equals(M_Constant.FACE)) {
			mycomments_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_face);
			mycomments_listitem_view_tag.setText("表情");
		}
		else if(item.getAbout_type().equals(M_Constant.NEWS)) {
			mycomments_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_news);
			mycomments_listitem_view_tag.setText("新闻");
		}
		else if(item.getAbout_type().equals(M_Constant.GAME)) {
			mycomments_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_news);
			mycomments_listitem_view_tag.setText("游戏");
		}
		else if(item.getAbout_type().equals(M_Constant.VIDEO)) {
			mycomments_listitem_view_tag.setBackgroundResource(R.drawable.mycomments_video);
			mycomments_listitem_view_tag.setText("视频");
		}
		
		
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
