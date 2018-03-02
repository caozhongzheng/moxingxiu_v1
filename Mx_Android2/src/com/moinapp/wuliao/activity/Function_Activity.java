package com.moinapp.wuliao.activity;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.ui.fragment.MyHistory_Fragment;
import com.moinapp.wuliao.ui.fragment.MyStar_Fragment.Type_Num;
import com.moinapp.wuliao.ui.fragment.PersonalInformation_Fragment;
import com.moinapp.wuliao.ui.fragment.WealthCenter_Fragment;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Function_Activity extends Base_FragmentActivity implements Type_Num {

	public M_Application application;
	public ImageLoader imageLoader;
	public Signin_User_Model login_data;
	public String uid;
	private String username;
	
	private TextView title_name;
	private ImageView head_avatar, head_gender;
	private TextView head_nickname, head_location;
	private LinearLayout friend_ll, message_ll;
	public TextView friend_num, message_num;
	
	private ViewPager function_vp;
	private ArrayList<Fragment> fragment_list;
	private int current_position;
	private ImageView dot1, dot2, dot3;
	
	// --------------------------------------
	public static final int NO_SDCARD = -1;
	public static final int NONE = 0;
	public static final int PHOTO_HRAPH = 1;// 拍照
	public static final int PHOTO_ZOOM = 2; // 缩放
	public static final int PHOTO_RESULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	private Handler mHandler;
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.function_layout);
		
		initData();
		initView();
	}
	
	private void initData() {
		Bundle bundle = getIntent().getExtras();
		current_position = (Integer) bundle.get("position");
		username = bundle.getString("username");
		imageLoader = ImageLoader.getInstance();
		application = (M_Application) getApplication();
		login_data = application.getLogin_model();
		uid = application.getUid();
		
		fragment_list = new ArrayList<Fragment>();
		fragment_list.add(new WealthCenter_Fragment());
		fragment_list.add(new PersonalInformation_Fragment());
		fragment_list.add(new MyHistory_Fragment());
		
	}
	
	public void loadAvatar(String url) {
		imageLoader.displayImage(url, head_avatar);
	}
	
	private void initView() {
		title_name = (TextView) findViewById(R.id.title_name);
		
		head_avatar = (ImageView) findViewById(R.id.head_avatar);
		head_nickname = (TextView) findViewById(R.id.head_nickname);
		head_gender = (ImageView) findViewById(R.id.head_gender);
		head_location = (TextView) findViewById(R.id.head_location);
		dot1 = (ImageView) findViewById(R.id.dot1);
		dot2 = (ImageView) findViewById(R.id.dot2);
		dot3 = (ImageView) findViewById(R.id.dot3);
		
//		friend_ll = (LinearLayout) findViewById(R.id.friend_ll);
//		message_ll = (LinearLayout) findViewById(R.id.message_ll);
//		friend_num = (TextView) findViewById(R.id.my_friend);
//		message_num = (TextView) findViewById(R.id.my_message);
//		if (current_position == 2) {
//			friend_ll.setVisibility(View.VISIBLE);
//			message_ll.setVisibility(View.VISIBLE);
//		}
		
		head_nickname.setText(login_data.getNickname());
		imageLoader.displayImage(login_data.getAvatar(), head_avatar, BitmapUtil.getImageLoaderOption());
		if ("男".equals(login_data.getSex()))
			head_gender.setImageResource(R.drawable.gender_male);
		else if ("女".equals(login_data.getSex()))
			head_gender.setImageResource(R.drawable.gender_female);
		head_location.setText(login_data.getAddr());
		
		function_vp = (ViewPager) findViewById(R.id.function_viewpager);
		function_vp.setOffscreenPageLimit(3);
		function_vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return fragment_list.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return fragment_list.get(arg0);
			}
		}); 
		setTitleName(current_position);
	}
	
	private void setTitleName(int position) {
		switch (position) {
		case 0:
			title_name.setText(getResources().getText(R.string.wealthcenter_head_title));
//			friend_ll.setVisibility(View.INVISIBLE);
//			message_ll.setVisibility(View.INVISIBLE);
			dot1.setImageResource(R.drawable.information_dot_filled);
			dot2.setImageResource(R.drawable.information_dot_empty);
			dot3.setImageResource(R.drawable.information_dot_empty);
			break;
		case 1:
			title_name.setText(getResources().getText(R.string.information_title));
//			friend_ll.setVisibility(View.INVISIBLE);
//			message_ll.setVisibility(View.INVISIBLE);
			dot1.setImageResource(R.drawable.information_dot_empty);
			dot2.setImageResource(R.drawable.information_dot_filled);
			dot3.setImageResource(R.drawable.information_dot_empty);
			break;
		case 2:
			title_name.setText(getResources().getText(R.string.mystar_head_title));
//			friend_ll.setVisibility(View.VISIBLE);
//			message_ll.setVisibility(View.VISIBLE);
			dot1.setImageResource(R.drawable.information_dot_empty);
			dot2.setImageResource(R.drawable.information_dot_empty);
			dot3.setImageResource(R.drawable.information_dot_filled);
		default:
			break;
		}
		function_vp.setCurrentItem(position);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.activity_left:
			if (current_position > 0) {
				current_position--;
			} else if (current_position == 0) {
				current_position = 2;
			}
			setTitleName(current_position);
			break;
		case R.id.activity_right:
			if (current_position < 2) {
				current_position++;
			} else if (current_position == 2) {
				current_position = 0;
			}
			setTitleName(current_position);
			break;
			
//		case R.id.friend_ll:
//			AppTools.toIntent(this, MyFriends_Activity.class);
//			break;
//		case R.id.message_ll:
//			AppTools.toIntent(this, MyMessages_Activity.class);
//			break;
		default:
			break;
		}
	}
	
	@Override
	public void setTypenum(String type, String num) {
		// TODO Auto-generated method stub
		if(M_Constant.DEFAULT.equals(type)) {
//			mystar_num.setText(num);
		} else if(M_Constant.FRIEND.equals(type)) {
			friend_num.setText(num);
		} else if(M_Constant.MESSAGE.equals(type)) {
			message_num.setText(num);
//		} else if(M_Constant.GIFT.equals(type)) {
//			mystar_gift_num.setText(num);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case NO_SDCARD:
			Toast.makeText(this, "没有检测到SD卡", Toast.LENGTH_SHORT).show();
			break;
		case PHOTO_ZOOM:
			if (resultCode == NONE)
				return;

			Uri originalUri = data.getData();
			if (resultCode == RESULT_OK) {
				ContentResolver cr = this.getContentResolver();
				Cursor cursor = cr.query(originalUri, null, null, null, null);
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					cursor.getString(i);
				}

				String file_path = cursor.getString(1); // 获取从相册选择到的图片路径

				if (file_path != null && !"".equals(file_path)) {

					Bitmap bitmap = imageLoader.loadImageSync(originalUri.toString());
					if (bitmap.getHeight() > 100 || bitmap.getWidth() > 100) {
						startPhotoZoom(data.getData());
					} else {
						Message msg = Message.obtain();
						msg.what = 1;
						msg.obj = file_path;
						mHandler.sendMessage(msg);
					}
				}
			}
			break;
		case PHOTO_HRAPH:
			break;
		case PHOTO_RESULT:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					final Bitmap photo = extras.getParcelable("data");

					new Thread() {
						@Override
						public void run() {
							try {
								// 向SD卡中写入图片缓存
								String path = BitmapUtil.saveBitmapToSDCardString(Function_Activity.this, photo, "icon_temp", 100);
								Message msg = Message.obtain();
								msg.what = 1;
								msg.obj = path;
								mHandler.sendMessage(msg);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

					}.start();

				}
			}
		}
	}
	
	/**
	 * @param uri
	 * 裁剪
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_RESULT);
	}
}
