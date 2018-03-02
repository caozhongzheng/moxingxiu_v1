package com.moinapp.wuliao.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.moinapp.wuliao.M_Application;
import com.moinapp.wuliao.M_Constant;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.R;
import com.moinapp.wuliao.activity.Expression_Activity;
import com.moinapp.wuliao.activity.Game_Activity;
import com.moinapp.wuliao.activity.News_Activity;
import com.moinapp.wuliao.activity.Video_Activity;
import com.moinapp.wuliao.adapter.BannerViewPagerAdapter;
import com.moinapp.wuliao.adapter.Main_Adapter;
import com.moinapp.wuliao.adapter.Main_Adapter2;
import com.moinapp.wuliao.model.Ad_Content_Model;
import com.moinapp.wuliao.model.MainTimelineItem_Model;
import com.moinapp.wuliao.model.MainTimeline_Model;
import com.moinapp.wuliao.task.AsyncTask;
import com.moinapp.wuliao.ui.view.VerticalViewPager;
import com.moinapp.wuliao.util.AppTools;

public class MainList_Fragment extends Base_Fragment {

	private String type;
	private PullToRefreshScrollView ms;
	private ListView ls1, ls2;
	private M_Application application;
	private Main_Adapter adapter;
	private Main_Adapter2 adapter2;
	private ArrayList<MainTimelineItem_Model> data_list;
	private ArrayList<MainTimelineItem_Model> left_data_list;
	private ArrayList<MainTimelineItem_Model> right_data_list;
	private int position = 1;
	private RecommendDataListener rdl;
	private ScrollToStarEndListener stsel;
	private int pagesize = 10;
	private ScrollView sv;
	private MainTimeline_Model init_cache1;
	private MainTimeline_Model init_cache2;
	private MainTimeline_Model init_cache3;

	// ------------------
	private VerticalViewPager banner_v;
	private BannerViewPagerAdapter banner_adaper;
	private ArrayList<Ad_Content_Model> datalista;

	// ------------------

	public void setRdl(RecommendDataListener rdl) {
		this.rdl = rdl;
	}

	public void setStsel(ScrollToStarEndListener stsel) {
		this.stsel = stsel;
	}

	public MainList_Fragment(String type) {
		this.type = type;
	}

	private Handler bannerHandler = new Handler();
	private Runnable banner = new Runnable() {

		@Override
		public void run() {
			bannerHandler.postDelayed(banner, 5000);

			if(banner_v != null) {
			if (banner_v.getCurrentItem() == banner_v.getChildCount() - 1)
				banner_v.setCurrentItem(0, true);
			else
				banner_v.setCurrentItem(banner_v.getCurrentItem() + 1, true);
			}
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		bannerHandler.removeCallbacks(banner);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (M_Application) getActivity().getApplication();
		init_cache1 = (MainTimeline_Model) application.getCatchModelForKey(M_Constant.DEFAULT);
		init_cache2 = (MainTimeline_Model) application.getCatchModelForKey(M_Constant.FACE);
		init_cache3 = (MainTimeline_Model) application.getCatchModelForKey(M_Constant.NEWS);

		setRdl((RecommendDataListener) getActivity());
		setStsel((ScrollToStarEndListener) getActivity());

		initData();
	}

	public void openBaner() {
		bannerHandler.removeCallbacks(banner);
	}

	public void closeBaner() {
		bannerHandler.post(banner);
	}

	public void loadDataForType(String type) {
		position = 1;
		this.type = type;
		
		if (type.equals(M_Constant.DEFAULT))
			setinitData(init_cache1);
		else if (type.equals(M_Constant.FACE))
			setinitData(init_cache2);
		else if (type.equals(M_Constant.NEWS))
			setinitData(init_cache3);

		this.type = type;
		getData(type, position, false);
	}

	private void initData() {
		data_list = new ArrayList<MainTimelineItem_Model>();
		left_data_list = new ArrayList<MainTimelineItem_Model>();
		right_data_list = new ArrayList<MainTimelineItem_Model>();

		adapter = new Main_Adapter(getActivity(), left_data_list);
		adapter2 = new Main_Adapter2(getActivity(), right_data_list);

		datalista = new ArrayList<Ad_Content_Model>();
		banner_adaper = new BannerViewPagerAdapter(getActivity(), datalista);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		initView();
		getData(type, position, false);
		setinitData(init_cache1);

		mHandler.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.mainlist_fragment_layout, container, false);
	}

	private void initView() {
		ls1 = (ListView) getActivity().findViewById(R.id.ls1);
		ls2 = (ListView) getActivity().findViewById(R.id.ls2);

		ms = (PullToRefreshScrollView) getActivity().findViewById(R.id.ms);

		ms.setOnPullEventListener(new OnPullEventListener() {

			@Override
			public void onPullEvent(PullToRefreshBase refreshView, State state, Mode direction) {
				// stsel.toTop(state, sv.getScrollY());
			}
		});

		ms.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				getData(type, 1, false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				position++;
				getData(type, position, true);
			}
		});

		ls1.setAdapter(adapter);
		ls2.setAdapter(adapter2);

		sv = ms.getRefreshableView();
		sv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					openBaner();
					break;
				case MotionEvent.ACTION_MOVE:
					// scrollY1 = v.getScrollY();
					// stsel.toTop(null, scrollY1);
					break;
				case MotionEvent.ACTION_CANCEL:
					openBaner();
					closeBaner();
					break;
				case MotionEvent.ACTION_UP:
					openBaner();
					closeBaner();
					break;

				}
				return false;
			}
		});

		ls1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainTimelineItem_Model data = left_data_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if (data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(getActivity(), bundle, Expression_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(getActivity(), bundle, News_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(getActivity(), bundle, Game_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(getActivity(), bundle, Video_Activity.class);

			}
		});

		ls2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MainTimelineItem_Model data = right_data_list.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("about_id", data.getAbout_id());
				bundle.putString("about_type", data.getAbout_type());
				if (data.getAbout_type().equals(M_Constant.FACE))
					AppTools.toIntent(getActivity(), bundle, Expression_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.NEWS))
					AppTools.toIntent(getActivity(), bundle, News_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.GAME))
					AppTools.toIntent(getActivity(), bundle, Game_Activity.class);
				else if (data.getAbout_type().equals(M_Constant.VIDEO))
					AppTools.toIntent(getActivity(), bundle, Video_Activity.class);
			}
		});

		banner_v = (VerticalViewPager) getActivity().findViewById(R.id.banner_v);
		banner_v.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				switch (arg0) {
				case 0:
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		banner_v.setAdapter(banner_adaper);

//		w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//		linearParams = (LinearLayout.LayoutParams) banner_v.getLayoutParams(); // 取控件textView当前的布局参数

//		ms.setSl(new ScrollViewListener() {
//			@Override
//			public void onScrollChanged(int x, int y, int oldx, int oldy) {
//				if (isfirst) {
//					handler.sendEmptyMessage(y);
//				} else {
//					isfirst = !isfirst;
//				}
//			}
//		});
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			sv.scrollTo(0, 0);
		}

	};

//	Handler handler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			int scrollY2 = msg.what;
//			int y2 = (int) (getResources().getDimension(R.dimen.banner1_height) - scrollY2 * 1);
//			if (y2 <= 0)
//				y2 = 0;
//
//			if (scrollY2 >= (int) getResources().getDimension(R.dimen.banner1_height))
//				y2 = 0;
//
//			// int ws2 = y2 * w / (int)
//			// getResources().getDimension(R.dimen.banner1_height);
//
//			linearParams.height = y2;// 控件的高强制设成20
//			linearParams.width = LayoutParams.MATCH_PARENT;// 控件的宽强制设成30
//
//			banner_v.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
//		}
//
//	};

	private void getData(String type, int i, boolean isMore) {
		rdl.open();
		new TimeLine_Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, isMore, true, type, i);
	}

	private class TimeLine_Task extends AsyncTask<Object, Void, MainTimeline_Model> {

		private boolean isRefresh;
		private boolean isMore;

		@Override
		protected MainTimeline_Model doInBackground(Object... params) {
			MainTimeline_Model data = null;
			isMore = (Boolean) params[0];
			isRefresh = (Boolean) params[1];
			try {
				data = application.getTimeLineData(isRefresh, isMore, params[2], params[3], pagesize + "");
			} catch (M_Exception e1) {
				e1.printStackTrace();
			}

//			try {
//				Thread.sleep(800);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

			return data;
		}

		@Override
		protected void onPostExecute(MainTimeline_Model result) {
			ms.onRefreshComplete();
			if (result != null) {
				data_list.clear();

				if (!isMore) {
					left_data_list.clear();
					right_data_list.clear();
					position = 1;
				}

				if (result.getTimeline().getList() != null && result.getTimeline().getList().size() != 0) {
					data_list.addAll(result.getTimeline().getList());

					for (int i = 0; i < result.getTimeline().getList().size(); i++) {
						if (i % 2 == 0)
							left_data_list.add(data_list.get(i));
						else
							right_data_list.add(data_list.get(i));
					}

					AppTools.setListView2Height(ls2, right_data_list.size());
					AppTools.setListView1Height(ls1, left_data_list.size());
					 adapter.notifyDataSetChanged();
					 adapter2.notifyDataSetChanged();
				}

				if (result.getAdvert() != null && result.getAdvert().getList() != null) {
					rdl.setData(result.getAdvert().getList());
					
					rdl.setData(result.getAdvert().getList());
					datalista.clear();
					datalista.addAll(result.getAdvert().getList());
					banner_adaper.notifyDataSetChanged();

					bannerHandler.removeCallbacks(banner);
					bannerHandler.post(banner);
				}

			}
			rdl.close();
		}
	}

	private void setinitData(MainTimeline_Model result) {
		if (result == null)
			return;

		if (result.getTimeline().getList() != null && result.getTimeline().getList().size() != 0) {
			data_list.clear();
			data_list.addAll(result.getTimeline().getList());
			left_data_list.clear();
			right_data_list.clear();

			for (int i = 0; i < result.getTimeline().getList().size(); i++) {
				if (i % 2 == 0)
					left_data_list.add(data_list.get(i));
				else
					right_data_list.add(data_list.get(i));
			}

			AppTools.setListView2Height(ls2, right_data_list.size());
			AppTools.setListView1Height(ls1, left_data_list.size());
			adapter.notifyDataSetChanged();
			adapter2.notifyDataSetChanged();
		}

		if (result.getAdvert() != null && result.getAdvert().getList() != null) {
			rdl.setData(result.getAdvert().getList());
			datalista.clear();
			datalista.addAll(result.getAdvert().getList());
			banner_adaper.notifyDataSetChanged();

			bannerHandler.removeCallbacks(banner);
			bannerHandler.post(banner);
		}
	}

	public interface ScrollToStarEndListener {
		public void toTop(State state, int y);
	}

	public interface RecommendDataListener {
		public void setData(ArrayList<Ad_Content_Model> datalist);

		public void open();

		public void close();
	}
}
