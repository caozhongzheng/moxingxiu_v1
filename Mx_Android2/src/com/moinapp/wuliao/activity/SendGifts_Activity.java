package com.moinapp.wuliao.activity;

import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SendGifts_Activity extends Base_Activity {

	private M_Application application;
	private ImageLoader imageLoader;
	private String uid;
	private ImageView mobi_10, mobi_50, mobi_100;
	private int mobi10_num, mobi50_num, mobi100_num, total = 0;
	private boolean isFirst_10, isFrist_50, isFrist_100;
	private int current_type;
	private EditText send_num;
	private TextView send_total;
	private Dialog send_mobi_dialog, charge_mobi_dialog;
	private ImageView dialog_mobi_avatar;
	private TextView dialog_gift_amount, dialog_nickname;
	private EditText dialog_charge_amount;
	private String fid, friend_avatar, friend_nickname;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sendgifts_layout);
		initData();
		initView();
	}
	
	private void initData() {
		Bundle bundle = getIntent().getExtras();
		fid = bundle.getString("fid");
		friend_avatar = bundle.getString("friend_avatar");
		friend_nickname = bundle.getString("friend_nickname");
		
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		uid = application.getUid();
		mobi10_num = 0;
		mobi50_num = 0;
		mobi100_num = 0;
		current_type = 0;
		isFirst_10 = true;
		isFrist_50 = true;
		isFrist_100 = true;
	}
	
	private void initView() {
		mobi_10 = (ImageView) findViewById(R.id.mobi_10);
		mobi_50 = (ImageView) findViewById(R.id.mobi_50);
		mobi_100 = (ImageView) findViewById(R.id.mobi_100);
		send_num = (EditText) findViewById(R.id.send_number);
		send_total = (TextView) findViewById(R.id.send_total);
		send_total.setText(total + "");
		
		send_num.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if (!hasFocus) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(send_num.getWindowToken(), 0);
				}
			}
		});
		send_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String content = send_num.getText().toString();
				if (content != null && !content.equals("")) {
					switch (current_type) {
					case 1:
						mobi10_num = Integer.parseInt(content);
						count();
						break;
					case 2:
						mobi50_num = Integer.parseInt(content);
						count();
						break;
					case 3:
						mobi100_num = Integer.parseInt(content);
						count();
						break;
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}
		});
	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(send_num.getWindowToken(), 0);
//		return true;
//	}
	
	private void initSendDialog() {
		View send_mobi_view = LayoutInflater.from(this).inflate(R.layout.send_mobi_dialog, null);
		dialog_mobi_avatar = (ImageView) send_mobi_view.findViewById(R.id.dialog_mobi_avatar);
		dialog_gift_amount = (TextView) send_mobi_view.findViewById(R.id.dialog_gift_amount);
		dialog_nickname = (TextView) send_mobi_view.findViewById(R.id.dialog_nickname);
		imageLoader.displayImage(friend_avatar, dialog_mobi_avatar, BitmapUtil.getImageLoaderOption());
		dialog_gift_amount.setText(total + "");
		dialog_nickname.setText(friend_nickname + "");
		
		send_mobi_dialog = new Dialog(this, R.style.retrieve_dialog);
		send_mobi_dialog.setContentView(send_mobi_view);
		send_mobi_dialog.setCancelable(true);
		send_mobi_dialog.show();
	}
	
	private void initChargeDialog() {
		View charge_mobi_view = LayoutInflater.from(this).inflate(R.layout.charge_mobi_dialog, null);
		dialog_charge_amount = (EditText) charge_mobi_view.findViewById(R.id.charge_amount);
		charge_mobi_dialog = new Dialog(this, R.style.retrieve_dialog);
		charge_mobi_dialog.setContentView(charge_mobi_view);
		charge_mobi_dialog.setCancelable(true);
		charge_mobi_dialog.show();
	}
	
	private void initToast() {
		View send_mobi_success = LayoutInflater.from(this).inflate(R.layout.send_mobi_success, null);
		TextView send_totle = (TextView) send_mobi_success.findViewById(R.id.send_success_total);
		TextView send_nickname = (TextView) send_mobi_success.findViewById(R.id.send_success_nickname);
		send_totle.setText(total + "");
		send_nickname.setText(friend_nickname);
		Toast toast = new Toast(this);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(send_mobi_success);
		toast.show();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.send_friend:
			if (total > 0) {
				initSendDialog();
			} else {
				ToastUtil.makeText(this, R.string.send_empty_tip, Toast.LENGTH_SHORT);
			}
			break;
		case R.id.mobi_10:
			if (isFirst_10) {
				mobi10_num++;
				isFirst_10 = false;
			}
			send_num.clearFocus();
			current_type = 10;
			setSelect();
			send_num.setText(mobi10_num + "");
			count(current_type);
			break;
		case R.id.mobi_50:
			if (isFrist_50) {
				mobi50_num++;
				isFrist_50 = false;
			}
			send_num.clearFocus();
			current_type = 50;
			setSelect();
			send_num.setText(mobi50_num + "");
			count(current_type);
			break;
		case R.id.mobi_100:
			if (isFrist_100) {
				mobi100_num++;
				isFrist_100 = false;
			}
			send_num.clearFocus();
			current_type = 100;
			setSelect();
			send_num.setText(mobi100_num + "");
			count(current_type);
			break;
		case R.id.decrease:
			send_num.clearFocus();
			switch (current_type) {
			case 10:
				if (mobi10_num != 0) {
					mobi10_num--;
					send_num.setText(mobi10_num + "");
//					count();
					count(10);
				} 
				break;
			case 50:
				if (mobi50_num != 0) {
					mobi50_num--;
					send_num.setText(mobi50_num + "");
//					count();
					count(50);
				} 
				break;
			case 100:
				if (mobi100_num != 0) {
					mobi100_num--;
					send_num.setText(mobi100_num + "");
//					count();
					count(100);
				} 
				break;
			}
			break;
		case R.id.crease:
			send_num.clearFocus();
			switch (current_type) {
			case 10:
				mobi10_num++;
				send_num.setText(mobi10_num + "");
//				count();
				count(10);
				break;
			case 50:
				mobi50_num++;
				send_num.setText(mobi50_num + "");
//				count();
				count(50);
				break;
			case 100:
				mobi100_num++;
				send_num.setText(mobi100_num + "");
//				count();
				count(100);
				break;
			}
			break;
		case R.id.dialog_send_sure:
			new SendGift_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, application.getUid(), fid, "integral", total + "", "");
			break;
		case R.id.dialog_send_cancel:
			send_mobi_dialog.dismiss();
			break;
		case R.id.dialog_charge_sure:
			charge_mobi_dialog.dismiss();
			break;
		case R.id.dialog_charge_cancel:
			charge_mobi_dialog.dismiss();
			break;
		}
	}
	
	private void setSelect() {
		switch (current_type) {
		case 1:
			mobi_10.setBackgroundResource(R.drawable.mb10_checked);
			mobi_50.setBackgroundResource(R.drawable.mb50_unchecked);
			mobi_100.setBackgroundResource(R.drawable.mb100_unchecked);
			break;
		case 2:
			mobi_10.setBackgroundResource(R.drawable.mb10_unchecked);
			mobi_50.setBackgroundResource(R.drawable.mb50_checked);
			mobi_100.setBackgroundResource(R.drawable.mb100_unchecked);
			break;
		case 3:
			mobi_10.setBackgroundResource(R.drawable.mb10_unchecked);
			mobi_50.setBackgroundResource(R.drawable.mb50_unchecked);
			mobi_100.setBackgroundResource(R.drawable.mb100_checked);
			break;
		}
	}
	
	private void count() {
		total = mobi10_num*10 + mobi50_num*50 + mobi100_num*100;
		send_total.setText(total + "");
	}
	
	private void count(int type) {
		switch (type) {
		case 10:
			total = mobi10_num * 10;
			break;
		case 50:
			total = mobi50_num * 50;
			break;
		case 100:
			total = mobi100_num * 100;
			break;
		}
		send_total.setText(total + "");
	}
	
	private class SendGift_Task extends AsyncTask<Object, Void, Map> {

		@Override
		protected Map doInBackground(Object... params) {
			return application.sendGift((String)params[0], (String)params[1], (String)params[2], (String)params[3], (String)params[4]);
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
				send_mobi_dialog.dismiss();
				initToast();
			} else {
				send_mobi_dialog.dismiss();
				Toast.makeText(SendGifts_Activity.this, "魔币不足", Toast.LENGTH_SHORT).show();	//(String)result.get("msg")
//				initChargeDialog();
			}
		}
	}
}
