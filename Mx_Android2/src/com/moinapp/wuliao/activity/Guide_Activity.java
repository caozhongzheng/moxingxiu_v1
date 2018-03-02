package com.moinapp.wuliao.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.util.AppTools;

public class Guide_Activity extends Base_Activity {

	private final int SPLASH_DISPLAY_LENGHT = 4000; // 延迟六秒
	private Handler handler;
	private Runnable runnable;
	private M_Application application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (M_Application) getApplication();
		setContentView(R.layout.guide_layout);
		application.initData();

		// GifImageView splash_imageview =
		// (GifImageView)findViewById(R.id.splash_imageview);
		//
		// try {
		// GifDrawable gifFromPath = new GifDrawable(getAssets(), "splash.gif");
		// splash_imageview.setImageDrawable(gifFromPath);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		ImageView s_image = (ImageView) findViewById(R.id.s_image);
		
		s_image.setBackgroundResource(R.anim.splash_anim);  
		AnimationDrawable animationDrawable = (AnimationDrawable) s_image.getBackground();  
        animationDrawable.start();//开始  

		handler = new Handler();
		runnable = new Runnable() {
			public void run() {
				// if (AppTools.isFirstRun(Splash_Activity.this)) {
				// AppTools.setFirstRun(Splash_Activity.this);
				// AppTools.toIntent(Splash_Activity.this,
				// Guide_Activity.class);
				// finish();
				// } else {
				AppTools.toIntent(Guide_Activity.this, Main_Activity.class);
				AppTools.setGuided(Guide_Activity.this);
				finish();
				// }
			}
		};
		handler.postDelayed(runnable, SPLASH_DISPLAY_LENGHT);
	}

}
