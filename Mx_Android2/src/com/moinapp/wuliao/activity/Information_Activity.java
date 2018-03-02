package com.moinapp.wuliao.activity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.bean.DataDetail_Bean;
import com.moinapp.wuliao.common.DataManager;
import com.moinapp.wuliao.model.Login_Model;
import com.moinapp.wuliao.model.ModifyUserInformation_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.model.UserInformation_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.CHexConver;
import com.moinapp.wuliao.util.MD5EncodeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class Information_Activity extends Base_Activity {

	private M_Application application;
	private ImageLoader imageLoader;
	private String uid, username;
	private Signin_User_Model login_data;
	private ImageView head_avatar, head_gender, avatar_iv;
	private TextView head_nickname, head_location, nickname_tv, address_tv, gender_tv, idol_tv, cellphone_tv, email_tv;

	private MyPopWindow alter_avatar_popupWindow, alter_gender_popupWindow;
	private Dialog alter_nickname_dialog, alter_idol_dialog;
	private Dialog loginOut_dialog;
	private EditText alter_nickname_et;
	private EditText alter_idol1_et, alter_idol2_et, alter_idol3_et;

	// --------------------------------------
	public static final int NO_SDCARD = -1;
	public static final int NONE = 0;
	public static final int PHOTO_HRAPH = 1;// 拍照
	public static final int PHOTO_ZOOM = 2; // 缩放
	public static final int PHOTO_RESULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (M_Application) getApplication();
		setContentView(R.layout.personalinformation_layout);

		initData();
		initView();
		initPopuptWindow1();
		initDialog1();
		initDialog2();
		initPopuptWindow2();
		initLoginOutDialog();
		new UserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		login_data = application.getLogin_model();
		uid = application.getUid();
		username = uid;
	}

	private void initView() {
		head_avatar = (ImageView) findViewById(R.id.head_avatar);
		head_nickname = (TextView) findViewById(R.id.head_nickname);
		head_gender = (ImageView) findViewById(R.id.head_gender);
		head_location = (TextView) findViewById(R.id.head_location);
		
		avatar_iv = (ImageView) findViewById(R.id.user_avatar);
		nickname_tv = (TextView) findViewById(R.id.nickname);
		address_tv = (TextView) findViewById(R.id.location);
		gender_tv = (TextView) findViewById(R.id.gender);
		idol_tv = (TextView) findViewById(R.id.idol);
		cellphone_tv = (TextView) findViewById(R.id.cellphone);
		email_tv = (TextView) findViewById(R.id.email);
		
		head_nickname.setText(login_data.getNickname());
		imageLoader.displayImage(login_data.getAvatar(), head_avatar, BitmapUtil.getImageLoaderOption());
		if("男".equals(login_data.getSex()))
			head_gender.setImageResource(R.drawable.gender_male);
		else if("女".equals(login_data.getSex()))
			head_gender.setImageResource(R.drawable.gender_female);
		head_location.setText(login_data.getAddr());
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.activity_left:
//			finish();
//			AppTools.toIntent(this, WealthCenter_Activity.class);
			break;
		case R.id.activity_right:
//			finish();
//			AppTools.toIntent(this, MyStar_Activity.class);
			break;
		case R.id.avatar_item:
			alter_avatar_popupWindow.showButtom();
			break;
		case R.id.alter_avatar_album:
			alter_avatar_popupWindow.dismiss();
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
			startActivityForResult(intent, PHOTO_ZOOM);
			break;
		case R.id.alter_avatar_cancel:
			alter_avatar_popupWindow.dismiss();
			break;

		case R.id.nickname_item:
			alter_nickname_dialog.show();
			break;
		case R.id.alter_nickname_cancel:
			alter_nickname_dialog.dismiss();
			break;
		case R.id.alter_nickname_sure:
			String nickname = alter_nickname_et.getText().toString();
			if (nickname != null && !"".equals(nickname))
				new ModifyUserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, nickname, "", "", "", "", "", "", "", username);
			alter_nickname_dialog.dismiss();
			break;

		case R.id.gender_item:
			alter_gender_popupWindow.showButtom();
			break;
		case R.id.alter_sex_male:
			alter_gender_popupWindow.dismiss();
			new ModifyUserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, "", "男", "", "", "", "", "", "", username);
			break;
		case R.id.alter_sex_female:
			alter_gender_popupWindow.dismiss();
			new ModifyUserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, "", "女", "", "", "", "", "", "", username);
			break;
		case R.id.alter_sex_unknown_gender:
			alter_gender_popupWindow.dismiss();
			new ModifyUserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, "", "性别不详", "", "", "", "", "", "", username);
			break;
		case R.id.alter_sex_cancel:
			alter_gender_popupWindow.dismiss();
			break;

		case R.id.idol_item:
			alter_idol_dialog.show();
			break;
		case R.id.alter_idol_sure:
			alter_idol_dialog.dismiss();
			String idols = alter_idol1_et.getText().toString() + " " + alter_idol2_et.getText().toString() + " " + alter_idol3_et.getText().toString();
			if (idols != null && idols.length() != 0) {
				new ModifyUserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, "", "", "", idols, "", "", "", "", username);
			}
			break;
		case R.id.alter_idol_cancel:
			alter_idol_dialog.dismiss();
			break;
		case R.id.email_item:
//			AppTools.toIntent(this, AlterEmail_Activity.class);
			break;
		case R.id.password_item:
			Bundle bundle = new Bundle();
			bundle.putString("username", username);
			AppTools.toIntent(this, AlterPassword_Activity.class);
			break;
		case R.id.login_out_item:
			loginOut_dialog.show();
			break;
		case R.id.loginout_sure:
			application.login_out();
			loginOut_dialog.dismiss();
			setResult(4);
			finish();
			break;
		case R.id.loginout_cancel:
			loginOut_dialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	private void initLoginOutDialog() {
		View version_view = LayoutInflater.from(this).inflate(R.layout.alter_loginout, null);
		loginOut_dialog = new Dialog(this, R.style.retrieve_dialog);
		loginOut_dialog.setContentView(version_view);
		loginOut_dialog.setCancelable(true);
	}

	public void initPopuptWindow1() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.alter_avatar, null);
		alter_avatar_popupWindow = new MyPopWindow(Information_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	public void initPopuptWindow2() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.alter_gender, null);
		alter_gender_popupWindow = new MyPopWindow(Information_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	private void initDialog1() {
		View alter_nickname_view = LayoutInflater.from(this).inflate(R.layout.alter_nickname, null);
		alter_nickname_et = (EditText) alter_nickname_view.findViewById(R.id.alter_nickname);
		alter_nickname_dialog = new Dialog(this, R.style.retrieve_dialog);
		alter_nickname_dialog.setContentView(alter_nickname_view);
		alter_nickname_dialog.setCancelable(true);
	}

	private void initDialog2() {
		View alter_idol_view = LayoutInflater.from(this).inflate(R.layout.alter_idol, null);
		alter_idol1_et = (EditText) alter_idol_view.findViewById(R.id.idol1);
		alter_idol2_et = (EditText) alter_idol_view.findViewById(R.id.idol2);
		alter_idol3_et = (EditText) alter_idol_view.findViewById(R.id.idol3);
		alter_idol_dialog = new Dialog(this, R.style.retrieve_dialog);
		alter_idol_dialog.setContentView(alter_idol_view);
		alter_idol_dialog.setCancelable(true);
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
						handler.sendMessage(msg);
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
								String path = BitmapUtil.saveBitmapToSDCardString(Information_Activity.this, photo, "icon_temp", 100);
								Message msg = Message.obtain();
								msg.what = 1;
								msg.obj = path;
								handler.sendMessage(msg);
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			final String path = (String) msg.obj;
			RequestParams params = new RequestParams();
			// params.addHeader("name", "value");
			// params.addQueryStringParameter("name", "value");

			// 只包含字符串参数时默认使用BodyParamsEntity，
			// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。
			// params.addBodyParameter("name", "value");

			// ----------------------
			HashMap<String, String> hashmap = new HashMap<String, String>();
			hashmap.put("uid", uid);
			hashmap.put("nickname", "");
			hashmap.put("sex", "");
			hashmap.put("birthday", "");
			hashmap.put("likestar", "");
			hashmap.put("weixin", "");
			hashmap.put("deviceid", "");
			hashmap.put("lng", "");
			hashmap.put("lat", "");

			Gson gson = new Gson();
			String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));
			params.addBodyParameter(M_Constant.DATA, hexStr + MD5EncodeUtil.MD5Encode(hexStr.getBytes()));
			// ----------------------

			// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
			// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
			// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
			// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
			// 例如发送json参数：params.setBodyEntity(new
			// StringEntity(jsonStr,charset));
			params.addBodyParameter("avatar", new File((String) msg.obj));

			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.POST, M_Constant.USER_UPDATE_USERINFO, params, new RequestCallBack<String>() {

				@Override
				public void onStart() {
					Toast.makeText(Information_Activity.this, R.string.upload_icon_star, Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					// if (isUploading) {
					// testTextView.setText("upload: " + current + "/" + total);
					// } else {
					// testTextView.setText("reply: " + current + "/" + total);
					// }
				}

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					imageLoader.displayImage("file://" + path, avatar_iv);
					Toast.makeText(Information_Activity.this, R.string.upload_icon_success, Toast.LENGTH_SHORT).show();
					try {
						DataDetail_Bean<Login_Model> result = DataManager.getDetail_Data(responseInfo.result.toString(), new TypeToken<Login_Model>() {}.getType());
						Intent intent = getIntent();
						Signin_User_Model data = result.getData().getData();
						application.writeLoginUserInformation(username, result.getData());
						intent.putExtra(M_Constant.UID, data);
						intent.putExtra("icon_path", "file://" + path);
						setResult(3, intent);
					} catch (M_Exception e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(Information_Activity.this, R.string.upload_icon_fail, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};

	private class UserInformation_Task extends AsyncTask<Object, Void, UserInformation_Model> {

		@Override
		protected UserInformation_Model doInBackground(Object... params) {
			return application.queryUserInformation(true, (String) params[0]);
		}

		@Override
		protected void onPostExecute(UserInformation_Model result) {
			if (result != null && result.getId() != null) {
//				imageLoader.displayImage(result.getAvatar(), head_avatar, BitmapUtil.getImageLoaderOption());
				if (result.getSex().equals("男")) {
					head_gender.setImageResource(R.drawable.gender_male);
				} else if (result.getSex().equals("女")) {
					head_gender.setImageResource(R.drawable.gender_female);
				}
				head_nickname.setText(result.getNickname());
				head_location.setText(result.getAddr());
				
				imageLoader.displayImage(result.getAvatar(), avatar_iv, BitmapUtil.getImageLoaderOption());
				nickname_tv.setText(result.getNickname());
				address_tv.setText(result.getAddr());
				gender_tv.setText(result.getSex());
				idol_tv.setText(result.getLikestar());
				cellphone_tv.setText(result.getPhone());
				email_tv.setText(result.getEmail());
			}

		}
	}

	private class ModifyUserInformation_Task extends AsyncTask<Object, Void, ModifyUserInformation_Model> {

		@Override
		protected ModifyUserInformation_Model doInBackground(Object... params) {
			return application.user_UpdateUserInfo(params);
		}

		@Override
		protected void onPostExecute(ModifyUserInformation_Model result) {
			if (result != null && result.getData() != null) {
				Signin_User_Model data = result.getData();
				imageLoader.displayImage(data.getAvatar(), avatar_iv, BitmapUtil.getImageLoaderOption());
				nickname_tv.setText(data.getNickname());
				gender_tv.setText(data.getSex());
				idol_tv.setText(data.getLikestar());
				cellphone_tv.setText(data.getPhone());
				email_tv.setText(data.getEmail());
				
				Intent intent = getIntent();
				intent.putExtra(M_Constant.UID, data);
				intent.putExtra("username", username);
				setResult(2, intent);
			}
		}
	}
}
