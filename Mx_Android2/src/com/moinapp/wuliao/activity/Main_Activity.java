package com.moinapp.wuliao.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.adapter.BannerViewPagerAdapter;
import com.moinapp.wuliao.common.UpdateManager;
import com.moinapp.wuliao.model.Ad_Content_Model;
import com.moinapp.wuliao.model.Heartbeat_Model;
import com.moinapp.wuliao.model.Login_Model;
import com.moinapp.wuliao.model.RememberUser;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.model.Version_Model;
import com.moinapp.wuliao.slidingmenu.SlidingFragmentActivity;
import com.moinapp.wuliao.slidingmenu.base.SlidingMenu;
import com.moinapp.wuliao.slidingmenu.base.SlidingMenu.OnCloseListener;
import com.moinapp.wuliao.slidingmenu.base.SlidingMenu.OnOpenListener;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.fragment.MainList_Fragment;
import com.moinapp.wuliao.ui.fragment.MainList_Fragment.RecommendDataListener;
import com.moinapp.wuliao.ui.fragment.MainList_Fragment.ScrollToStarEndListener;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.FileUtil;
import com.moinapp.wuliao.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

<<<<<<< .mine
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Main_Activity extends SlidingFragmentActivity implements RecommendDataListener ,ScrollToStarEndListener{
=======
public class Main_Activity extends SlidingFragmentActivity implements RecommendDataListener, ScrollToStarEndListener {
>>>>>>> .r195

	private M_Application application;
	private SlidingMenu mSlidingMenu;

	private static final String fingerprint_key = "signin_date_";
	private static final SimpleDateFormat fingerprint_sdf = new SimpleDateFormat("yyyy-MM-dd");;
	private CheckBox leftmenu_fingerprint_cbx;
	private TextView leftmenu_mb;
	private LinearLayout nologin_layout, login_success_layout;
	private ImageView leftmenu_icon, title_left;
	private TextView leftmenu_name, leftmenu_comments_num, leftmenu_favorite_num, fingerprint_tv, fingerprint_success;
	private ImageView update_prompt;

	private Animation animation5, animation7, animation_7, leftmenu_mb_animation, leftmenu_mb_animation2;
	private ArrayList<Fragment> fragment_list;
	private ViewPager mainlist_viewpager;
	private ImageLoader imageLoader;
	private Signin_User_Model login_data = null;
	private String uid, username;
	private ArrayList<Ad_Content_Model> datalist;
	private BannerViewPagerAdapter banner_adaper;
	private AppUpdate_Task appUpdateTask;

	private Dialog update_dialog;
	private String versionName;
	private Version_Model appinfo;
	private MainList_Fragment main_fragment;
	private RadioGroup main_rg;

	private GifImageView loading_imageview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_layout);

		initData();
		initSlidingMenu();
		initView();
		initAnimation();

		getUnOpenMessageNum();

		MobclickAgent.updateOnlineConfig(this);

		initUpdateDialog();
		versionName = AppTools.getVersionName(this);
		int currentversion = AppTools.getVersionCode(this);
		appUpdateTask = new AppUpdate_Task();
		appUpdateTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, currentversion);
	}

	private void getUnOpenMessageNum() {
		new UnOpenMessageNum_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "uid");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1: // 登录成功
			login_data = (Signin_User_Model) data.getSerializableExtra(M_Constant.UID);
			username = data.getStringExtra("username");
			handler.sendEmptyMessage(1);
			break;
		case 2: // 修改个人信息
			login_data = (Signin_User_Model) data.getSerializableExtra(M_Constant.UID);
			handler.sendEmptyMessage(1);
			break;
		case 3: // 修改个人头像
			login_data = (Signin_User_Model) data.getSerializableExtra(M_Constant.UID);
			String icon_path = data.getStringExtra("icon_path");
			Message msg = Message.obtain();
			msg.what = 3;
			msg.obj = icon_path;
			handler.sendMessage(msg);
			break;
		case 4:
			login_data = null;
			uid = application.getUid();
			username = "";
			handler.sendEmptyMessage(2);
			break;
		}
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (login_data != null) {
					uid = login_data.getId();
					leftmenu_name.setText(login_data.getNickname());
					leftmenu_mb.setText(login_data.getIntegral());
					imageLoader.displayImage(login_data.getAvatar(), leftmenu_icon, BitmapUtil.getImageLoaderOption());
					imageLoader.displayImage(login_data.getAvatar(), title_left, BitmapUtil.getImageLoaderOption());
					leftmenu_comments_num.setText(login_data.getReply_num());
					leftmenu_favorite_num.setText(login_data.getLike_num());

					String signin_date = "";
					if (FileUtil.readFileData(Main_Activity.this, fingerprint_key + username) != null) {
						signin_date = FileUtil.readFileData(Main_Activity.this, fingerprint_key + username);

						String current_date = fingerprint_sdf.format(new java.util.Date());
						if (current_date.equals(signin_date)) {
							leftmenu_fingerprint_cbx.setVisibility(View.INVISIBLE);
							leftmenu_fingerprint_cbx.setChecked(true);
							fingerprint_tv.setVisibility(View.INVISIBLE);
							fingerprint_success.setVisibility(View.VISIBLE);
						} else {
							leftmenu_fingerprint_cbx.setVisibility(View.VISIBLE);
							leftmenu_fingerprint_cbx.setChecked(false);
							fingerprint_tv.setVisibility(View.VISIBLE);
							fingerprint_success.setVisibility(View.INVISIBLE);
						}
					} else {
						leftmenu_fingerprint_cbx.setVisibility(View.VISIBLE);
					}

					nologin_layout.setVisibility(View.INVISIBLE);
					login_success_layout.setVisibility(View.VISIBLE);
					handler.post(heart);
					application.setUid(uid);
					application.setLogin_model(login_data);
				}
				break;
			case 2:
				nologin_layout.setVisibility(View.VISIBLE);
				login_success_layout.setVisibility(View.INVISIBLE);
				leftmenu_fingerprint_cbx.setChecked(false);
				leftmenu_fingerprint_cbx.setVisibility(View.VISIBLE);
				fingerprint_tv.setVisibility(View.VISIBLE);
				fingerprint_success.setVisibility(View.INVISIBLE);

				title_left.setImageResource(R.drawable.leftmenu_default_icon);

				handler.removeCallbacks(heart);
				AppTools.toIntent(Main_Activity.this, Login_Activity.class, 1);
				break;
			case 3:
				application.setLogin_model(login_data);
				Bitmap bitmap = imageLoader.loadImageSync((String) msg.obj);

				leftmenu_icon.setImageBitmap(bitmap);
				title_left.setImageBitmap(bitmap);
				break;
			}
		}

	};

	private Runnable heart = new Runnable() {
		@Override
		public void run() {
			heartHandler.sendEmptyMessage(0);
			handler.postDelayed(heart, 1000 * 8);
		}
	};

	private Handler bannerHandler = new Handler();

	private Runnable banner = new Runnable() {
		@Override
		public void run() {
			bannerHandler.postDelayed(banner, 5000);

			// if (banner_v.getCurrentItem() == banner_v.getChildCount() - 1)
			// banner_v.setCurrentItem(0, true);
			// else
			// banner_v.setCurrentItem(banner_v.getCurrentItem() + 1, true);
		}
	};

	Handler heartHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what) {
			case 0:
				toHeartBeat();
				break;
			case 1:
				Heartbeat_Model headbeat = (Heartbeat_Model) msg.obj;
				leftmenu_comments_num.setText(headbeat.getReply_num());
				leftmenu_favorite_num.setText(headbeat.getLike_num());
				break;
			}
		}
	};

	private void toHeartBeat() {
		uid = application.getUid();
		if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
			new Thread() {
				@Override
				public void run() {
					Heartbeat_Model headbeat = application.heartBeat(uid);
					Signin_User_Model cache_model = application.getLogin_model();
					try {
						cache_model.setOnline_time(headbeat.getOnline_time());
						application.setLogin_model(cache_model);

						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = headbeat;
						heartHandler.sendMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}.start();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(heart);
		bannerHandler.removeCallbacks(banner);
		application.clearCatchModel();
	}

	public void onClick(View v) {
		Bundle bundle;
		switch (v.getId()) {
		case R.id.title_left:
			mSlidingMenu.showMenu();
			break;
		case R.id.leftmenu_fingerprint:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				if (!leftmenu_fingerprint_cbx.isChecked()) {
					String Signin_date = fingerprint_sdf.format(new java.util.Date());
					// 保存签到时间
					FileUtil.writeFileData(Main_Activity.this, fingerprint_key + username, Signin_date);

					leftmenu_fingerprint_cbx.setChecked(true);
					leftmenu_fingerprint_cbx.setVisibility(View.INVISIBLE);
					fingerprint_tv.setVisibility(View.INVISIBLE);
					fingerprint_success.setVisibility(View.VISIBLE);
					signin(uid, username);
					leftmenu_mb.startAnimation(leftmenu_mb_animation);
				} else {
					ToastUtil.makeText(this, R.string.leftmenu_fingerprint_tip, Toast.LENGTH_SHORT);
				}
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.leftmenu_mystar:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				bundle = new Bundle();
				bundle.putInt("position", 2);
				bundle.putString("username", username);
				AppTools.toIntent(this, bundle, Function_Activity.class, 2);
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.leftmenu_wealthcenter:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				bundle = new Bundle();
				bundle.putInt("position", 0);
				bundle.putString("username", username);
				AppTools.toIntent(this, bundle, Function_Activity.class, 2);
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.leftmenu_setting:
			AppTools.toIntent(this, Setting_Activity.class, 0);
			break;
		case R.id.leftmenu_icon:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				bundle = new Bundle();
				bundle.putInt("position", 1);
				bundle.putString("username", username);
				AppTools.toIntent(this, bundle, Function_Activity.class, 2);
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.leftmenu_login:
			mSlidingMenu.toggle();
			AppTools.toIntent(this, Login_Activity.class, 1);
			break;
		case R.id.leftmenu_no_login:
			mSlidingMenu.toggle();
			AppTools.toIntent(this, Login_Activity.class, 1);
			break;
		case R.id.mycomments_layout:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("nickname", login_data.getNickname());
				AppTools.toIntent(this, bundle, MyComments_Activity.class);
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.mylikes_layout:
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("nickname", login_data.getNickname());
				AppTools.toIntent(this, bundle, MyLikes_Activity.class);
			} else {
				ToastUtil.makeText(this, R.string.leftmenu_no_login, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.title_right_search:
			AppTools.toIntent(this, Search_Activity.class);
			break;
		case R.id.title_right:
			AppTools.toIntent(this, Search_Activity.class);
			break;
		case R.id.alter_update_sure:
			UpdateManager.getUpdateManager().checkAppUpdate(Main_Activity.this, appinfo.getApkFileUrl(), appinfo.getVersionNameAndroid(), appinfo.getAppname());
			break;
		case R.id.alter_update_cancel:
			update_dialog.dismiss();
			break;
		}
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();

		datalist = new ArrayList<Ad_Content_Model>();
		banner_adaper = new BannerViewPagerAdapter(this, datalist);

		main_fragment = new MainList_Fragment(M_Constant.DEFAULT);
		fragment_list = new ArrayList<Fragment>();
		fragment_list.add(main_fragment);

		RememberUser rememberUser = AppTools.getRemeberUsernameAndPassword(this);
		username = rememberUser.getUsername();
		uid = rememberUser.getUid();
		if (!"".equals(username) && username != null && !"".equals(uid) && uid != null) {
			Login_Model login_model = application.readLoginUserInformation(rememberUser.getUsername());
			if (login_model != null) {
				login_data = login_model.getData();
				handler.sendEmptyMessage(1);

				application.setUid(uid);
				application.setLogin_model(login_data);
			} else {
				application.login_out();
			}
		}
	}

	private void initSlidingMenu() {
		setBehindContentView(R.layout.leftmenu_layout);
		mSlidingMenu = getSlidingMenu();
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		mSlidingMenu.setBackgroundResource(R.drawable.lf_bg);
		// 设置专场动画效果
		mSlidingMenu.setBehindCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, -canvas.getWidth() / 2, canvas.getHeight() / 2);
			}
		});

		mSlidingMenu.setAboveCanvasTransformer(new SlidingMenu.CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (1 - percentOpen * 0.25);
				canvas.scale(scale, scale, 0, canvas.getHeight() / 2);
			}
		});

		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				if (main_fragment != null)
					main_fragment.openBaner();
			}
		});

		mSlidingMenu.setOnCloseListener(new OnCloseListener() {
			@Override
			public void onClose() {
				if (main_fragment != null)
					main_fragment.closeBaner();
			}
		});
	}

	private void initAnimation() {
		// 菜单小按钮
		animation5 = AnimationUtils.loadAnimation(this, R.anim.rotate);
		animation7 = AnimationUtils.loadAnimation(this, R.anim.scale7);
		animation_7 = AnimationUtils.loadAnimation(this, R.anim.scale_7);
		leftmenu_mb_animation = AnimationUtils.loadAnimation(this, R.anim.leftmenu_mb_scale);
		leftmenu_mb_animation2 = AnimationUtils.loadAnimation(this, R.anim.leftmenu_mb_scale2);

		animation7.setAnimationListener(al);
		animation_7.setAnimationListener(al);
		animation5.setAnimationListener(al);
		leftmenu_mb_animation.setAnimationListener(al);
	}

	private AnimationListener al = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			if (animation == animation_7) {
			} else if (animation == animation7) {
			} else if (animation == animation5) {
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (animation == animation_7) {
			} else if (animation == animation7) {
			} else if (animation == animation5) {
			} else if (animation == leftmenu_mb_animation) {
				leftmenu_mb.startAnimation(leftmenu_mb_animation2);
			}

		}
	};

	private void initView() {
		loading_imageview = (GifImageView) findViewById(R.id.loading_imageview);
		try {
			GifDrawable gifFromPath = new GifDrawable(getAssets(), "loading.gif");
			loading_imageview.setImageDrawable(gifFromPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		main_rg = (RadioGroup) findViewById(R.id.main_rg);
		main_rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.order_page:
					loading_imageview.setVisibility(View.VISIBLE);
					main_fragment.loadDataForType(M_Constant.NEWS);
					break;
				case R.id.main_page:
					loading_imageview.setVisibility(View.VISIBLE);
					main_fragment.loadDataForType(M_Constant.DEFAULT);
					break;
				case R.id.face_page:
					loading_imageview.setVisibility(View.VISIBLE);
					main_fragment.loadDataForType(M_Constant.FACE);
					break;
				}
			}
		});

		title_left = (ImageView) findViewById(R.id.title_left);

		leftmenu_fingerprint_cbx = (CheckBox) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_fingerprint_cbx);
		fingerprint_tv = (TextView) mSlidingMenu.getMenu().findViewById(R.id.fingerprint_tv);
		fingerprint_success = (TextView) mSlidingMenu.getMenu().findViewById(R.id.fingerprint_success);

		leftmenu_mb = (TextView) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_mb);
		nologin_layout = (LinearLayout) mSlidingMenu.getMenu().findViewById(R.id.nologin_layout);
		login_success_layout = (LinearLayout) mSlidingMenu.getMenu().findViewById(R.id.login_success_layout);
		leftmenu_icon = (ImageView) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_icon);
		leftmenu_name = (TextView) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_name);
		leftmenu_comments_num = (TextView) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_comments_num);
		leftmenu_favorite_num = (TextView) mSlidingMenu.getMenu().findViewById(R.id.leftmenu_favorite_num);
		update_prompt = (ImageView) mSlidingMenu.getMenu().findViewById(R.id.update_prompt);

		mainlist_viewpager = (ViewPager) findViewById(R.id.mainlist_viewpager);
		mainlist_viewpager.setOffscreenPageLimit(3);
		mainlist_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				default:
					mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mainlist_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return fragment_list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return fragment_list.get(arg0);
			}
		});
	}

	private void signin(String uid, String username) {
		new UserSignin_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, username);
	}

	/**
	 * 用户打开
	 * 
	 * @author Administrator
	 * 
	 */
	private class UserSignin_Task extends AsyncTask<Object, Void, Login_Model> {

		@Override
		protected Login_Model doInBackground(Object... params) {
			return application.user_Signin((String) params[0], (String) params[1]);
		}

		@Override
		protected void onPostExecute(Login_Model result) {
			if (result != null && result.getData() != null) {
				Signin_User_Model signin_data = result.getData();
				leftmenu_name.setText(signin_data.getNickname());
				leftmenu_mb.setText(signin_data.getIntegral());
				// imageLoader.displayImage(signin_data.getAvatar(),
				// leftmenu_icon, BitmapUtil.getImageLoaderOption());
				// imageLoader.displayImage(login_data.getAvatar(), title_left,
				// BitmapUtil.getImageLoaderOption());
				leftmenu_comments_num.setText(signin_data.getReply_num());
				leftmenu_favorite_num.setText(signin_data.getLike_num());

				nologin_layout.setVisibility(View.INVISIBLE);
				login_success_layout.setVisibility(View.VISIBLE);

				if (result.getResult().equals("1")) {
					application.setLogin_model(result.getData());
					ToastUtil.makeText(Main_Activity.this, R.string.leftmenu_signin_success, Toast.LENGTH_SHORT);
				}
			} else {
				ToastUtil.makeText(Main_Activity.this, result.getMsg(), Toast.LENGTH_SHORT);
			}
		}
	}

	/**
	 * 获取用户未打开私信数量
	 * 
	 * @author Administrator
	 * 
	 */
	private class UnOpenMessageNum_Task extends AsyncTask<Object, Void, Integer> {
		@Override
		protected Integer doInBackground(Object... params) {
			return application.getRecentEventNum(application.getUid());
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result >= 0)
				application.setUnOpenGiftNum(result);
		}
	}

	private class AppUpdate_Task extends AsyncTask<Object, Void, Version_Model> {
		@Override
		protected Version_Model doInBackground(Object... params) {
			try {
				int currentversion = (Integer) params[0];
				Version_Model appinfo = application.getVersion();
				if (appinfo.getVersionCodeAndroid() > currentversion) {
					return appinfo;
				} else {
					return null;
				}
			} catch (Exception e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(Version_Model result) {
			if (result != null) {
				update_prompt.setVisibility(View.VISIBLE);
				appinfo = result;
				update_dialog.show();
			}

		}
	}

	private void initUpdateDialog() {
		update_dialog = new Dialog(this, R.style.retrieve_dialog);
		View version_view = LayoutInflater.from(this).inflate(R.layout.alter_update, null);
		TextView update_version = (TextView) version_view.findViewById(R.id.current_version);
		update_version.setText(versionName);
		update_dialog.setContentView(version_view);
		update_dialog.setCancelable(true);
	}

	@Override
	public void setData(ArrayList<Ad_Content_Model> datalist) {
		if (datalist != null) {
			this.datalist.clear();
			this.datalist.addAll(datalist);
			banner_adaper.notifyDataSetChanged();

			bannerHandler.removeCallbacks(banner);
			bannerHandler.post(banner);
		}
	}

	public void onResume() {
		super.onResume();
		
		toHeartBeat();
		MobclickAgent.onPageStart(getClass().getName());
		MobclickAgent.onResume(this); // 统计时长
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getName());
		MobclickAgent.onPause(this);
	}

	@Override
	public void toTop(State state, int y) {

	}

	@Override
	public void close() {
		loading_imageview.setVisibility(View.INVISIBLE);
	}

	@Override
	public void open() {
		loading_imageview.setVisibility(View.VISIBLE);
	}
}
