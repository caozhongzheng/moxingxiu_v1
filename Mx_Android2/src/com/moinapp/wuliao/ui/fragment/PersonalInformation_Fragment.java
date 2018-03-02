package com.moinapp.wuliao.ui.fragment;

import java.io.File;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.moinapp.wuliao.activity.AlterEmail_Activity;
import com.moinapp.wuliao.activity.AlterPassword_Activity;
import com.moinapp.wuliao.activity.Function_Activity;
import com.moinapp.wuliao.bean.DataDetail_Bean;
import com.moinapp.wuliao.common.DataManager;
import com.moinapp.wuliao.model.Login_Model;
import com.moinapp.wuliao.model.ModifyUserInformation_Model;
import com.moinapp.wuliao.model.RememberUser;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.model.UserInformation_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.CHexConver;
import com.moinapp.wuliao.util.MD5EncodeUtil;
import com.moinapp.wuliao.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PersonalInformation_Fragment extends Base_Fragment implements OnClickListener {

	private M_Application application;
	private ImageLoader imageLoader;
	private String uid, username;
	private Signin_User_Model login_data;
	private ImageView avatar_iv;
	private TextView nickname_tv, address_tv, gender_tv, idol_tv, cellphone_tv, email_tv;

	private MyPopWindow alter_avatar_popupWindow, alter_gender_popupWindow;
	private Dialog alter_nickname_dialog, alter_idol_dialog;
	private Dialog loginOut_dialog;
	private EditText alter_nickname_et;
	private EditText alter_idol1_et, alter_idol2_et, alter_idol3_et;
	
	private RelativeLayout avatar_item, nickname_item, location_item, gender_item, idol_item, email_item, password_item, login_out_item;
	private TextView alter_avatar_album, alter_avatar_cancel, alter_nickname_cancel, alter_nickname_sure, alter_sex_male, alter_sex_female,
			alter_sex_unknown_gender, alter_sex_cancel,  alter_idol_sure, alter_idol_cancel, loginout_sure, loginout_cancel;

	// --------------------------------------
	public static final int NO_SDCARD = -1;
	public static final int NONE = 0;
	public static final int PHOTO_HRAPH = 1;// 拍照
	public static final int PHOTO_ZOOM = 2; // 缩放
	public static final int PHOTO_RESULT = 3;// 结果
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	private Function_Activity mActivity;
	
	private Handler fragment_handler = new Handler() {
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
					Toast.makeText(getActivity(), R.string.upload_icon_star, Toast.LENGTH_SHORT).show();
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
					mActivity.loadAvatar("file://" + path);
					Toast.makeText(getActivity(), R.string.upload_icon_success, Toast.LENGTH_SHORT).show();
					try {
						DataDetail_Bean<Login_Model> result = DataManager.getDetail_Data(responseInfo.result.toString(), new TypeToken<Login_Model>() {}.getType());
						Intent intent = getActivity().getIntent();
						Signin_User_Model data = result.getData().getData();
						application.writeLoginUserInformation(username, result.getData());
						intent.putExtra(M_Constant.UID, data);
						intent.putExtra("icon_path", "file://" + path);
						getActivity().setResult(3, intent);
					} catch (M_Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(getActivity(), R.string.upload_icon_fail, Toast.LENGTH_SHORT).show();
				}
			});
		}
	};
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mActivity.setHandler(fragment_handler);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.personalinformation_layout, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initView();
		initPopuptWindow1();
		initPopuptWindow2();
		initDialog1();
		initDialog2();
		initLoginOutDialog();
		new UserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (Function_Activity) activity;
		 
		mActivity.setHandler(fragment_handler);
	}
	
	private void initData() {
		application = (M_Application) getActivity().getApplication();
		imageLoader = ImageLoader.getInstance();
		login_data = application.getLogin_model();
		uid = application.getUid();
		RememberUser rememberUser = AppTools.getRemeberUsernameAndPassword(getActivity());
		username = rememberUser.getUsername();
	}
	
	private void initView() {
		avatar_iv = (ImageView) getActivity().findViewById(R.id.user_avatar);
		nickname_tv = (TextView) getActivity().findViewById(R.id.nickname);
		address_tv = (TextView) getActivity().findViewById(R.id.location);
		gender_tv = (TextView) getActivity().findViewById(R.id.gender);
		idol_tv = (TextView) getActivity().findViewById(R.id.idol);
		cellphone_tv = (TextView) getActivity().findViewById(R.id.cellphone);
		email_tv = (TextView) getActivity().findViewById(R.id.email);
		
		avatar_item = (RelativeLayout) getActivity().findViewById(R.id.avatar_item);
		nickname_item = (RelativeLayout) getActivity().findViewById(R.id.nickname_item);
		location_item = (RelativeLayout) getActivity().findViewById(R.id.location_item);
		gender_item = (RelativeLayout) getActivity().findViewById(R.id.gender_item);
		idol_item = (RelativeLayout) getActivity().findViewById(R.id.idol_item);
		email_item = (RelativeLayout) getActivity().findViewById(R.id.email_item);
		password_item = (RelativeLayout) getActivity().findViewById(R.id.password_item);
		login_out_item = (RelativeLayout) getActivity().findViewById(R.id.login_out_item);
		avatar_item.setOnClickListener(this);
		nickname_item.setOnClickListener(this);
		location_item.setOnClickListener(this);
		gender_item.setOnClickListener(this);
		idol_item.setOnClickListener(this);
		email_item.setOnClickListener(this);
		password_item.setOnClickListener(this);
		login_out_item.setOnClickListener(this);
	}
	
	private void initLoginOutDialog() {
		View loginOut_view = LayoutInflater.from(getActivity()).inflate(R.layout.alter_loginout, null);
		loginout_sure = (TextView) loginOut_view.findViewById(R.id.loginout_sure);
		loginout_cancel = (TextView) loginOut_view.findViewById(R.id.loginout_cancel);
		loginout_sure.setOnClickListener(this);
		loginout_cancel.setOnClickListener(this);
		loginOut_dialog = new Dialog(getActivity(), R.style.retrieve_dialog);
		loginOut_dialog.setContentView(loginOut_view);
		loginOut_dialog.setCancelable(true);
	}
	
	/**
	 *  修改头像
	 */
	public void initPopuptWindow1() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.alter_avatar, null);
		alter_avatar_album = (TextView) popupWindow_view.findViewById(R.id.alter_avatar_album);
		alter_avatar_cancel = (TextView) popupWindow_view.findViewById(R.id.alter_avatar_cancel);
		alter_avatar_album.setOnClickListener(this);
		alter_avatar_cancel.setOnClickListener(this);
		alter_avatar_popupWindow = new MyPopWindow(getActivity(), popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 *  修改性别
	 */
	public void initPopuptWindow2() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.alter_gender, null);
		alter_sex_male = (TextView) popupWindow_view.findViewById(R.id.alter_sex_male);
		alter_sex_female = (TextView) popupWindow_view.findViewById(R.id.alter_sex_female);
		alter_sex_unknown_gender = (TextView) popupWindow_view.findViewById(R.id.alter_sex_unknown_gender);
		alter_sex_cancel = (TextView) popupWindow_view.findViewById(R.id.alter_sex_cancel);
		alter_sex_male.setOnClickListener(this);
		alter_sex_female.setOnClickListener(this);
		alter_sex_unknown_gender.setOnClickListener(this);
		alter_sex_cancel.setOnClickListener(this);
		alter_gender_popupWindow = new MyPopWindow(getActivity(), popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 *  修改昵称对话框
	 */
	private void initDialog1() {
		View alter_nickname_view = LayoutInflater.from(getActivity()).inflate(R.layout.alter_nickname, null);
		alter_nickname_et = (EditText) alter_nickname_view.findViewById(R.id.alter_nickname);
		alter_nickname_cancel = (TextView) alter_nickname_view.findViewById(R.id.alter_nickname_cancel);
		alter_nickname_sure = (TextView) alter_nickname_view.findViewById(R.id.alter_nickname_sure);
		alter_nickname_cancel.setOnClickListener(this);
		alter_nickname_sure.setOnClickListener(this);
		alter_nickname_dialog = new Dialog(getActivity(), R.style.retrieve_dialog);
		alter_nickname_dialog.setContentView(alter_nickname_view);
		alter_nickname_dialog.setCancelable(true);
	}

	/**
	 *  修改喜欢的明星
	 */
	private void initDialog2() {
		View alter_idol_view = LayoutInflater.from(getActivity()).inflate(R.layout.alter_idol, null);
		alter_idol1_et = (EditText) alter_idol_view.findViewById(R.id.idol1);
		alter_idol2_et = (EditText) alter_idol_view.findViewById(R.id.idol2);
		alter_idol3_et = (EditText) alter_idol_view.findViewById(R.id.idol3);
		alter_idol_sure = (TextView) alter_idol_view.findViewById(R.id.alter_idol_sure);
		alter_idol_cancel = (TextView) alter_idol_view.findViewById(R.id.alter_idol_cancel);
		alter_idol_sure.setOnClickListener(this);
		alter_idol_cancel.setOnClickListener(this);
		alter_idol_dialog = new Dialog(getActivity(), R.style.retrieve_dialog);
		alter_idol_dialog.setContentView(alter_idol_view);
		alter_idol_dialog.setCancelable(true);
	}
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);
		intent.putExtra("return-data", true);

		getActivity().startActivityForResult(intent, PHOTO_RESULT);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.avatar_item:
			alter_avatar_popupWindow.showButtom();
			break;
		case R.id.alter_avatar_album:
			alter_avatar_popupWindow.dismiss();
			Intent intent = new Intent(Intent.ACTION_PICK, null);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
			getActivity().startActivityForResult(intent, PHOTO_ZOOM);
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
//			AppTools.toIntent(getActivity(), AlterEmail_Activity.class);
			break;

		case R.id.password_item:
			Bundle bundle = new Bundle();
			bundle.putString("username", username);
			AppTools.toIntent(getActivity(), AlterPassword_Activity.class);
			break;
		case R.id.login_out_item:
			loginOut_dialog.show();
			break;
		case R.id.loginout_sure:
			application.login_out();
			loginOut_dialog.dismiss();
			getActivity().setResult(4);
			getActivity().finish();
			break;
		case R.id.loginout_cancel:
			loginOut_dialog.dismiss();
			break;
		default:
			break;
		}
	}

	private class UserInformation_Task extends AsyncTask<Object, Void, UserInformation_Model> {
		@Override
		protected UserInformation_Model doInBackground(Object... params) {
			return application.queryUserInformation(true, (String) params[0]);
		}

		@Override
		protected void onPostExecute(UserInformation_Model result) {
			if (result != null && result.getId() != null) {
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
				
				Intent intent = getActivity().getIntent();
				intent.putExtra(M_Constant.UID, data);
				intent.putExtra("username", username);
				getActivity().setResult(2, intent);
			}
		}
	}


}
