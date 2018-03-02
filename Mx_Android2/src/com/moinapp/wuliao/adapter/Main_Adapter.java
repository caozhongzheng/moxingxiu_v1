package com.moinapp.wuliao.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moinapp.wuliao.model.MainTimelineItem_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Main_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<MainTimelineItem_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public Main_Adapter(Context context, ArrayList<MainTimelineItem_Model> listitems) {
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
			convertView = listParentContainer.inflate(R.layout.listitem_layout, null);
		}
		
		RelativeLayout ls_l = ViewHolder.get(convertView, R.id.ls_l);
		View marin_top_view = ViewHolder.get(convertView, R.id.marin_top_view_l);
		ImageView main_adapter_item_image = ViewHolder.get(convertView, R.id.main_adapter_item_image);
		ImageView main_adapter_item_image_la = ViewHolder.get(convertView, R.id.main_adapter_item_image_la);
		TextView main_adapter_item_title = ViewHolder.get(convertView, R.id.main_adapter_item_title);
		TextView main_adapter_item_time = ViewHolder.get(convertView, R.id.main_adapter_item_time);
		ImageView main_adapter_item_type = ViewHolder.get(convertView, R.id.main_adapter_item_type);
		TextView main_adapter_item_comment_num = ViewHolder.get(convertView, R.id.main_adapter_item_comment_num);
		TextView main_adapter_item_hits_num = ViewHolder.get(convertView, R.id.main_adapter_item_hits_num);

		MainTimelineItem_Model item = listitems.get(position);
		// bmpManager.loadBitmap(item.getImgPath(),
		// holder.k_listview_item_image, DensityUtil.dip2px(context,
		// context.getResources().getDimension(R.dimen.k_listview_item_image_width)),
		// DensityUtil.dip2px(context,
		// context.getResources().getDimension(R.dimen.k_listview_item_image_height)));
		main_adapter_item_title.setText(item.getTitle());
		main_adapter_item_comment_num.setText(item.getReply_num());
		main_adapter_item_hits_num.setText(item.getHits_num());
		
//		LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) marin_top_view.getLayoutParams(); //取控件textView当前的布局参数
//		int height = new Random().nextInt((int)context.getResources().getDimension(R.dimen.main_adapter2_top_bottom_margin));
//		int height = (int) context.getResources().getDimension(R.dimen.main_adapter2_top_bottom_margin);
//		linearParams.height =  height;// 控件的高强制设成20
//		linearParams.width = LayoutParams.WRAP_CONTENT;// 控件的宽强制设成30 

//		marin_top_view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
		
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM.dd");
		try {
			main_adapter_item_time.setText(sdf2.format(sdf0.parse(item.getAddtime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		String type = item.getAbout_type();
		if ("news".equals(type)) {
//			ls_l.setBackgroundResource(R.drawable.news_ls_left);
			main_adapter_item_image_la.setVisibility(View.INVISIBLE);
			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_xw);
		}
		else if ("video".equals(type)) {
//			ls_l.setBackgroundResource(R.drawable.video_ls_left);
			main_adapter_item_image_la.setVisibility(View.INVISIBLE);
			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_bf);
		}
		else if ("face".equals(type)) {
//			ls_l.setBackgroundResource(R.drawable.face_ls_left);
			main_adapter_item_image_la.setVisibility(View.VISIBLE);
			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_bq);
		}
		else if ("game".equals(type)) {
//			ls_l.setBackgroundResource(R.drawable.game_ls_left);
			main_adapter_item_image_la.setVisibility(View.INVISIBLE);
			main_adapter_item_type.setImageResource(R.drawable.main_adapter_item_yx);
		}
		imageLoader.displayImage(item.getPic(), main_adapter_item_image, BitmapUtil.getImageLoaderOption());

		return convertView;
	}

}
