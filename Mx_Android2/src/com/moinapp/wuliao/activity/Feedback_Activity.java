package com.moinapp.wuliao.activity;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.util.AppTools;

public class Feedback_Activity extends Base_Activity {
	private M_Application application;
	private EditText feedback;
	private String uid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.feedback_layout);
		application = (M_Application) getApplication();
		
		initData();
		initView();
	}
	
	private void initData() {
		uid = application.getUid();
	}
	
	private void initView() {
		feedback = (EditText)findViewById(R.id.feedback);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.feedback_submit:
			String content = feedback.getText().toString();
			if(!"".equals(content) && content != null) 
			sendFeedback(uid, AppTools.getPhoneNumber(Feedback_Activity.this), content);
			break;
		}
	}
	
	private void sendFeedback(String uid, String phone, String content) {
		new Feedback_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, phone, content);
	}
	
	private class Feedback_Task extends AsyncTask<Object, Void, Map> {

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			data = application.sendFeedBack(params);
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					feedback.setText("");
				}
				
				Toast.makeText(Feedback_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}
}
