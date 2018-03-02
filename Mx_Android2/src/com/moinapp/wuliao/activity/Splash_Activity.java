package com.moinapp.wuliao.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.moinapp.wuliao.R;
import com.moinapp.wuliao.util.AppTools;
import com.zyh.task.MainActivity;

public class Splash_Activity extends Base_Activity {
	private final static int SWITCH_MAINACTIVITY = 1000;
	private final static int SWITCH_GUIDACTIVITY = 1001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_layout);
		
		boolean mFirst = AppTools.isFirstEnter(this, this.getClass().getName());
        if (mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 3000);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 3000);

	}
	
	//*************************************************
    // Handler:跳转至不同页面
    //*************************************************
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
            case SWITCH_MAINACTIVITY:
                Intent mIntent = new Intent();
                mIntent.setClass(Splash_Activity.this, Main_Activity.class);
                startActivity(mIntent);
                finish();
                break;
            case SWITCH_GUIDACTIVITY:
                mIntent = new Intent();
                mIntent.setClass(Splash_Activity.this, Guide_Activity.class);
                startActivity(mIntent);
                finish();
                break;
            }
            super.handleMessage(msg);
        }
    };

	
	

}
