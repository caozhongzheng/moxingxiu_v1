package com.moinapp.wuliao.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.model.UserInformation_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.R;

public class WealthCenter_Fragment extends Base_Fragment {
	private M_Application application;
	private TextView tv1, tv2, tv3;
 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.wealthcenter_layout, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initView();
		new UserInformation_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, application.getUid());
	}
	
	private void initData() {
		application = (M_Application) getActivity().getApplication();
	}
	
	private void initView() {
		tv1 = (TextView) getActivity().findViewById(R.id.number1);
		tv2 = (TextView) getActivity().findViewById(R.id.number2);
		tv3 = (TextView) getActivity().findViewById(R.id.number3);
	}
	
	private class UserInformation_Task extends AsyncTask<Object, Void, UserInformation_Model> {
		@Override
		protected UserInformation_Model doInBackground(Object... params) {
			return application.queryUserInformation(true, (String) params[0]);
		}

		@Override
		protected void onPostExecute(UserInformation_Model result) {
			if (result != null && result.getId() != null) {
				tv1.setText(result.getOnline_time());
				tv2.setText(result.getIntegral());
				tv3.setText(result.getShare_num());
			}
		}
	}

}
