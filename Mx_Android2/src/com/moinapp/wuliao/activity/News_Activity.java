package com.moinapp.wuliao.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.activity.Expression_Activity.DeleteReply_Task;
import com.moinapp.wuliao.adapter.Comment_Adapter;
import com.moinapp.wuliao.adapter.Comment_Adapter.IdeleteReply;
import com.moinapp.wuliao.adapter.Comment_Adapter.InotifyReply;
import com.moinapp.wuliao.model.NewsDetail_Content_Model;
import com.moinapp.wuliao.model.NewsDetail_Model;
import com.moinapp.wuliao.model.Recommend_Content_Model;
import com.moinapp.wuliao.model.Reply_Content_Model;
import com.moinapp.wuliao.model.Reply_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.Mx_WebView;
import com.moinapp.wuliao.ui.view.MyListView;
import com.moinapp.wuliao.ui.view.MyPopWindow;
import com.moinapp.wuliao.ui.view.ResizeLayout;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.BitmapUtil;
import com.moinapp.wuliao.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class News_Activity extends Base_FragmentActivity implements InotifyReply, IdeleteReply {
	private MyPopWindow popupWindow;
	private LinearLayout comment_list;
	private ListView listview;
	private Comment_Adapter commentAdapter;
	private Mx_WebView news_webview;
	private TextView head_title, news_title, news_from, news_date, news_collect, to_editText, comment_send;
	private EditText comment_et;
	private ImageLoader imageLoader;
	private CheckBox collect_iv;
	private boolean collect_initial = false;
	
	private ImageView ad_iv1, ad_iv2;
	private TextView ad_tv1, ad_tv2;
	private Recommend_Content_Model ad_data1, ad_data2;

	private M_Application application;

	private String about_type, about_id;
	private ArrayList<Reply_Content_Model> list;
	private Signin_User_Model login_model;
	private String uid;
	private final String pagesize = "10";
	private int page = 1;
	private int like_num;
	private String title, pic, desr_content, share_url;
	
	private Boolean is_Reply = false;
	private String reply_to_uid;
	
	private TextView deleteReply_sure, deleteReply_cancel;
	private Dialog deleteReply_dialog;
	private String reply_id = "";

	// ------------------
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private static final int MSG_RESIZE = 1;
	private static final int HEIGHT_THREADHOLD = 30;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_RESIZE:
				if (msg.arg1 == BIGGER) {
					to_editText.setVisibility(View.VISIBLE);
					comment_et.setVisibility(View.GONE);
					comment_send.setVisibility(View.GONE);

					if ("".equals(comment_et.getText().toString()))
						to_editText.setText(comment_et.getHint().toString());
					else
						to_editText.setText(comment_et.getText().toString());
				} else {
				}
				break;
			case 10:
				likeSet(uid, about_type, about_id, M_Constant.SHARE);
			}
			super.handleMessage(msg);
		}

	};

	// ------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_layout);

		initData();
		initView();
		initDeleteDialog();
		initPopuptWindow();

		getData(about_type, about_id);
		getReply(page, false);
	}

	private void initData() {
		application = (M_Application) getApplication();
		imageLoader = ImageLoader.getInstance();
		Bundle bundle = this.getIntent().getExtras();
		about_type = bundle.getString("about_type");
		about_id = bundle.getString("about_id");

		list = new ArrayList<Reply_Content_Model>();
		login_model = application.getLogin_model();
		uid = application.getUid();

		ad_data1 = new Recommend_Content_Model();
		ad_data2 = new Recommend_Content_Model();

	}

	private void initView() {
		// ---------------------------------------
		head_title = (TextView) findViewById(R.id.title_center);
		head_title.setText(getResources().getString(R.string.news_title));
		
		ResizeLayout layout = (ResizeLayout) findViewById(R.id.root_layout);
		layout.setOnResizeListener(new ResizeLayout.OnResizeListener() {

			public void OnResize(int w, int h, int oldw, int oldh) {
				int change = BIGGER;
				if (h < oldh) {
					change = SMALLER;
				}

				Message msg = new Message();
				msg.what = 1;
				msg.arg1 = change;
				mHandler.sendMessage(msg);
			}
		});
		// ----------------------------------------

		collect_iv = (CheckBox)findViewById(R.id.collect_iv);
		if("0".equals(uid) || uid == null || "".equals(uid)) {
			collect_iv.setChecked(false);
			collect_iv.setClickable(false);
		}
		
		collect_iv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (collect_initial) {
					if (isChecked) {
						likeSet(uid, about_type, about_id, M_Constant.LIKE);
					} else {
						likeSet(uid, about_type, about_id, M_Constant.HATE);	
					}
				}
			}
		});
		news_title = (TextView) findViewById(R.id.news_title);
		news_from = (TextView) findViewById(R.id.news_from);
		news_date = (TextView) findViewById(R.id.news_date);
		news_collect = (TextView) findViewById(R.id.news_collect);
		comment_list = (LinearLayout) findViewById(R.id.comment_list);
		comment_et = (EditText) findViewById(R.id.comment_editText);
		to_editText = (TextView) findViewById(R.id.to_editText);
		comment_send = (TextView) findViewById(R.id.comment_send);
		
		comment_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (arg0.length() > 0) {
					comment_send.setBackgroundResource(R.drawable.comment_send_bg_red);
				} else {
					comment_send.setBackgroundResource(R.drawable.comment_send_bg_gray);
				}
			}
		});

		news_webview = (Mx_WebView) findViewById(R.id.news_webview);
		// 设置webview自适应屏幕大小
//		news_webview.getSettings().setUseWideViewPort(true);
//		news_webview.getSettings().setLoadWithOverviewMode(true);
		news_webview.getSettings().setJavaScriptEnabled(true);
		// news_webview.getSettings().setSupportZoom(true);
		// news_webview.getSettings().setBuiltInZoomControls(true);
		// news_webview.clearView();

		// news_webview.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// news_webview.setVerticalScrollBarEnabled(false);
		// news_webview.setVerticalScrollbarOverlay(false);
		// news_webview.setHorizontalScrollBarEnabled(false);
		// news_webview.setHorizontalScrollbarOverlay(false);

		news_webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// loadphotos();
			}
		});
//		news_webview.loadUrl("file:///android_asset/news_tpl.html");
		// news_webview.addJavascriptInterface(new JavaScriptInterface(),
		// "toolbar");

		commentAdapter = new Comment_Adapter(this, list, this, this);
		listview = (MyListView) findViewById(R.id.news_lv);
		listview.setAdapter(commentAdapter);

		ad_iv1 = (ImageView) findViewById(R.id.ad1_iv);
		ad_tv1 = (TextView) findViewById(R.id.ad1_title);
		ad_iv2 = (ImageView) findViewById(R.id.ad2_iv);
		ad_tv2 = (TextView) findViewById(R.id.ad2_title);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
		case R.id.title_right:
			AppTools.showShare(this, false, null, title, desr_content + share_url, pic, share_url, mHandler);
			break;
		case R.id.comment_send:
			String content = comment_et.getText().toString();
			if (uid != null && !"".equals(uid) && !"0".equals(uid)) {
				if (!"".equals(content) && content != null) {
					if (is_Reply) {
						new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, reply_to_uid);
					} else {
						new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, "");
					}
				} else {
					Toast.makeText(this, R.string.not_content_comment, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, R.string.not_login_comment, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.load_more_comment:
			page++;
			getReply(page, true);
			break;
		case R.id.to_editText:
			to_editText.setVisibility(View.GONE);
			comment_et.setVisibility(View.VISIBLE);
			comment_send.setVisibility(View.VISIBLE);
			opencollapseSoftInputMethod(comment_et);
			break;
		case R.id.ad1_iv:
			Bundle bundle1 = new Bundle();
			bundle1.putString("about_id", ad_data1.getAbout_id());
			bundle1.putString("about_type", ad_data1.getAbout_type());
			finish();
			if (ad_data1.getAbout_type().equals(M_Constant.FACE))
				AppTools.toIntent(this, bundle1, Expression_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.NEWS))
				AppTools.toIntent(this, bundle1, News_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.GAME))
				AppTools.toIntent(this, bundle1, Game_Activity.class);
			else if (ad_data1.getAbout_type().equals(M_Constant.VIDEO))
				AppTools.toIntent(this, bundle1, Video_Activity.class);
			break;
		case R.id.ad2_iv:
			Bundle bundle2 = new Bundle();
			bundle2.putString("about_id", ad_data2.getAbout_id());
			bundle2.putString("about_type", ad_data2.getAbout_type());
			finish();
			if (ad_data2.getAbout_type().equals(M_Constant.FACE))
				AppTools.toIntent(this, bundle2, Expression_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.NEWS))
				AppTools.toIntent(this, bundle2, News_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.GAME))
				AppTools.toIntent(this, bundle2, Game_Activity.class);
			else if (ad_data2.getAbout_type().equals(M_Constant.VIDEO))
				AppTools.toIntent(this, bundle2, Video_Activity.class);
			break;
		case R.id.announcement:
			AppTools.toIntent(this, Announcement_Activity.class);
			break;
		case R.id.deleteReply_sure:
			new DeleteReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, reply_id);
			deleteReply_dialog.dismiss();
			break;
		case R.id.deleteReply_cancel:
			deleteReply_dialog.dismiss();
			break;
		default:
			break;
		}
	}

	public void collapseSoftInputMethod(EditText editText) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
		editText.clearFocus();
	}

	public void opencollapseSoftInputMethod(EditText editText) {
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();
		InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(editText, 0);
	}
	
	private void likeSet(String uid, String about_type, String about_id, String set) {
		new SendLikeSet_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, set);
	}

	public void initPopuptWindow() {
		// 获取自定义布局文件activity_popupwindow_left.xml的视图
		View popupWindow_view = getLayoutInflater().inflate(R.layout.share_layout, null);
		popupWindow = new MyPopWindow(News_Activity.this, popupWindow_view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

		TextView cancel = (TextView) popupWindow_view.findViewById(R.id.pop_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
	}

	private void getData(String about_type, String about_id) {
		new News_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, true, about_type, about_id);
	}

	public void sendReply(String uid, String about_type, String about_id, String content, String to_id) {
		new SendReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, content, to_id);
	}

	private void getReply(int page, boolean isMore) {
		new GetReply_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uid, about_type, about_id, page + "", pagesize, isMore);
	}

	private class News_Task extends AsyncTask<Object, Void, NewsDetail_Model> {

		private boolean isRefresh;

		@Override
		protected NewsDetail_Model doInBackground(Object... params) {
			NewsDetail_Model data = null;
			isRefresh = (Boolean) params[0];
			try {
				data = application.getNewsDetailData(isRefresh, params[1], params[2]);
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(NewsDetail_Model result) {
			if (result != null) {
				NewsDetail_Content_Model content_data = result.getDetail();

				news_title.setText(content_data.getTitle());
				news_from.setText(String.format(getResources().getString(R.string.news_from), content_data.getSource()));
				news_date.setText(String.format(getResources().getString(R.string.news_date), content_data.getAddtime()));
				news_collect.setText(String.format(getResources().getString(R.string.news_collect), content_data.getLike_num()));
				
				like_num = Integer.parseInt(result.getDetail().getLike_num());
				
				title = content_data.getTitle();
				pic = result.getDetail().getPic();
				desr_content = title;
				share_url = result.getDetail().getShare_url();
				
				if (M_Constant.LIKE.equals(result.getDetail().getLikeSet())) {
					collect_iv.setChecked(true);
				} else {
					collect_iv.setChecked(false);
				}
				collect_initial = true;

				if (result.getRecommend() != null && result.getRecommend().getList() != null) {
					
					ad_data1 = result.getRecommend().getList().get(1);
					ad_data2 = result.getRecommend().getList().get(2);
					imageLoader.displayImage(ad_data1.getPic(), ad_iv1, BitmapUtil.getImageLoaderOption());
					ad_tv1.setText(ad_data1.getTitle());
					imageLoader.displayImage(ad_data2.getPic(), ad_iv2, BitmapUtil.getImageLoaderOption());
					ad_tv2.setText(ad_data2.getTitle());
				}
				
				// news_webview.loadUrl("javascript:setData(" + "'" +
				// content_data.getContent() + "'" + ")");

				// --------------
				String htmladdress = "file:///android_asset/news_tpl.html";
				String htmlname = "news_tpl.html";
				news_webview.loadDataWithBaseURL(htmladdress, processorString(initAssetsHtml(htmlname), content_data.getContent()), "text/html", "utf-8", null);
				// news_webview.loadUrl("http://192.168.1.102/yun/mx/news-tpl2.html");
				// --------------

				// list.addAll(result.getReply().getList());
				// adapter.notifyDataSetChanged();
			}

		}

	}

	public class SendReply_Task extends AsyncTask<Object, Void, Map> {

		private String content;

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
				content = (String) params[3];
				data = application.addReply(params[0], params[1], params[2], params[3], params[4]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					getReply(1, false);
					collapseSoftInputMethod(comment_et);
					comment_et.setText("");
				} else {
					System.out.println("=====>" + result.get(M_Constant.MSG).toString());
					Toast.makeText(News_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private class GetReply_Task extends AsyncTask<Object, Void, Reply_Model> {
		private boolean isMore;

		@Override
		protected Reply_Model doInBackground(Object... params) {
			isMore = (Boolean) params[5];
			Reply_Model data = null;
			try {
				data = application.getReply(true, params);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Reply_Model result) {
			if (result != null && result.getList().size() != 0) {
				comment_list.setVisibility(View.VISIBLE);
				if (isMore) {
					list.addAll(result.getList());
				} else {
					list.clear();
					list.addAll(result.getList());
				}
				commentAdapter.notifyDataSetChanged();
			} else {  // 没有数据了
				if (!isMore) {
					list.clear();
					comment_list.setVisibility(View.GONE);
				} else {
					Toast.makeText(News_Activity.this, R.string.more_end, Toast.LENGTH_SHORT).show();
				}
			} 
			
		}
	}
	
	/**
	 *	删除评论
	 */
	public class DeleteReply_Task extends AsyncTask<Object, Void, Map> {

		@Override
		protected Map doInBackground(Object... params) {
			Map data = null;
			try {
				data = application.deleteReply(params[0], params[1]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		}

		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					ToastUtil.makeText(News_Activity.this, "撤销成功", Toast.LENGTH_SHORT);
					page = 1;
					getReply(page, false);
				} else {
					ToastUtil.makeText(News_Activity.this, result.get(M_Constant.MSG).toString(), Toast.LENGTH_SHORT);
				}
			}
		}
	}
	
	private class SendLikeSet_Task extends AsyncTask<Object, Void, Map> {
		private String type;

		@Override
		protected Map doInBackground(Object... params) {
			type = (String) params[3];
			Map data = null;
			try {
				data = application.listSet(params[0], params[1], params[2], params[3]);
			} catch (M_Exception e) {
				e.printStackTrace();
			}
			return data;
		} 
		
		@Override
		protected void onPostExecute(Map result) {
			if (result != null) {
				if (result.get(M_Constant.RESULT).toString().equals("1.0")) {
					if(type.equals(M_Constant.LIKE)) 
						like_num = like_num + 1;
					else 
						like_num = like_num - 1;
					news_collect.setText(String.format(getResources().getString(R.string.news_collect), like_num));
				}
			}
		}
	}

	public class JavaScriptInterface {

		// final Handler cwjHandler = new Handler();

		@JavascriptInterface
		public void loadData() {
			getData("news", "22");
		}
	}

	private String processorString(String data, String ac) {
		String processorString = data.replace("$content", ac);
		return processorString;
	}

	private String initAssetsHtml(String htmlname) {
		AssetManager assetmanager = this.getAssets();
		InputStream inputstream;
		try {
			inputstream = assetmanager.open(htmlname);
			return readHtmlString(inputstream);

		} catch (IOException e) {
			e.printStackTrace(); // @todo exception
		}
		return "404";
	}

	private String readHtmlString(InputStream inputstream) {
		String data = "";
		BufferedReader bufferreader = new BufferedReader(new InputStreamReader(inputstream));

		try {
			while (bufferreader.ready()) {
				data += bufferreader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferreader.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return data;
	}

	@Override
	public void notifyReply(String reply_id, String reply_nickname,
			String reply_content) {
		// TODO Auto-generated method stub
		reply_to_uid = reply_id;
		to_editText.setVisibility(View.GONE);
		comment_et.setVisibility(View.VISIBLE);
		comment_send.setVisibility(View.VISIBLE);
		comment_et.requestFocus();
		InputMethodManager imm = (InputMethodManager) comment_et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
		comment_et.setText("// @" + reply_nickname + ": " + reply_content);
	}

	@Override
	public void deleteReply(String reply_id) {
		// TODO Auto-generated method stub
		this.reply_id = reply_id;
		deleteReply_dialog.show();
	}
	
	private void initDeleteDialog() {
		View loginOut_view = LayoutInflater.from(this).inflate(R.layout.delete_reply_layout, null);
		deleteReply_sure = (TextView) loginOut_view.findViewById(R.id.deleteReply_sure);
		deleteReply_cancel = (TextView) loginOut_view.findViewById(R.id.deleteReply_cancel);
		deleteReply_dialog = new Dialog(this, R.style.retrieve_dialog);
		deleteReply_dialog.setContentView(loginOut_view);
		deleteReply_dialog.setCancelable(true);
	}
}
