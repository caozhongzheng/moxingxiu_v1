package com.moinapp.wuliao;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpException;
import org.apache.http.conn.HttpHostConnectException;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moinapp.wuliao.bean.DataDetail_Bean;
import com.moinapp.wuliao.common.DataManager;
import com.moinapp.wuliao.model.FaceDetail_Model;
import com.moinapp.wuliao.model.Friend_Model;
import com.moinapp.wuliao.model.GameDetail_Model;
import com.moinapp.wuliao.model.Heartbeat_Model;
import com.moinapp.wuliao.model.HistoryMessage_Model;
import com.moinapp.wuliao.model.Login_Model;
import com.moinapp.wuliao.model.MainTimeline_Model;
import com.moinapp.wuliao.model.ModifyUserInformation_Model;
import com.moinapp.wuliao.model.MyComments_Model;
import com.moinapp.wuliao.model.MyStar_Model;
import com.moinapp.wuliao.model.MyTrade_Model;
import com.moinapp.wuliao.model.NewMessageEvent_Model;
import com.moinapp.wuliao.model.NewsDetail_Model;
import com.moinapp.wuliao.model.OpenGift_Model;
import com.moinapp.wuliao.model.RecentEvent_Model;
import com.moinapp.wuliao.model.Reply_Model;
import com.moinapp.wuliao.model.SearchHotWords_Model;
import com.moinapp.wuliao.model.Signin_Model;
import com.moinapp.wuliao.model.Signin_User_Model;
import com.moinapp.wuliao.model.UserInformation_Model;
import com.moinapp.wuliao.model.Version_Model;
import com.moinapp.wuliao.model.VideoDetail_Model;
import com.moinapp.wuliao.util.AppTools;
import com.moinapp.wuliao.util.CHexConver;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.thinkland.sdk.util.CommonFun;
import com.zyh.util.JsonUtil;
import com.zyh.volleybox.FileUtil;
import com.zyh.volleybox.VolleyBoxApplication;

public class M_Application extends VolleyBoxApplication {

	private HashMap<String, Object> catchModel = new HashMap<String, Object>();
	
	public Object getCatchModelForKey(String key) {
		return catchModel.get(key);
	}

	public void addCatchModel(String key, Object value) {
		catchModel.put(key, value);
	}
	
	public void clearCatchModel() {
		catchModel.clear();
	}

	public String uid = "0";
	public int unOpenGiftNum, unOpenMessageNum;
	public Signin_User_Model login_model;
	private String qq_token = "";
	private String weibo_token = "";
	private String weixin_token = "";
	
	public String getQq_token() {
		return qq_token;
	}

	public void setQq_token(String qq_token) {
		this.qq_token = qq_token;
	}

	public String getWeibo_token() {
		return weibo_token;
	}

	public void setWeibo_token(String weibo_token) {
		this.weibo_token = weibo_token;
	}

	public String getWeixin_token() {
		return weixin_token;
	}

	public void setWeixin_token(String weixin_token) {
		this.weixin_token = weixin_token;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getUnOpenGiftNum() {
		return unOpenGiftNum;
	}

	public void setUnOpenGiftNum(int unOpenGiftNum) {
		this.unOpenGiftNum = unOpenGiftNum;
	}

	public int getUnOpenMessageNum() {
		return unOpenMessageNum;
	}

	public void setUnOpenMessageNum(int unOpenMessageNum) {
		this.unOpenMessageNum = unOpenMessageNum;
	}

	public Signin_User_Model getLogin_model() {
		return login_model;
	}

	public void setLogin_model(Signin_User_Model login_model) {
		this.login_model = login_model;
	}

	public void login_out() {
		this.uid = "0";
		this.login_model = null;
		AppTools.clearUsernameAndPassword(this);
	}
	
	public boolean hasUpdate = false; 
	public boolean isHasUpdate() {
		return hasUpdate;
	}

	public void setHasUpdate(boolean hasUpdate) {
		this.hasUpdate = hasUpdate;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
//				ToastUtil.makeText(getApplicationContext(), R.string.network_error, Toast.LENGTH_SHORT);
				break;
			case -2:
//				ToastUtil.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT);
				break;
			}
		}
	};

	@Override
	protected void exceptionProcessor(Exception e) {
		if (e instanceof NetworkError) {
		} else if (e instanceof ParseError) {
		} else if (e instanceof HttpException) {
			handler.sendEmptyMessage(-1);
		} else if (e instanceof HttpHostConnectException) {
			handler.sendEmptyMessage(-1);
		} else if (e instanceof M_Exception) {
			handler.sendEmptyMessage(-1);
		} else {
			Message msg = Message.obtain();
			msg.what = -2;
			msg.obj = e.getMessage();
			handler.sendMessage(msg);
		}
	}

	@Override
	protected void iniData() {

		ShareSDK.initSDK(this);
		CommonFun.initialize(getApplicationContext(), true);
//		CrashHandler.getInstance().init(this);
		initImageLoader(this);
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, M_Constant.IMAGELOAD_CACHE);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCache(new UnlimitedDiscCache(cacheDir))
				// .diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
																					// for
																					// release
																					// app
				.memoryCache(new WeakMemoryCache())
				.threadPoolSize(3)
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 获取设备uuid
	 * 
	 * @return
	 */
	public String getUuid() {
		final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = tm.getDeviceId() + "";
		tmSerial = tm.getSimSerialNumber() + "";
		androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID) + "";
		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}
	
	public void initData() {
		String[] catch_list = {M_Constant.DEFAULT, M_Constant.FACE, M_Constant.NEWS};
		for (String string : catch_list) {
			if(FileUtil.isExistDataFile("TIME_LINE" + string, this))
				addCatchModel(string, readData("TIME_LINE" + string));
		}
	}

	/**
	 * 获取时间线数据
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public MainTimeline_Model getTimeLineData(final boolean isRefresh, final boolean isMore, Object... params) throws M_Exception {
		String key = "TIME_LINE" + (String) params[0];
		final String url = M_Constant.TIME_LINE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.ABOUT_TYPE, (String) params[0]);
		hashmap.put("page", "" + params[1]);
		hashmap.put("pagesize", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));
		// System.out.println(hexStr);
		// System.out.println(MD5EncodeUtil.MD5Encode(hexStr.getBytes()));
		// System.out.println(hexStr +
		// MD5EncodeUtil.MD5Encode(hexStr.getBytes()));

		// HashMap<String, String> postmap = new HashMap<String, String>();
		// postmap.put("data", hexStr +
		// MD5EncodeUtil.MD5Encode(hexStr.getBytes()));

		// return DataManager.getVideoList_HomeData(hexStr).getData();

		// return postDataObject(new TypeToken<MainTimeline_Model>() {
		// }.getType(), key, isRefresh, url, postmap);
		// return null;

		MainTimeline_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<MainTimeline_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MainTimeline_Model>() {
				}.getType());
				data = data_bean.getData();
				if (!isMore)
					writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				if (!isMore)
					data = readData(key);
				e.printStackTrace();
			}
		} else {
			if (!isMore)
				data = readData(key);
		}
		return data;
	}

	/**
	 * 获取最近搜索热词
	 * 
	 * @param content
	 * @return
	 * @throws M_Exception
	 */
	public SearchHotWords_Model getHotWords(String uid, String count) throws M_Exception {
		final String url = M_Constant.GETHOTWORDS;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("count", count);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		SearchHotWords_Model data = null;
		try {
			DataDetail_Bean<SearchHotWords_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<SearchHotWords_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 获取搜索数据
	 * 
	 * @param content
	 * @return
	 * @throws M_Exception
	 */
	public MainTimeline_Model getSearchData(String content) throws M_Exception {
		final String url = M_Constant.TIME_LINE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.ABOUT_TYPE, "");
		hashmap.put("page", "");
		hashmap.put("pagesize", "");
		hashmap.put("keyword", content);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		MainTimeline_Model data = null;
		try {
			DataDetail_Bean<MainTimeline_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MainTimeline_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取视频页详细数据
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public VideoDetail_Model getVideoData(final boolean isRefresh, Object... params) throws M_Exception {
		String key = "DETAIL_" + params[0] + "_" + params[1];
		final String url = M_Constant.GET_DETAIL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.UID, "0");
		hashmap.put(M_Constant.ABOUT_TYPE, (String) params[0]);
		hashmap.put(M_Constant.ABOUT_ID, "" + params[1]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		VideoDetail_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<VideoDetail_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<VideoDetail_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取表情页详细数据
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public FaceDetail_Model getExpressionData(final boolean isRefresh, Object... params) throws M_Exception {
		String key = "DETAIL_" + params[0] + "_" + params[1];
		final String url = M_Constant.GET_DETAIL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.UID, "0");
		hashmap.put(M_Constant.ABOUT_TYPE, (String) params[0]);
		hashmap.put(M_Constant.ABOUT_ID, "" + params[1]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		FaceDetail_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<FaceDetail_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<FaceDetail_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取游戏页详细数据
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public GameDetail_Model getGameData(final boolean isRefresh, Object... params) throws M_Exception {
		String key = "DETAIL_" + params[0] + "_" + params[1];
		final String url = M_Constant.GET_DETAIL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.UID, "0");
		hashmap.put(M_Constant.ABOUT_TYPE, (String) params[0]);
		hashmap.put(M_Constant.ABOUT_ID, "" + params[1]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		GameDetail_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<GameDetail_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<GameDetail_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取新闻页详细数据
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public NewsDetail_Model getNewsDetailData(final boolean isRefresh, Object... params) throws M_Exception {
		String key = "DETAIL_" + params[0] + "_" + params[1];
		final String url = M_Constant.GET_DETAIL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put(M_Constant.UID, "0");
		hashmap.put(M_Constant.ABOUT_TYPE, (String) params[0]);
		hashmap.put(M_Constant.ABOUT_ID, "" + params[1]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		NewsDetail_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<NewsDetail_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<NewsDetail_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 添加评论
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map addReply(Object... params) throws M_Exception {
		final String url = M_Constant.ADD_REPLY;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("about_type", (String) params[1]);
		hashmap.put("about_id", (String) params[2]);
		hashmap.put("content", (String) params[3]);
		hashmap.put("reply_to_uid", (String) params[4]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));
		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 获取评论
	 * 
	 * @param isRefresh
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Reply_Model getReply(final boolean isRefresh, Object... params) throws M_Exception {
		String key = "REPLY_" + params[0] + "_" + params[1] + "_" + params[2];
		final String url = M_Constant.GET_REPLY;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("about_type", (String) params[1]);
		hashmap.put("about_id", (String) params[2]);
		hashmap.put("page", (String) params[3]);
		hashmap.put("pagesize", (String) params[4]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Reply_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<Reply_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Reply_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 请求添加好友
	 * 
	 * @param uid
	 * @param fid
	 * @param message
	 * @throws M_Exception
	 */
	public Map addFriend(String uid, String fid, String message) throws M_Exception {
		String url = M_Constant.ADD_FRIEND;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", fid);
		hashmap.put("message", message);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 同意添加好友
	 * 
	 * @param uid
	 * @param fid
	 * @throws M_Exception
	 */
	public Map agreeFriend(String uid, String fid) {
		String url = M_Constant.AGREE_FRIEND;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", fid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 拒绝添加好友
	 * 
	 * @param uid
	 * @param fid
	 * @throws M_Exception
	 */
	public Map disagreeFriend(String uid, String fid) {
		String url = M_Constant.DISAGREE_FRIEND;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", fid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 删除好友
	 * 
	 * @param uid
	 * @param fid
	 * @throws M_Exception
	 */
	public Map deleteFriend(String uid, String fid) {
		String url = M_Constant.DEL_FRIEND;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", fid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 查询好友
	 * 
	 * @param uid
	 * @return
	 */
	public Friend_Model getFriends(boolean isRefresh, String uid) {
		String key = "FRIEND_" + uid;
		String url = M_Constant.GET_FRIENDS;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Friend_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<Friend_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Friend_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 发送私信
	 * 
	 * @param uid
	 * @param fid
	 * @param message
	 * @throws M_Exception
	 */
	public Map sendMessage(String uid, String fid, String message) {
		String url = M_Constant.SEND_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", fid);
		hashmap.put("message", message);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}
	
	/**
	 * 获取用户未读消息
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public NewMessageEvent_Model getUserUnreadMessage(String uid) {
		String url = M_Constant.USER_UNREAD_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		NewMessageEvent_Model data = null;
		try {
			DataDetail_Bean<NewMessageEvent_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<NewMessageEvent_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取用户特定好友私信列表
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public HistoryMessage_Model getUserFriendMessage(final boolean isRefresh, Object... params) {
		String key = "USERFRIENDMESSAGE_" + params[0] + "_to_" + params[1];
		String url = M_Constant.USER_FRIEND_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("fid", (String) params[1]);
		hashmap.put("page", (String) params[2]);
		hashmap.put("pagesize", (String) params[3]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		HistoryMessage_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<HistoryMessage_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<HistoryMessage_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取用户发送私信列表
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public HistoryMessage_Model getUserSendHistoryMessage(final boolean isRefresh, Object... params) {
		String key = "USERSENDHISTORYMESSAGE_" + uid + "_to " + (String) params[1];
		String url = M_Constant.USER_SEND_HISTORY_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("fid", (String) params[1]);
		hashmap.put("page", (String) params[2]);
		hashmap.put("pagesize", (String) params[3]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		HistoryMessage_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<HistoryMessage_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<HistoryMessage_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	public int getUserMessageNum(String uid) {
		return getUserSendHistoryMessage(uid) + getUserReceiveHistoryMessage(uid);
	}

	public int getGiftNum(String uid) {
		return getUserSendGiftList(uid) + getUserReceiveGiftList(uid);
	}

	/**
	 * 获取用户发出的私信数量
	 * 
	 * @param uid
	 * @return
	 */
	public int getUserSendHistoryMessage(String uid) {
		String url = M_Constant.USER_SEND_HISTORY_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", "");
		hashmap.put("message", "");

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = Integer.parseInt(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}
		return num;
	}

	/**
	 * 获取用户接收私信列表
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public HistoryMessage_Model getUserReceiveHistoryMessage(final boolean isRefresh, Object... params) {
		String key = "USERRECEIVEHISTORYMESSAGE_" + uid + "_to " + (String) params[1];
		String url = M_Constant.USER_RECEIVE_HISTORY_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("fid", (String) params[1]);
		hashmap.put("page", (String) params[2]);
		hashmap.put("pagesize", (String) params[3]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		HistoryMessage_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<HistoryMessage_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<HistoryMessage_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取用户接收私信列表数量
	 * 
	 * @param uid
	 * @return
	 */
	public int getUserReceiveHistoryMessage(String uid) {
		String url = M_Constant.USER_RECEIVE_HISTORY_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", "");
		hashmap.put("message", "");

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = Integer.parseInt(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}
		return num;
	}

	/**
	 * 检测用户名是否存在
	 * 
	 * @param username
	 * @return
	 * @throws M_Exception
	 */
	public Map checkUsername(String username) throws M_Exception {
		String url = M_Constant.CHECK_USERNAME;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("username ", username);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 检测手机号码是否存在
	 * 
	 * @param phone
	 * @return
	 * @throws M_Exception
	 */
	public Map checkPhone(String phone) throws M_Exception {
		String url = M_Constant.CHECK_PHONE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("phone", phone);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 检测邮箱是否存在
	 * 
	 * @param email
	 * @return
	 * @throws M_Exception
	 */
	public Map checkEmail(String email) throws M_Exception {
		String url = M_Constant.CHECK_EMAIL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("email", email);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param isRefresh
	 * @param uid
	 * @return
	 */
	public UserInformation_Model queryUserInformation(final boolean isRefresh, String uid) {
		String key = "USER_INFORMATION" + uid;
		String url = M_Constant.USER_INFORMATION;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		UserInformation_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<UserInformation_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<UserInformation_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	public Login_Model readLoginUserInformation(String username) {
		String key = "USER_LOGIN" + "_" + username;
		return readData(key);
	}

	public void writeLoginUserInformation(String username, Signin_Model data) throws M_Exception {
		String key = "USER_LOGIN" + "_" + username;
		writeData(key, data);
	}

	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws M_Exception
	 */
	public Login_Model user_Login(String username, String password) throws M_Exception {
		String key = "USER_LOGIN" + "_" + username;

		String url = M_Constant.LOGIN;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("username", username);
		hashmap.put("password", password);
		hashmap.put("qq_token", qq_token);
		hashmap.put("weibo_token", weibo_token);
		hashmap.put("weixin_token", weibo_token);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Login_Model data = null;
		try {
			DataDetail_Bean<Login_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Login_Model>() {
			}.getType());
			data = data_bean.getData();
			writeData(key, data);
		} catch (Exception e) {
			exceptionProcessor(e);
			// data = readData(key);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 用户注册
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map user_Regist(Object... params) throws M_Exception {
		final String url = M_Constant.USER_REGIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("phone", (String) params[0]);
		hashmap.put("email", (String) params[1]);
		hashmap.put("username", (String) params[2]);
		hashmap.put("password", (String) params[3]);
		hashmap.put("deviceid", (String) params[4]);
		hashmap.put("nickname", (String) params[5]);
		hashmap.put("sex", (String) params[6]);
		hashmap.put("lng", (String) params[7]);
		hashmap.put("lat", (String) params[8]);
		hashmap.put("qq_token", qq_token);
		hashmap.put("weibo_token", weibo_token);
		hashmap.put("weixin_token", weibo_token);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 用户信息更新
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public ModifyUserInformation_Model user_UpdateUserInfo(Object... params) {
		String key = "USER_LOGIN" + "_" + params[9];
		final String url = M_Constant.USER_UPDATE_USERINFO;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("nickname", (String) params[1]);
		hashmap.put("sex", (String) params[2]);
		hashmap.put("birthday", (String) params[3]);
		hashmap.put("likestar", (String) params[4]);
		hashmap.put("weixin", (String) params[5]);
		hashmap.put("deviceid", (String) params[6]);
		hashmap.put("lng", (String) params[7]);
		hashmap.put("lat", (String) params[8]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		ModifyUserInformation_Model data = null;
		DataDetail_Bean<ModifyUserInformation_Model> data_bean;
		try {
			data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<ModifyUserInformation_Model>() {
			}.getType());
			data = data_bean.getData();
			writeData(key, data);
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 用戶修改密碼
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map user_ChangePassword(Object... params) throws M_Exception {
		final String url = M_Constant.USER_CHANGE_PASSWORD;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("username", (String) params[0]);
		hashmap.put("oldpass", (String) params[1]);
		hashmap.put("newpass", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}
	
	/**
	 * 手机重置密碼
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map resetPassword(String phone, String email) {
		final String url = M_Constant.RESETPASSWORD;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("phone", phone);
		hashmap.put("email", email);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}
	
	/**
	 * 发送邮箱验证码
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map sendEmailCaptcha(String email) {
		final String url = M_Constant.SENDEMAILCAPTCHA;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("email", email);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}
	
	/**
	 * 验证邮件验证码
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map checkEmailCaptcha(String email, String captcha) {
		final String url = M_Constant.CHECKEMAILCAPTCHA;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("email", email);
		hashmap.put("captcha", captcha);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 顶，踩评论
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map replySet(Object... params) throws M_Exception {
		final String url = M_Constant.REPLY_SET;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("rid", (String) params[1]);
		hashmap.put("set", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}
	
	/**
	 * 删除评论
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map deleteReply(Object... params) throws M_Exception {
		final String url = M_Constant.DELETE_REPLY;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("rid", (String) params[1]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 喜爱/讨厌/分享 视频,表情,游戏,新闻
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map listSet(Object... params) throws M_Exception {
		final String url = M_Constant.LIKE_SET;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("about_type", (String) params[1]);
		hashmap.put("about_id", (String) params[2]);
		hashmap.put("set", (String) params[3]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 用户签到
	 * 
	 * @param isRefresh
	 * @param uid
	 * @return
	 * @throws M_Exception
	 */
	public Login_Model user_Signin(String uid, String username) {
		String key = "USER_LOGIN" + "_" + username;
		String url = M_Constant.SIGNIN;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Login_Model data = null;
		try {
			DataDetail_Bean<Login_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Login_Model>() {
			}.getType());
			data = data_bean.getData();
			if (data != null && data.getData() != null) {
				if(data.getResult().equals("1"))
				writeData(key, data);
			}
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取我的星迹信息
	 * 
	 * @param isRefresh
	 * @param uid
	 * @return
	 */
	public MyStar_Model getUserMyStar(final boolean isRefresh, String uid, String action_type, String page, String pagesize) {
		String key = "USER_MYSTAR" + action_type + uid;
		String url = M_Constant.MYSTAR;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("action_type", action_type);
		hashmap.put("page", page);
		hashmap.put("pagesize", pagesize);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		MyStar_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<MyStar_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MyStar_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取我的星迹信息
	 * 
	 * @param isRefresh
	 * @param uid
	 * @return
	 */
	public MyTrade_Model getUserMyTrade(final boolean isRefresh, String uid, String page, String pagesize) {
		String key = "USER_MYTRADE" + uid;
		String url = M_Constant.USER_TRADE_LIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("page", page);
		hashmap.put("pagesize", pagesize);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		MyTrade_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<MyTrade_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MyTrade_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 赠送礼物
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map sendGift(Object... params) {
		final String url = M_Constant.SEND_GIFT_URL;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("fid", (String) params[1]);
		hashmap.put("about_type", (String) params[2]);
		hashmap.put("about_value", (String) params[3]);
		hashmap.put("message", (String) params[4]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 获取未打开礼包列表
	 * 
	 * @param uid
	 * @return
	 */
	public OpenGift_Model getUnOpenGift(String uid) {
		// String key = "USER_INFORMATION" + uid;
		String url = M_Constant.USER_UNOPENED_GIFT;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		OpenGift_Model data = null;
		try {
			DataDetail_Bean<OpenGift_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<OpenGift_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取未打开的礼包数量
	 * 
	 * @param uid
	 * @return
	 */
	public int getUnOpenGiftNum(String uid) {
		String url = M_Constant.USER_UNOPENED_GIFT;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = (int) Double.parseDouble(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}
		return num;
	}

	/**
	 * 获取用户发送礼包列表
	 * 
	 * @param uid
	 * @return
	 */
	public OpenGift_Model getUserSendGiftList(Object... params) {
		String url = M_Constant.USER_SENDGIFT_LIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("page", (String) params[1]);
		hashmap.put("pagesize", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		OpenGift_Model data = null;
		try {
			DataDetail_Bean<OpenGift_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<OpenGift_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	public int getUserSendGiftList(String uid) {
		String url = M_Constant.USER_SENDGIFT_LIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("fid", "");
		hashmap.put("message", "");

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = Integer.parseInt(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}
		return num;
	}

	/**
	 * 获取用户接收礼包列表
	 * 
	 * @param uid
	 * @return
	 */
	public OpenGift_Model getUserReceiveGiftList(Object... params) {
		String url = M_Constant.USER_RECEIVEGIFT_LIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("page", (String) params[1]);
		hashmap.put("pagesize", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		OpenGift_Model data = null;
		try {
			DataDetail_Bean<OpenGift_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<OpenGift_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	public int getUserReceiveGiftList(String uid) {
		String url = M_Constant.USER_RECEIVEGIFT_LIST;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("page", "");
		hashmap.put("pagesize", "");

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = Integer.parseInt(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}
		return num;
	}

	/**
	 * 打开礼包
	 * 
	 * @param params
	 * @return
	 * @throws M_Exception
	 */
	public Map openGift(Object... params) {
		final String url = M_Constant.OPEN_GIFT;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("gifttype", (String) params[1]);
		hashmap.put("gid", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 心跳
	 * @param uid
	 * @return
	 */
	public Heartbeat_Model heartBeat(String uid) {
		// String key = "USER_INFORMATION" + uid;
		String url = M_Constant.HEARD_BEAT;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Heartbeat_Model data = null;
		try {
			DataDetail_Bean<Heartbeat_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Heartbeat_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 获取我的评论列表
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public MyComments_Model getMyComments(final boolean isRefresh, String uid, String page, String pagesize) {
		String key = "MY_COMMENTS_" + uid;
		String url = M_Constant.MY_COMMENTS;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("page", page);
		hashmap.put("pagesize", pagesize);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		MyComments_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<MyComments_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MyComments_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取我的喜欢列表
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public MyStar_Model getMyLikes(final boolean isRefresh, String uid, String page, String pagesize) {
		String key = "MY_LIKES_" + uid;
		String url = M_Constant.MY_LIKES;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("page", page);
		hashmap.put("pagesize", pagesize);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		MyStar_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<MyStar_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<MyStar_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	/**
	 * 获取最新事件
	 * 
	 * @param isRefresh
	 * @param uid
	 * @param page
	 * @param pagesize
	 * @return
	 */
	public RecentEvent_Model getRecentEvent(final boolean isRefresh, String uid, int time) {
		String key = "GET_RECENTEVENT_" + uid;
		String url = M_Constant.GET_RECENTEVENT;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);
		hashmap.put("time", time + "");

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		RecentEvent_Model data = null;
		if (isRefresh || !FileUtil.isExistDataFile(key, this)) {
			try {
				DataDetail_Bean<RecentEvent_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<RecentEvent_Model>() {
				}.getType());
				data = data_bean.getData();
				writeData(key, data);
			} catch (Exception e) {
				exceptionProcessor(e);
				data = readData(key);
				e.printStackTrace();
			}
		} else {
			data = readData(key);
		}
		return data;
	}

	public int getRecentEventNum(String uid) {
		String url = M_Constant.USERUNREAD_MESSAGE;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map1 = null;
		int num = 0;
		try {
			map1 = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));

			num = (int) Double.parseDouble(map1.get(M_Constant.TOTAL).toString());
		} catch (Exception e) {
			exceptionProcessor(e);
			num = -1;
		}

		return num;
	}

	/**
	 * 意见反馈
	 * 
	 * @param params
	 * @return
	 */
	public Map sendFeedBack(Object... params) {
		final String url = M_Constant.FEEDBACK;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("uid", (String) params[0]);
		hashmap.put("phone", (String) params[1]);
		hashmap.put("content", (String) params[2]);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Map map = null;
		try {
			map = JsonUtil.DataToMap(DataManager.getDetailString_Data(url, hexStr));
		} catch (Exception e) {
			exceptionProcessor(e);
		}
		return map;
	}

	/**
	 * 获取最新版本
	 * 
	 * @return
	 */
	public Version_Model getVersion() {
		String url = M_Constant.VERSION;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		// hashmap.put("uid", uid);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Version_Model data = null;
		try {
			DataDetail_Bean<Version_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Version_Model>() {
			}.getType());
			data = data_bean.getData();
		} catch (M_Exception e) {
			exceptionProcessor(e);
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 用户登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws M_Exception
	 */
	public Login_Model checkToken(String user_id) throws M_Exception {
		String key = "USER_LOGIN" + "_" + user_id;

		String url = M_Constant.CHECK_TOKEN;
		HashMap<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("qq_token", qq_token);
		hashmap.put("weibo_token", weibo_token);
		hashmap.put("weixin_token", weibo_token);

		Gson gson = new Gson();
		String hexStr = CHexConver.str2HexStr(gson.toJson(hashmap));

		Login_Model data = null;
		try {
			DataDetail_Bean<Login_Model> data_bean = DataManager.getDetail_Data(url, hexStr, new TypeToken<Login_Model>() {
			}.getType());
			data = data_bean.getData();
			writeData(key, data);
		} catch (Exception e) {
			exceptionProcessor(e);
			// data = readData(key);
			e.printStackTrace();
		}
		return data;
	}
}
