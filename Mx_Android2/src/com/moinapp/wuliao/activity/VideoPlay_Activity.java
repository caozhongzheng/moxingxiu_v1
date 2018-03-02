package com.moinapp.wuliao.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.moinapp.wuliao.R;

public class VideoPlay_Activity extends Base_Activity {

	private String url;
	private WebView video_play_webview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.video_play_layout);
		
		initData();
		initView();
	}
	
	private void initData() {
		url = getIntent().getExtras().getString("url");
	}
	
	private void initView() {
		video_play_webview = (WebView)findViewById(R.id.video_play_webview);
		video_play_webview.setWebViewClient(new WebViewClient());
		video_play_webview.getSettings().setUseWideViewPort(true);
		video_play_webview.getSettings().setLoadWithOverviewMode(true);
		video_play_webview.getSettings().setJavaScriptEnabled(true);
		video_play_webview.loadUrl(url);
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.title_left:
			finish();
			break;
		}
	}
}
