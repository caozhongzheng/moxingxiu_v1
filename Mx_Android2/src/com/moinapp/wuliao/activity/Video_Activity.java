package com.moinapp.wuliao.activity;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.Comment_Adapter;
import com.moinapp.wuliao.adapter.Comment_Adapter.IdeleteReply;
import com.moinapp.wuliao.adapter.Comment_Adapter.InotifyReply;
import com.moinapp.wuliao.model.Recommend_Content_Model;
import com.moinapp.wuliao.model.Reply_Content_Model;
import com.moinapp.wuliao.model.Reply_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.model.VideoDetail_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.MyListView;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.ui.view.ResizeLayout;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Video_Activity extends Base_FragmentActivity implements InotifyReply, IdeleteReply {
	private MyPopWindow popupWindow;
	private ListView listview;
	private Comment_Adapter commentAdapter;
	private ImageView comment_iv;
	private CheckBox collect_iv;
	private boolean collect_initial = false;
	
	private ImageView ad_iv1, ad_iv2;
	private TextView ad_tv1, ad_tv2;
	private Recommend_Content_Model ad_data1, ad_data2;

	private M_Application application;
	private ImageLoader imageLoader;
	private Signin_User_Model login_model;
	private String uid;

	private TextView title_tv;
	private TextView collect_tv;
	private TextView date_tv, from_tv, to_editText, comment_send;
	private ImageView pic_iv;
	private EditText comment_et;
	private String about_type, about_id;
	private ArrayList<Reply_Content_Model> list;

//	private HorizontalListView ad_listview;
//	private Ad_View_Adapter ad_adapter;
//	private ArrayList<Recommend_Content_Model> ad_list;
	private final String pagesize = "10";
	private int page = 1;
	private int list_num;
	private String title, pic, source_url, share_url;
	
	private Boolean is_Reply = false;
	private String reply_to_uid;
	
	// ------------------
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;
	private static final int HEIGHT_THREADHOLD = 30;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE:
				if (msg.arg1 == BIGGER) {
					to_editText.setVisibility(View.VISIBLE);
					comment_et.setVisibility(View.GONE);
					comment_send.setVisibility(View.GONE);

					if ("".equals(comment_et.getText().toString()))
						to_editText.setText(comment_et.getHint().toString());
					else
						to_editText.setText(comment_et.getText().toString());
				} else {
				}
				break;
			case 10:
				likeSet(uid, about_type, about_id, M_Constant.SHARE);
			}
			
			super.handleMessage(msg);
		}

	};

	// ------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_layout);
		initData();
		initView();
		initPopuptWindow();

		new Video_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true);
		getReply(page, false);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = this.getIntent().getExtras();
		about_type = bundle.getString("about_type");
		about_id = bundle.getString("about_id");

		list = new ArrayList<Reply_Content_Model>();

		login_model = application.getLogin_model();
		uid = application.getUid();

//		ad_list = new ArrayList<Recommend_Content_Model>();
//		ad_adapter = new Ad_View_Adapter(this, ad_list);
	}

	private void initView() {
		// ---------------------------------------
		ResizeLayout layout = (ResizeLayout) findViewById(R.id.root_layout);
		layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

			public void OnResize(int w, int h, int oldw, int oldh) {
				int change = BIGGER;
				if (h < oldh) {
					change = SMALLER;
				}

				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = change;
				mHandler.sendMessage(msg);
			}
		});
		// ----------------------------------------
		
		commentAdapter = new Comment_Adapter(this, list, this, this);
		collect_iv = (CheckBox) findViewById(R.id.collect_iv);
		if("0".equals(uid) || uid == null || "".equals(uid)) {
			collect_iv.setChecked(false);
			collect_iv.setClickable(false);
		}
		
		listview = (MyListView) findViewById(R.id.video_lv);
		listview.setAdapter(commentAdapter);
		to_editText = (TextView) findViewById(R.id.to_editText);
		comment_et = (EditText) findViewById(R.id.comment_editText);
		comment_send = (TextView) findViewById(R.id.comment_send);
		
		comment_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (arg0.length() > 0) {
					comment_send.setBackgroundResource(R.drawable.comment_send_bg_red);
				} else {
					comment_send.setBackgroundResource(R.drawable.comment_send_bg_gray);
				}
			}
		});

		title_tv = (TextView) findViewById(R.id.video_title);
		date_tv = (TextView) findViewById(R.id.video_date);
		from_tv = (TextView) findViewById(R.id.video_from);
		pic_iv = (ImageView) findViewById(R.id.video_pic);
		collect_tv = (TextView) findViewById(R.id.video_collect);

		collect_iv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (collect_initial) {
					if(isChecked)
						likeSet(uid, about_type, about_id, M_Constant.LIKE);
					else
						likeSet(uid, about_type, about_id, M_Constant.HATE);
				}
			}
		});

//		ad_listview = (HorizontalListView) findViewById(R.id.ad_listview);
//		ad_listview.setAdapter(ad_adapter);
//
//		ad_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Recommend_Content_Model data = ad_list.get(position);
//				Bundle bundle = new Bundle();
//				bundle.putString("about_id", data.getAbout_id());
//				bundle.putString("about_type", data.getAbout_type());
//				if (data.getAbout_type().equals(M_Constant.FACE))
//					AppTools.toIntent(Video_Activity.this, bundle, Expression_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.NEWS))
//					AppTools.toIntent(Video_Activity.this, bundle, News_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.GAME))
//					AppTools.toIntent(Video_Activity.this, bundle, Game_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.VIDEO))
//					AppTools.toIntent(Video_Activity.this, bundle, Video_Activity.class);
//			}
//		});
		
		ad_iv1 = (ImageView) findViewById(R.id.ad1_iv);
		ad_tv1 = (TextView) findViewById(R.id.ad1_title);
		ad_iv2 = (ImageView) findViewById(R.id.ad2_iv);
		ad_tv2 = (TextView) findViewById(R.id.ad2_title);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			AppTools.showShare(this, false, null, title, title + share_url, pic, share_url, mHandler);
			break;
		case R.id.comment_send:
			String content = comment_et.getText().toString();
			if (!"".equals(content) && content != null && uid != null && !"".equals(uid)) {
				if (is_Reply) {
					new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, reply_to_uid);
				} else {
					new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, "");
				}
			}
			break;
		case R.id.to_editText:
			to_editText.setVisibility(View.GONE);
			comment_et.setVisibility(View.VISIBLE);
			comment_send.setVisibility(View.VISIBLE);
			opencollapseSoftInputMethod(comment_et);
			break;
		case R.id.video_pic:
			if(source_url != null && !"".equals(source_url)) {
				Bundle bundle = new Bundle();
				bundle.putString("url", source_url);
				AppTools.toIntent(this, bundle, VideoPlay_Activity.class);
			}
			break;
		case R.id.load_more:
			page ++ ;
			getReply(page, true);
			break;
		case R.id.ad1_iv:
			Bundle bundle1 = new Bundle();
			bundle1.putString("about_id", ad_data1.getAbout_id());
			bundle1.putString("about_type", ad_data1.getAbout_type());
			if (ad_data1.getAbout_type().equals(M_Constant.FACE))
				AppTools.toIntent(this, bundle1, Expression_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.NEWS))
				AppTools.toIntent(this, bundle1, News_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.GAME))
				AppTools.toIntent(this, bundle1, Game_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.VIDEO))
				AppTools.toIntent(this, bundle1, Video_Activity.class);
			break;
		case R.id.ad2_iv:
			Bundle bundle2 = new Bundle();
			bundle2.putString("about_id", ad_data2.getAbout_id());
			bundle2.putString("about_type", ad_data2.getAbout_type());
			if (ad_data2.getAbout_type().equals(M_Constant.FACE))
				AppTools.toIntent(this, bundle2, Expression_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.NEWS))
				AppTools.toIntent(this, bundle2, News_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.GAME))
				AppTools.toIntent(this, bundle2, Game_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.VIDEO))
				AppTools.toIntent(this, bundle2, Video_Activity.class);
			break;
		case R.id.announcement:
			AppTools.toIntent(this, Announcement_Activity.class);
			break;
		default:
			break;
		}
	}
	
	public void collapseSoftInputMethod(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		editText.clearFocus();
	}

	public void opencollapseSoftInputMethod(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}

	private void likeSet(String uid, String about_type, String about_id, String set) {
		new SendLikeSet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, set);
	}
	
	public void initPopuptWindow() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.share_layout, null);
		popupWindow = new MyPopWindow(Video_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView cancel = (TextView) popupWindow_view.findViewById(R.id.pop_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	private void getReply(int page, boolean isMore) {
		new GetReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, page + "", pagesize, isMore);
	}

	private class GetReply_Task extends AsyncTask<Object, Void, Reply_Model> {
		private boolean isMore;

		@Override
		protected Reply_Model doInBackground(Object... params) {
			isMore = (Boolean) params[5];
			Reply_Model data = null;
			try {
				data = application.getReply(true, params);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Reply_Model result) {
			if (result != null) {

				if (isMore) {
					if (result.getList().size() != 0) {
						list.addAll(result.getList());
						commentAdapter.notifyDataSetChanged();
					}
					else
						Toast.makeText(Video_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
				} else {
					list.clear();
					list.addAll(result.getList());
					
					commentAdapter.notifyDataSetChanged();
				}
				
			}

		}

	}

	private class Video_Task extends AsyncTask<Object, Void, VideoDetail_Model> {

		private boolean isRefresh;

		@Override
		protected VideoDetail_Model doInBackground(Object... params) {
			VideoDetail_Model data = null;
			isRefresh = (Boolean) params[0];
			try {
				data = application.getVideoData(isRefresh, about_type, about_id);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(VideoDetail_Model result) {
			if (result != null) {
				title_tv.setText(result.getDetail().getTitle());
				collect_tv.setText(result.getDetail().getLike_num());
				imageLoader.displayImage(result.getDetail().getBig_pic(), pic_iv, BitmapUtil.getImageLoaderOption());
				from_tv.setText(result.getDetail().getSource());
				date_tv.setText(result.getDetail().getUpdate_time());
				
				title = result.getDetail().getTitle();
				pic = result.getDetail().getPic();
				share_url = result.getDetail().getShare_url();
				
				source_url = result.getDetail().getSource_url();
				
				if(M_Constant.LIKE.equals(result.getDetail().getLikeSet()))
					collect_iv.setChecked(true);
				else
					collect_iv.setChecked(false);
				collect_initial = true;
				
				list_num = Integer.parseInt(result.getDetail().getLike_num());
				// int ad_num = result.getAdvert().getList().size();
				// for (int i = 0; i < ad_num; i++) {
				// imageLoader.displayImage(result.getAdvert().getList().get(i).getPic(),
				// ad_pics[i], BitmapUtil.getImageLoaderOption());
				// ad_titles[i].setText(result.getAdvert().getList().get(i).getTitle());
				// }

				if (result.getRecommend() != null && result.getRecommend().getList() != null) {
//					ad_list.addAll(result.getRecommend().getList());
//					ad_adapter.notifyDataSetChanged();
					
					ad_data1 = result.getRecommend().getList().get(1);
					ad_data2 = result.getRecommend().getList().get(2);
					imageLoader.displayImage(ad_data1.getPic(), ad_iv1, BitmapUtil.getImageLoaderOption());
					ad_tv1.setText(ad_data1.getTitle());
					imageLoader.displayImage(ad_data2.getPic(), ad_iv2, BitmapUtil.getImageLoaderOption());
					ad_tv2.setText(ad_data2.getTitle());
				}
			}
		}
	}

	public class SendReply_Task extends AsyncTask<Object, Void, Map> {
		private String content;

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
				content = (String) params[3];
				data = application.addReply(params[0], params[1], params[2], params[3], params[4]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					page = 1;
					getReply(page, false);
					collapseSoftInputMethod(comment_et);
					comment_et.setText("");
				} else {
					Toast.makeText(Video_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	private class SendLikeSet_Task extends AsyncTask<Object, Void, Map> {

		private String type;
		
		@Override
		protected Map doInBackground(Object... params) {
			type = (String) params[3];
			Map data = null;
			try {
				data = application.listSet(params[0], params[1], params[2], params[3]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		} 
		
		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					if(type.equals(M_Constant.LIKE))
						list_num = list_num + 1;
					else
						list_num = list_num - 1;
						
						collect_tv.setText(list_num + "");
				}
			}
		}
	}

	@Override
	public void notifyReply(String reply_id, String reply_nickname,
			String reply_content) {
		// TODO Auto-generated method stub
		reply_to_uid = reply_id;
		comment_et.requestFocus();
		InputMethodManager imm = (InputMethodManager) comment_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
		comment_et.setText("// @" + reply_nickname + ": " + reply_content);
	}

	@Override
	public void deleteReply(String reply_id) {
		// TODO Auto-generated method stub
		
	}
}
