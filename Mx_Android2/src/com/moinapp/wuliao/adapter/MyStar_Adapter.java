package com.moinapp.wuliao.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.model.MyStar_Content_Model;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ViewHolder;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyStar_Adapter extends BaseAdapter {

	private Context context;

	// 数据集合
	private List<MyStar_Content_Model> listitems;

	// listitem的视图容器
	private LayoutInflater listParentContainer;

	// private BitmapManager bmpManager;
	private ImageLoader imageLoader;

	public MyStar_Adapter(Context context, ArrayList<MyStar_Content_Model> listitems) {
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
			convertView = listParentContainer.inflate(R.layout.mystar_listitem_layout, null);
		}

		RelativeLayout mystar_ls_l = ViewHolder.get(convertView, R.id.mystar_ls_l);
		LinearLayout mystar_listitem_layout = ViewHolder.get(convertView, R.id.mystar_listitem_layout);
		TextView mystar_listitem_msg = ViewHolder.get(convertView, R.id.mystar_listitem_msg);
		TextView mystar_listitem_msg_time = ViewHolder.get(convertView, R.id.mystar_listitem_msg_time);
		ImageView ls_point = ViewHolder.get(convertView, R.id.ls_point);
		LinearLayout mystar_listitem_msg_time_layout = ViewHolder.get(convertView, R.id.mystar_listitem_msg_time_layout);
//		LinearLayout mystar_faca_time = ViewHolder.get(convertView, R.id.mystar_faca_time);
		TextView mystar_listitem_time = ViewHolder.get(convertView, R.id.mystar_listitem_time);
		ImageView mystar_listitem_image = ViewHolder.get(convertView, R.id.mystar_listitem_image);
		TextView mystar_listitem_title = ViewHolder.get(convertView, R.id.mystar_listitem_title);
		ImageView mystar_listitem_type = ViewHolder.get(convertView, R.id.mystar_listitem_type);
		TextView mystar_listitem_comment = ViewHolder.get(convertView, R.id.mystar_listitem_comment);
		
		TextView mystar_year = ViewHolder.get(convertView, R.id.mystar_year);
		TextView mystar_month = ViewHolder.get(convertView, R.id.mystar_month);
		
		MyStar_Content_Model data = listitems.get(position);
		if(data.getAction_type().equals(M_Constant.ACTION_LOGIN)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.TRADE)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.SEND_GIFT)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.RECEIVE_GIFT)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.UPDATE_USER_INFO)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.VIEW) || data.getAction_type().endsWith(M_Constant.SHARE)) {
			mystar_ls_l.setVisibility(View.VISIBLE);
			mystar_listitem_layout.setVisibility(View.GONE);
			ls_point.setVisibility(View.GONE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.VISIBLE);
			
			mystar_listitem_title.setText(data.getTitle());
			imageLoader.displayImage(data.getPic(), mystar_listitem_image, BitmapUtil.getImageLoaderOption());
			mystar_listitem_comment.setText(data.getReply_num());
			
			String type = data.getAbout_type();
			if ("news".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.news_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_xw);
			}
			else if ("video".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.video_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_bf);
			}
			else if ("face".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.face_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_bq);
			}
			else if ("game".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.game_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_yx);
			}
		} else if(data.getAction_type().equals(M_Constant.SIGNIN_MYSTAR)) {
			mystar_ls_l.setVisibility(View.GONE);
			mystar_listitem_layout.setVisibility(View.VISIBLE);
			ls_point.setVisibility(View.VISIBLE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.GONE);
		} else if(data.getAction_type().equals(M_Constant.LIKE)) {
			mystar_ls_l.setVisibility(View.VISIBLE);
			mystar_listitem_layout.setVisibility(View.GONE);
			ls_point.setVisibility(View.GONE);
			mystar_listitem_msg_time_layout.setVisibility(View.VISIBLE);
//			mystar_faca_time.setVisibility(View.VISIBLE);
			
			mystar_listitem_title.setText(data.getTitle());
			imageLoader.displayImage(data.getPic(), mystar_listitem_image, BitmapUtil.getImageLoaderOption());
			mystar_listitem_comment.setText(data.getReply_num());
			
			String type = data.getAbout_type();
			if ("news".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.news_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_xw);
			}
			else if ("video".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.video_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_bf);
			}
			else if ("face".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.face_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_bq);
			}
			else if ("game".equals(type)) {
//				mystar_ls_l.setBackgroundResource(R.drawable.game_ls_right);
				mystar_listitem_type.setImageResource(R.drawable.main_adapter_item_yx);
			}
		}
		
		mystar_listitem_msg.setText(data.getAction());
		SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm:ss");
		try {
			mystar_year.setText(sdf1.format(sdf0.parse(data.getAddtime())));
			mystar_month.setText(sdf2.format(sdf0.parse(data.getAddtime())));
			
			mystar_listitem_msg_time.setText(sdf3.format(sdf0.parse(data.getAddtime())));
			mystar_listitem_time.setText(sdf3.format(sdf0.parse(data.getAddtime())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return convertView;
	}
}
