package com.moinapp.wuliao.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
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
import com.moinapp.wuliao.model.GameDetail_Model;
import com.moinapp.wuliao.model.Recommend_Content_Model;
import com.moinapp.wuliao.model.Reply_Content_Model;
import com.moinapp.wuliao.model.Reply_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.MyListView;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Game_Activity extends Base_FragmentActivity implements InotifyReply, IdeleteReply {
	private LinearLayout l1, l2;
	private int current_item = 1;
	private ImageView tab_cursor;
	private TextView detail_tv, comment_tv;
	private TranslateAnimation translateAnimation;
	private AnimationSet animationSet;

	private ListView listview;
	private Comment_Adapter commentAdapter;

	private LinearLayout viewPagerContainer;
	private ViewPager viewPager;
	private MyPagerAdapter pagerAdapter;
	private ArrayList<String> pics;
	// 装点点的数组
	private ImageView[] dots;
	private int dot_num = 0;
	private int currentIndex;
		
	private MyPopWindow popupWindow;
	private M_Application application;
	private ImageLoader imageLoader;
	private Signin_User_Model login_model;
	private String uid;

	private ImageView bannar_iv;
	private TextView name_tv, category_tv, time_tv, share_tv, collect_tv, describe_tv;
	private EditText comment_et;
	private ImageView comment_avatar, load_more;
	private CheckBox game_collect_cb;
	private boolean collect_initial = false;
	
	private ImageView ad_iv1, ad_iv2;
	private TextView ad_tv1, ad_tv2;
	private Recommend_Content_Model ad_data1, ad_data2;

	private String about_type, about_id;
	private ArrayList<Reply_Content_Model> list;

	private final String pagesize = "10";
	private int page = 1;
//	private HorizontalListView ad_listview;
//	private Ad_View_Adapter ad_adapter;
//	private ArrayList<Recommend_Content_Model> ad_list;
//	private Face_Star_Adapter star_adapter;
//	private ListViewLinearLayoutHorizontal star_listview;
	private int list_num;
	private String title, pic, desr_content, share_url;
	
	private Boolean is_Reply = false;
	private String reply_to_uid;

	Handler shareHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:	// 分享成功
				likeSet(uid, about_type, about_id, M_Constant.SHARE);
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_layout);

		initData();
		initView();
		initPopuptWindow();
		initDots();
		
		new Game_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true);
		getReply(page, false);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = this.getIntent().getExtras();
		about_type = bundle.getString("about_type");
		about_id = bundle.getString("about_id");

		list = new ArrayList<Reply_Content_Model>();
		pics = new ArrayList<String>();

		login_model = application.getLogin_model();
		uid = application.getUid();

//		ad_list = new ArrayList<Recommend_Content_Model>();
//		ad_adapter = new Ad_View_Adapter(this, ad_list);
	}

	public void initView() {
		game_collect_cb = (CheckBox)findViewById(R.id.game_collect_cb);
		if ("0".equals(uid) || uid == null || "".equals(uid)) {
			game_collect_cb.setChecked(false);
			game_collect_cb.setClickable(false);
		}
		
		game_collect_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
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
		load_more = (ImageView)findViewById(R.id.load_more);
		commentAdapter = new Comment_Adapter(this, list, this, this);
//		star_listview = (ListViewLinearLayoutHorizontal) findViewById(R.id.star_listview);
		l1 = (LinearLayout) findViewById(R.id.game_detail_ll);
		l2 = (LinearLayout) findViewById(R.id.game_comment_ll);
		detail_tv = (TextView) findViewById(R.id.game_detail);
		comment_tv = (TextView) findViewById(R.id.game_comment);
		tab_cursor = (ImageView) findViewById(R.id.game_tab_cursor);
		listview = (MyListView) findViewById(R.id.game_lv);
		listview.setAdapter(commentAdapter);

		viewPagerContainer = (LinearLayout) findViewById(R.id.pager_layout);
		viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
		viewPager.setOffscreenPageLimit(3);
		pagerAdapter = new MyPagerAdapter(pics, this);
		viewPager.setAdapter(pagerAdapter);

		viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.margin_bigger));
		viewPager.setOnPageChangeListener(new ViewPagerChangeListener());

		viewPagerContainer.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return viewPager.dispatchTouchEvent(event);
			}
		});

		bannar_iv = (ImageView) findViewById(R.id.game_banner);
		name_tv = (TextView) findViewById(R.id.game_name);
		category_tv = (TextView) findViewById(R.id.game_category);
		time_tv = (TextView) findViewById(R.id.game_update);
		share_tv = (TextView) findViewById(R.id.game_share);
		collect_tv = (TextView) findViewById(R.id.game_collect);
		describe_tv = (TextView) findViewById(R.id.game_describe);

		comment_et = (EditText) findViewById(R.id.game_comment_et);
		comment_avatar = (ImageView) findViewById(R.id.game_comment_avatar);
		if (login_model != null) {
			comment_et.setHint(login_model.getNickname() + "说：");
			imageLoader.displayImage(login_model.getAvatar(), comment_avatar, BitmapUtil.getImageLoaderOption());
		}

//		ad_listview = (HorizontalListView) findViewById(R.id.ad_listview);
//		ad_listview.setAdapter(ad_adapter);
		
//		ad_listview.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Recommend_Content_Model data = ad_list.get(position);
//				Bundle bundle = new Bundle();
//				bundle.putString("about_id", data.getAbout_id());
//				bundle.putString("about_type", data.getAbout_type());
//				if (data.getAbout_type().equals(M_Constant.FACE))
//					AppTools.toIntent(Game_Activity.this, bundle, Expression_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.NEWS))
//					AppTools.toIntent(Game_Activity.this, bundle, News_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.GAME))
//					AppTools.toIntent(Game_Activity.this, bundle, Game_Activity.class);
//				else if (data.getAbout_type().equals(M_Constant.VIDEO))
//					AppTools.toIntent(Game_Activity.this, bundle, Video_Activity.class);
//			}
//		});
		
		ad_iv1 = (ImageView) findViewById(R.id.ad1_iv);
		ad_tv1 = (TextView) findViewById(R.id.ad1_title);
		ad_iv2 = (ImageView) findViewById(R.id.ad2_iv);
		ad_tv2 = (TextView) findViewById(R.id.ad2_title);
	}

	private void initDots() {
		LinearLayout ll = (LinearLayout) findViewById(R.id.indicator);
		dots = new ImageView[dot_num];
		for (int i = 0; i < dot_num; i++) {
			ImageView dot_iv = new ImageView(this);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 10, 0);
			dot_iv.setLayoutParams(lp);
			dots[i] = dot_iv;
			if (i == 0) {
				dots[i].setBackgroundResource(R.drawable.dot_white);
			} else {
				dots[i].setBackgroundResource(R.drawable.dot_gray);
			}
			ll.addView(dot_iv);
		}
		currentIndex = 0;
	}

	public void onClick(View v) {
		animationSet = new AnimationSet(true);
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			AppTools.showShare(this, false, null, title, desr_content + share_url, pic, share_url, shareHandler);
			break;
		case R.id.game_detail:
			if (current_item != 1) {
				load_more.setVisibility(View.GONE);
				l1.setVisibility(View.VISIBLE);
				l2.setVisibility(View.GONE);
				listview.setVisibility(View.GONE);
				detail_tv.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				comment_tv.setTextColor(getResources().getColor(R.color.black));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
						0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 1;
			}
			break;
		case R.id.game_comment:
			if (current_item != 2) {
				load_more.setVisibility(View.VISIBLE);
				l2.setVisibility(View.VISIBLE);
				l1.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				comment_tv.setTextColor(getResources().getColor(R.color.head_layout_bg_color));
				detail_tv.setTextColor(getResources().getColor(R.color.black));
				translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
						0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
				translateAnimation.setDuration(200);
				animationSet.addAnimation(translateAnimation);
				animationSet.setFillAfter(true);
				tab_cursor.startAnimation(animationSet);
				current_item = 2;
			}
			break;
		case R.id.comment_send:
			String content = comment_et.getText().toString();
			if (!"".equals(content) && content != null && uid != null && !"".equals(uid)) {
				if (is_Reply) {
					new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, reply_to_uid);
				} else {
					new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, "");
				}
			} else {
				Toast.makeText(this, R.string.not_login_comment, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.load_more:
			page++;
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
	
	private void likeSet(String uid, String about_type, String about_id, String set) {
		new SendLikeSet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, set);
	}

	public void initPopuptWindow() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.share_layout, null);
		popupWindow = new MyPopWindow(Game_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView cancel = (TextView) popupWindow_view.findViewById(R.id.pop_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	class MyPagerAdapter extends PagerAdapter {
		private ArrayList<String> images;
		private LayoutInflater inflater;

		public MyPagerAdapter(ArrayList<String> images, Context context) {
			this.images = images;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// View imageLayout = inflater.inflate(R.layout.game_viewpager_item,
			// container, false);
			// ImageView imageView = (ImageView)
			// imageLayout.findViewById(R.id.item_image);
			// imageLoader.displayImage(images.get(position), imageView,
			// BitmapUtil.getImageLoaderOption());
			// ((ViewPager) container).addView(imageLayout);
			// imageLoader.displayImage(images.get(position), imageView,
			// BitmapUtil.getImageLoaderOption());
			// return imageLayout;

			ImageView imageView = new ImageView(Game_Activity.this);
			imageView.setScaleType(ScaleType.FIT_XY);
			
			imageLoader.displayImage(images.get(position), imageView, BitmapUtil.getImageLoaderOption());
			((ViewPager) container).addView(imageView, position);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

	public class ViewPagerChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int postion) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (viewPagerContainer != null) {
				viewPagerContainer.invalidate();
			}
		}

		@Override
		public void onPageSelected(int position) {
			setCurDot(position);
		}
	}

	private void setCurDot(int position) {
		if (position < 0 || position > dot_num-1 || currentIndex == position) {
			return;
		}
		dots[position].setBackgroundResource(R.drawable.dot_white);
		dots[currentIndex].setBackgroundResource(R.drawable.dot_gray);
		currentIndex = position;
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
					} else {
						Toast.makeText(Game_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
					}
				} else {
					list.clear();
					list.addAll(result.getList());
					commentAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private class Game_Task extends AsyncTask<Object, Void, GameDetail_Model> {

		private boolean isRefresh;

		@Override
		protected GameDetail_Model doInBackground(Object... params) {
			GameDetail_Model data = null;
			isRefresh = (Boolean) params[0];
			try {
				data = application.getGameData(isRefresh, about_type, about_id);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(GameDetail_Model result) {
			if (result != null) {
				imageLoader.displayImage(result.getDetail().getTop_pic(), bannar_iv, BitmapUtil.getImageLoaderOption());
				list_num = Integer.parseInt(result.getDetail().getLike_num());
				
				if(M_Constant.LIKE.equals(result.getDetail().getLikeSet()))
					game_collect_cb.setChecked(true);
				else
					game_collect_cb.setChecked(false);
				collect_initial = true;
				
				name_tv.setText(result.getDetail().getTitle());
				category_tv.setText(result.getDetail().getClassname());
				try {
					time_tv.setText(M_Constant.sdf_custom_face.format(M_Constant.sdf_standard.parse(result.getDetail().getUpdate_time())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				collect_tv.setText(result.getDetail().getLike_num());
				describe_tv.setText(result.getDetail().getDescr());

				// 星级
//				star_adapter = new Face_Star_Adapter(Game_Activity.this, Integer.parseInt(result.getDetail().getStar_num()));
//				star_listview.setAdapter(star_adapter);
				
				title = result.getDetail().getTitle();
				pic = result.getDetail().getPic();
				desr_content = result.getDetail().getDescr();
				share_url = result.getDetail().getShare_url();
				
				pics.clear();
				if (result.getDetail().getPics() != null)
					pics.addAll(result.getDetail().getPics());
				dot_num = pics.size();
				initDots();
				pagerAdapter.notifyDataSetChanged();

				if (result.getRecommend() != null && result.getRecommend().getList() != null) {
				
					ad_data1 = result.getRecommend().getList().get(1);
					ad_data2 = result.getRecommend().getList().get(2);
					imageLoader.displayImage(ad_data1.getPic(), ad_iv1, BitmapUtil.getImageLoaderOption());
					ad_tv1.setText(ad_data1.getTitle());
					imageLoader.displayImage(ad_data2.getPic(), ad_iv2, BitmapUtil.getImageLoaderOption());
					ad_tv2.setText(ad_data2.getTitle());
				
//					ad_list.addAll(result.getRecommend().getList());
//					ad_adapter.notifyDataSetChanged();
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
					getReply(1, false);
					comment_et.setText("");
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