package com.moinapp.wuliao.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.moinapp.wuliao.model.FaceDetail_Content_Faces_List;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Expression_GridView_Adapter extends BaseAdapter {
    //上下文对象 
    private Context mContext; 
    private LayoutInflater inflater; 
    private ArrayList<FaceDetail_Content_Faces_List> data;
    private ImageLoader imageLoader;
    public static final int PAGE_SIZE = 9; // 每一屏幕显示28 Button
        
    public Expression_GridView_Adapter(Context context, ArrayList<FaceDetail_Content_Faces_List> datas, int page) { 
    	this.mContext = context; 
    	inflater = LayoutInflater.from(mContext); 
    	imageLoader = ImageLoader.getInstance();
    	data = new ArrayList<FaceDetail_Content_Faces_List>();
    	int i = page * PAGE_SIZE;
		int end = i + PAGE_SIZE;
		while ((i < datas.size()) && (i < end)) {
			data.add(datas.get(i));
			i++;
		}
    } 
    
    public int getCount() { 
    	return data.size(); 
    } 
    
    public Object getItem(int item) { 
    	return item; 
    } 
 
    public long getItemId(int id) { 
    	return id; 
    } 
         
    //创建View方法 
    public View getView(int position, View convertView, ViewGroup parent) { 
    	ViewHolder viewHolder; 
        if (convertView == null) { 
        	convertView = inflater.inflate(R.layout.expression_gridview_item, null); 
        	viewHolder = new ViewHolder(); 
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image); 
            convertView.setTag(viewHolder); 
        }  
        else { 
        	viewHolder = (ViewHolder) convertView.getTag(); 
        } 
        imageLoader.displayImage(data.get(position).getThumb(), viewHolder.image, BitmapUtil.getImageLoaderOption());
        return convertView; 
    } 
    
    class ViewHolder { 
        public ImageView image; 
    } 
}
