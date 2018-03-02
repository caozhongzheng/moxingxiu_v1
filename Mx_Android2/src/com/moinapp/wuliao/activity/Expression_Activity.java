package com.moinapp.wuliao.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.Comment_Adapter;
import com.moinapp.wuliao.adapter.Comment_Adapter.IdeleteReply;
import com.moinapp.wuliao.adapter.Comment_Adapter.InotifyReply;
import com.moinapp.wuliao.adapter.Expression_GridView_Adapter;
import com.moinapp.wuliao.adapter.Expression_ViewPager_Adapter;
import com.moinapp.wuliao.model.FaceDetail_Content_Faces_List;
import com.moinapp.wuliao.model.FaceDetail_Model;
import com.moinapp.wuliao.model.Recommend_Content_Model;
import com.moinapp.wuliao.model.Reply_Content_Model;
import com.moinapp.wuliao.model.Reply_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.InsideViewPager;
import com.moinapp.wuliao.ui.view.MyListView;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zyh.util.MD5EncodeUtil;
import com.zyh.volleybox.FileUtil;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Expression_Activity extends Base_FragmentActivity implements InotifyReply, IdeleteReply {
	private LinearLayout l1, l2;
	private int current_item = 1;
	private ImageView tab_cursor;
	private TextView detail_tv, comment_tv;
	private TranslateAnimation translateAnimation;
	private AnimationSet animationSet;

	private LinearLayout comment_list;
	private ListView listview;
	private Comment_Adapter commentAdapter;
	private List<View> lists;
	// 装点点的数组
	private ImageView[] dots;
	private int dot_num = 0;
	private int currentIndex;
	
	private ImageView ad_iv1, ad_iv2;
	private TextView ad_tv1, ad_tv2;
	private Recommend_Content_Model ad_data1, ad_data2;

	private MyPopWindow popupWindow;
	private M_Application application;
	private ImageLoader imageLoader;
	private Signin_User_Model login_model;
	private String uid;

	private View share_view;
	private Dialog dialog;

	private TextView head_title;
	private ImageView bannar_iv;
	private TextView name_tv, category_tv, time_tv, share_tv, collect_tv;
	private EditText comment_et;
	private TextView comment_send;
	private ImageView comment_avatar, load_more;
	private CheckBox expression_collect_cb;
	private boolean collect_initial = false;

	private String about_type, about_id;
	private InsideViewPager expression_pager;
	private Expression_ViewPager_Adapter pagerAdapter;
	private ArrayList<Reply_Content_Model> list;
	private ArrayList<FaceDetail_Content_Faces_List> gridview_datas;

	private GifImageView dialog_imageview;
	private final String pagesize = "10";
	private int page = 1;
	private int dl_num, list_num;
	private Dialog download_dialog;
	private String face_download_url, id;
	private boolean isDownLoading;
	private String title, pic, gif_cache_url, image_path, gif_thumb, share_url;
	private Boolean is_Reply = false;
	private String reply_to_uid;
	
	private TextView deleteReply_sure, deleteReply_cancel;
	private Dialog deleteReply_dialog;
	private String reply_id = "";
	
	private LinearLayout temp_ll;

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
		setContentView(R.layout.expression_layout);

		initData();
		initView();
		initDeleteDialog();
		initDialog();
		new Expression_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true);

		getReply(page, false);
	}

	private void initDialog() {
		View version_view = LayoutInflater.from(this).inflate(R.layout.face_download_dialog, null);
		RadioGroup face_download_dialog_rgp = (RadioGroup) version_view.findViewById(R.id.face_download_dialog_rgp);
		final ScrollView face_download_dialog_scrollview1 = (ScrollView) version_view.findViewById(R.id.face_download_dialog_scrollview1);
		final ScrollView face_download_dialog_scrollview2 = (ScrollView) version_view.findViewById(R.id.face_download_dialog_scrollview2);
		face_download_dialog_rgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.face_download_dialog_weixin:
					face_download_dialog_scrollview1.setVisibility(View.VISIBLE);
					face_download_dialog_scrollview2.setVisibility(View.GONE);
					break;
				case R.id.face_download_dialog_qq:
					face_download_dialog_scrollview1.setVisibility(View.GONE);
					face_download_dialog_scrollview2.setVisibility(View.VISIBLE);
					break;
				}
			}
		});
		download_dialog = new Dialog(this, R.style.retrieve_dialog);
		download_dialog.setContentView(version_view);
		download_dialog.setCancelable(true);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = this.getIntent().getExtras();
		about_type = bundle.getString("about_type");
		about_id = bundle.getString("about_id");

		list = new ArrayList<Reply_Content_Model>();
		lists = new ArrayList<View>();
		gridview_datas = new ArrayList<FaceDetail_Content_Faces_List>();
//		ad_list = new ArrayList<Recommend_Content_Model>();
//		ad_adapter = new Ad_View_Adapter(this, ad_list);
		ad_data1 = new Recommend_Content_Model();
		ad_data2 = new Recommend_Content_Model();

		login_model = application.getLogin_model();
		uid = application.getUid();

	}

	public void initView() {
		head_title = (TextView) findViewById(R.id.title_center);
		head_title.setText(getResources().getString(R.string.expression_title));
		
		temp_ll = (LinearLayout) findViewById(R.id.expression_ll);
		initPopuptWindow();
		expression_collect_cb = (CheckBox) findViewById(R.id.expression_collect_cb);
		if("0".equals(uid) || uid == null || "".equals(uid)) {
			expression_collect_cb.setChecked(false);
			expression_collect_cb.setClickable(false);
		}
			
		expression_collect_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (collect_initial) {
					if (isChecked)
						likeSet(uid, about_type, about_id, M_Constant.LIKE);
					else
						likeSet(uid, about_type, about_id, M_Constant.HATE);
				}
			}
		});
		comment_list = (LinearLayout) findViewById(R.id.comment_list);
		load_more = (ImageView) findViewById(R.id.load_more);
		commentAdapter = new Comment_Adapter(this, list, this, this);
		l1 = (LinearLayout) findViewById(R.id.expression_detail_ll);
		l2 = (LinearLayout) findViewById(R.id.expression_comment_ll);
		detail_tv = (TextView) findViewById(R.id.expression_detail);
		comment_tv = (TextView) findViewById(R.id.expression_comment);
		tab_cursor = (ImageView) findViewById(R.id.expression_tab_cursor);
		listview = (MyListView) findViewById(R.id.expression_lv);
		listview.setAdapter(commentAdapter);

		share_view = View.inflate(this, R.layout.gif_play_dialog, null); // 填充一个布局文件
		dialog_imageview = (GifImageView) share_view.findViewById(R.id.dialog_imageview);

		bannar_iv = (ImageView) findViewById(R.id.expression_banner);
		name_tv = (TextView) findViewById(R.id.expression_name);
		category_tv = (TextView) findViewById(R.id.expression_category);
		time_tv = (TextView) findViewById(R.id.expression_update);
		share_tv = (TextView) findViewById(R.id.expression_share);
		collect_tv = (TextView) findViewById(R.id.expression_collect);
		
		comment_et = (EditText) findViewById(R.id.expression_comment_et);
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

		comment_avatar = (ImageView) findViewById(R.id.expression_comment_avatar);
		if (login_model != null) {
			comment_et.setHint(login_model.getNickname() + "说：");
			imageLoader.displayImage(login_model.getAvatar(), comment_avatar, BitmapUtil.getImageLoaderOption());
		}
		
		expression_pager = (InsideViewPager) l1.findViewById(R.id.detail_viewpager);
		pagerAdapter = new Expression_ViewPager_Adapter(lists);
		expression_pager.setAdapter(pagerAdapter);
		expression_pager.setOnPageChangeListener(new ViewPagerChangeListener());
		
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

	private void initPager(int page) {
		for (int i = 0; i < page; i++) {
			final int cur_page = i; 
			LinearLayout linear = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.expression_pager_item, null);
			GridView expression_gv = (GridView) linear.findViewById(R.id.gridview);
			Expression_GridView_Adapter gridviewAdapter = new Expression_GridView_Adapter(this, gridview_datas, i);
			expression_gv.setAdapter(gridviewAdapter);
			expression_gv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					FaceDetail_Content_Faces_List data = gridview_datas.get(arg2 + cur_page * 9);
					loadDialogMap(data.getFace(), data.getThumb());
				}
			});
			lists.add(linear);
		}
	}
	
	private void measureViewpager(int number) {
		LayoutParams linearParams = (LinearLayout.LayoutParams) expression_pager.getLayoutParams(); 
		if (number > 0 && number <= 3) {
			linearParams.height = (int) getResources().getDimension(R.dimen.expression_viewpager_height1);
		} else if (number > 3 && number <= 6) {
			linearParams.height = (int) getResources().getDimension(R.dimen.expression_viewpager_height2);
		} else {
			linearParams.height = (int) getResources().getDimension(R.dimen.expression_viewpager_height3);
		}
		expression_pager.setLayoutParams(linearParams);
	}
	
	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
			case 0:
				Toast.makeText(Expression_Activity.this, R.string.face_unzip_fail, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(Expression_Activity.this, R.string.face_unzip_success, Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
	};

	public void onClick(View v) {
		animationSet = new AnimationSet(true);
		switch (v.getId()) {
		case R.id.expression_download:
			if (AppTools.existsSDCARD()) {
				FileUtil.createFolder(M_Constant.CACHE_ZIP);
			}
			
			if(isDownLoading) {
				Toast.makeText(this, R.string.face_zip_downloading, Toast.LENGTH_SHORT).show();
				return;
			}

			final File file = new File(M_Constant.CACHE_ZIP + MD5EncodeUtil.MD5Encode(face_download_url.getBytes()));
			if (file.exists()) {
				download_dialog.show();
			} else {
				HttpUtils httpUtils = new HttpUtils();
				httpUtils.download(face_download_url, M_Constant.CACHE_ZIP + MD5EncodeUtil.MD5Encode(face_download_url.getBytes()),
						new RequestCallBack<File>() {
					
							@Override
							public void onStart() {
								super.onStart();
								isDownLoading = true;
							}

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								isDownLoading = false;
								try {
									download_dialog.show(); 
								} catch(Exception e) {
									
								}
								final String zipFilePath = arg0.result.getPath();
								new Thread() {

									@Override
									public void run() {
										AppTools.unZipFile(zipFilePath, M_Constant.CACHE_ZIP + id +"/", mhandler);
									}
									
								}.start();
							}

							@Override
							public void onFailure(HttpException arg0, String arg1) {
								isDownLoading = false;
								onFailureDeleteFile(file.getPath());
							}
						});
			}
			break;
		case R.id.face_download_dialog_cancel:
			download_dialog.dismiss();
			break;
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			AppTools.showShare(this, false, null, title, title + share_url, pic, share_url ,shareHandler);
			break;
		case R.id.expression_detail:
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
		case R.id.expression_comment:
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
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				if (!"".equals(content) && content != null) {
					if (is_Reply) {
						new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, reply_to_uid);
					} else {
						new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, "");
					}
				} else {
					Toast.makeText(this, R.string.not_content_comment, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, R.string.not_login_comment, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.share_dialog_cancel:
			dialog.dismiss();
			break;
		case R.id.load_more:
			page++;
			getReply(page, true);
			break;
		case R.id.gif_weixin:
			shareData(Wechat.NAME);
			break;
		case R.id.gif_friend:
			shareData(WechatMoments.NAME);
			break;
		case R.id.ad1_iv:
			Bundle bundle1 = new Bundle();
			bundle1.putString("about_id", ad_data1.getAbout_id());
			bundle1.putString("about_type", ad_data1.getAbout_type());
			finish();
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
			finish();
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
		case R.id.deleteReply_sure:
			new DeleteReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, reply_id);
			deleteReply_dialog.dismiss();
			break;
		case R.id.deleteReply_cancel:
			deleteReply_dialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	private void shareData(String type) {
		ShareParams sp = new ShareParams();
		sp.setTitle(title);
		sp.setText(title);
		sp.setTitleUrl(share_url);
		
		if(type.equals(Wechat.NAME)) {
			sp.setShareType(Platform.SHARE_EMOJI);
			sp.setImageUrl(gif_cache_url);
		} else {
			sp.setShareType(Platform.SHARE_IMAGE);
			sp.setImageUrl(gif_thumb);
			sp.setUrl(share_url);
		}
		
		Platform weibo = ShareSDK.getPlatform(type);
		weibo.setPlatformActionListener(pal); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);
	}
	
	Handler handler  = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			ToastUtil.makeText(Expression_Activity.this, R.string.xinxi_no, Toast.LENGTH_SHORT);
		}
		
	};
	
	private PlatformActionListener pal = new PlatformActionListener() {
		
		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			if(arg1 == 9)
				handler.sendEmptyMessage(0);
		}
		
		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			ToastUtil.makeText(Expression_Activity.this, R.string.share_success, Toast.LENGTH_SHORT);
		}
		
		@Override
		public void onCancel(Platform arg0, int arg1) {
			
		}
	};
	
	private void likeSet(String uid, String about_type, String about_id, String set) {
		new SendLikeSet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, set);
	}

	public void initPopuptWindow() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.share_layout, null);
		popupWindow = new MyPopWindow(Expression_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView cancel = (TextView) popupWindow_view.findViewById(R.id.pop_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	public class ViewPagerChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int postion) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			setCurDot(position);
		}
	}

	private void setCurDot(int positon) {
		if (positon < 0 || positon > dot_num-1 || currentIndex == positon) {
			return;
		}
		dots[positon].setBackgroundResource(R.drawable.dot_white);
		dots[currentIndex].setBackgroundResource(R.drawable.dot_gray);
		currentIndex = positon;
	}

	private void loadDialogMap(String face_url, String thumb_url) {
		if (dialog == null) {
			dialog = new Dialog(this, R.style.MyDialog);
			// 设置它的ContentView
			dialog.setContentView(share_view);
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface arg0) {
					gif_cache_url = "";
				}
			});

			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			WindowManager m = getWindowManager();
			DisplayMetrics dm = new DisplayMetrics();
			m.getDefaultDisplay().getMetrics(dm);
			
			
			//lp.alpha = 0.3f;
			// dialog.getWindow().setAttributes(lp);

			// alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明

			// 2.设置黑暗度(Dialog自身的黑暗度)
			//
			// dialog.setContentView(R.layout.dialog);
			// WindowManager.LayoutParams lp =
			// dialog.getWindow().getAttributes();
			// lp.dimAmount= 0.3f;
			// dialog.getWindow().setAttributes(lp);
			// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
			// WindowManager.LayoutParams.FLAG_BLUR_BEHIND（设置模糊）
		}
		dialog.show();
		dialog.setCanceledOnTouchOutside(true);
		
		gif_cache_url = face_url;
		gif_thumb = thumb_url;

		if (AppTools.existsSDCARD()) {
			FileUtil.createFolder(M_Constant.CACHE_GIF);
		}

		final File file = new File(M_Constant.CACHE_GIF + MD5EncodeUtil.MD5Encode(face_url.getBytes()));
		if (file.exists()) {
			GifDrawable gifFromPath;
			try {
				image_path = file.getPath();
				gifFromPath = new GifDrawable(file.getPath());
				dialog_imageview.setImageDrawable(gifFromPath);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(face_url, M_Constant.CACHE_GIF + MD5EncodeUtil.MD5Encode(face_url.getBytes()), new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					try {
						image_path = arg0.result.getPath();
						GifDrawable gifFromPath = new GifDrawable(arg0.result.getPath());
						dialog_imageview.setImageDrawable(gifFromPath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					onFailureDeleteFile(file.getPath());
				}
			});
		}

		// imageLoader.displayImage(face_url, dialog_imageview,
		// BitmapUtil.getImageLoaderOption());
	}

	private void onFailureDeleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
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
			if (result != null && result.getList().size() != 0) {
				comment_list.setVisibility(View.VISIBLE);
				if (isMore) {
					list.addAll(result.getList());
				} else {
					list.clear();
					list.addAll(result.getList());
				}
				commentAdapter.notifyDataSetChanged();
			} else {  // 没有数据了
				if (!isMore) {
					list.clear();
					comment_list.setVisibility(View.GONE);
				} else {
					Toast.makeText(Expression_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
				}
			} 
		}
	}

	private class Expression_Task extends AsyncTask<Object, Void, FaceDetail_Model> {

		private boolean isRefresh;

		@Override
		protected FaceDetail_Model doInBackground(Object... params) {
			FaceDetail_Model data = null;
			isRefresh = (Boolean) params[0];
			try {
				data = application.getExpressionData(isRefresh, about_type, about_id);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(FaceDetail_Model result) {
			if (result != null) {
				id = result.getDetail().getId();
				face_download_url = result.getDetail().getFilepath() + "/" + result.getDetail().getFilename();
				imageLoader.displayImage(result.getDetail().getBig_pic(), bannar_iv, BitmapUtil.getImageLoaderOption());
				
				name_tv.setText(result.getDetail().getTitle());
				share_tv.setText(result.getDetail().getShare_num());
				category_tv.setText(result.getDetail().getClassname());
				try {
					time_tv.setText(M_Constant.sdf_custom_face.format(M_Constant.sdf_standard.parse(result.getDetail().getUpdate_time())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				collect_tv.setText(result.getDetail().getLike_num());
				
				title = result.getDetail().getTitle();
				pic = result.getDetail().getPic();

				dl_num = Integer.parseInt(result.getDetail().getDownload_num());
				list_num = Integer.parseInt(result.getDetail().getLike_num());
				share_url = result.getDetail().getShare_url();
				
				if (M_Constant.LIKE.equals(result.getDetail().getLikeSet()))
					expression_collect_cb.setChecked(true);
				else
					expression_collect_cb.setChecked(false);
				collect_initial = true;

				// 星级
//				star_adapter = new Face_Star_Adapter(Expression_Activity.this, Integer.parseInt(result.getDetail().getStar_num()));
//				star_listview.setAdapter(star_adapter);

				if (result.getRecommend() != null && result.getRecommend().getList() != null) {
//					ad_list.addAll(result.getRecommend().getList());
					
					ad_data1 = result.getRecommend().getList().get(1);
					ad_data2 = result.getRecommend().getList().get(2);
					imageLoader.displayImage(ad_data1.getPic(), ad_iv1, BitmapUtil.getImageLoaderOption());
					ad_tv1.setText(ad_data1.getTitle());
					imageLoader.displayImage(ad_data2.getPic(), ad_iv2, BitmapUtil.getImageLoaderOption());
					ad_tv2.setText(ad_data2.getTitle());
				}
//				ad_adapter.notifyDataSetChanged();
				
				// 表情预览图
				gridview_datas.clear();
				if (result.getDetail().getFaces() != null && result.getDetail().getFaces().getList() != null) {
					gridview_datas.addAll(result.getDetail().getFaces().getList());
				}
				// 代码设置viewpager高度
				measureViewpager(gridview_datas.size());
				if (gridview_datas.size() % 9 == 0) {
					dot_num = gridview_datas.size() / 9;
				} else {
					dot_num = gridview_datas.size() / 9 + 1;
				}
				initPager(dot_num);
				initDots();
				pagerAdapter.notifyDataSetChanged();
			}
		}
	}

	private class SendReply_Task extends AsyncTask<Object, Void, Map> {

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
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
					collapseSoftInputMethod(comment_et);
					comment_et.setText("");
				}
			} else {
				Toast.makeText(Expression_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	public void collapseSoftInputMethod(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		editText.clearFocus();
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
					if (type.equals(M_Constant.LIKE))
						list_num = list_num + 1;
					else
						list_num = list_num - 1;

					collect_tv.setText(list_num + "");
				}
			}
		}
	}
	
	
	/**
	 *	删除评论
	 */
	public class DeleteReply_Task extends AsyncTask<Object, Void, Map> {

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
				data = application.deleteReply(params[0], params[1]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					ToastUtil.makeText(Expression_Activity.this, "撤销成功", Toast.LENGTH_SHORT);
					page = 1;
					getReply(page, false);
				} else {
					ToastUtil.makeText(Expression_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT);
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
		this.reply_id = reply_id;
		deleteReply_dialog.show();
	}
	
	private void initDeleteDialog() {
		View loginOut_view = LayoutInflater.from(this).inflate(R.layout.delete_reply_layout, null);
		deleteReply_sure = (TextView) loginOut_view.findViewById(R.id.deleteReply_sure);
		deleteReply_cancel = (TextView) loginOut_view.findViewById(R.id.deleteReply_cancel);
		deleteReply_dialog = new Dialog(this, R.style.retrieve_dialog);
		deleteReply_dialog.setContentView(loginOut_view);
		deleteReply_dialog.setCancelable(true);
	}
}