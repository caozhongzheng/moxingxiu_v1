package com.moinapp.wuliao;

import java.text.SimpleDateFormat;

import android.os.Environment;

public class M_Constant {
	
	public static String IMAGELOAD_CACHE = "MX/CACHE";
//	异常收集
	public static final String EXCEPTION_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MX/EXCEPTION/";
	
//	GIF缓存图地址
	public static final String CACHE_GIF = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MX/GIF/";
	
//	ZIP缓存图地址
	public static final String CACHE_ZIP = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MX/FACE_ZIP/";

	public static final String WEBSITE = "http://m.mo-image.com";
//	private static final String BASE_URL = "http://221.6.19.117:8088/?app=api2&act=";
	private static final String BASE_URL = "http://182.92.188.239:8088/?app=api2&act=";
	public static final String UID = "uid";
	public static final String RESULT = "result";
	public static final String MSG = "msg";
	public static final String TOTAL =  "total";
	
	public static final String ACTION_LOGIN = "login";
	public static final String TRADE = "trade";
	public static final String SEND_GIFT = "sendGift";
	public static final String RECEIVE_GIFT = "receiveGift";
	public static final String UPDATE_USER_INFO = "updateUserInfo";
	public static final String VIEW = "view";
	public static final String SIGNIN_MYSTAR = "signin";
	public static final String LIKE = "like";
	public static final String HATE = "hate";
	public static final String SHARE = "share";
	
	public static final String DEFAULT = "";
	public static final String FACE = "face";
	public static final String VIDEO = "video";
	public static final String NEWS = "news";
	public static final String GAME = "game";
	public static final String ABOUT_TYPE = "about_type";
	public static final String ABOUT_ID = "about_id";
	public static final String DATA = "data";
	public static final String UPDATE = "update";
	
	public static final String FRIEND = "friend";
	public static final String MESSAGE = "message";
	public static final String GIFT = "gift";
	
	public static final SimpleDateFormat sdf_custom = new SimpleDateFormat("HH:mm MM/dd");
	public static final SimpleDateFormat sdf_custom_face = new SimpleDateFormat("yyyy/MM/dd");
	public static final SimpleDateFormat sdf_standard = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat sdf_chat = new SimpleDateFormat("MM:dd HH:mm");
	
//	获取时间线数据
	public static final String TIME_LINE = BASE_URL + "getTimeline";
	
//	获取详细信息
	public static final String GET_DETAIL = BASE_URL + "getDetail";
	
//  请求添加好友
	public static final String ADD_FRIEND = BASE_URL + "addFriend";
	
//  同意添加好友
	public static final String AGREE_FRIEND = BASE_URL + "agreeFriend";
	
//  拒绝添加好友
	public static final String DISAGREE_FRIEND = BASE_URL + "disagreeFriend";
	
//  删除好友
	public static final String DEL_FRIEND = BASE_URL + "deleteFriend";
	
//  查询好友	
	public static final String GET_FRIENDS = BASE_URL + "getFriends";
	
//  添加评论
	public static final String ADD_REPLY = BASE_URL + "reply";
	
//  获取评论
	public static final String GET_REPLY = BASE_URL + "getReply";
	
//	顶，踩评论
	public static final String REPLY_SET = BASE_URL + "replySet";
	
//	喜爱/讨厌/分享   视频,表情,游戏,新闻
	public static final String LIKE_SET = BASE_URL + "likeSet";
	
//  删除评论
	public static final String DELETE_REPLY = BASE_URL + "deleteReply";
	
	
	
//	用户登录
	public static final String LOGIN = BASE_URL + "login";
	
//	用户注册
	public static final String USER_REGIST = BASE_URL + "register";
	
//	检测用户名是否存在
	public static final String CHECK_USERNAME = BASE_URL + "checkUsername";
	
//	检测手机号码是否存在
	public static final String CHECK_PHONE = BASE_URL + "checkPhone";
	
//	检测用户邮箱是否存在
	public static final String CHECK_EMAIL = BASE_URL + "checkEmail";
	
//	获取用户信息
	public static final String USER_INFORMATION = BASE_URL + "getUserInfo";
	
//	用户信息更新
	public static final String USER_UPDATE_USERINFO = BASE_URL + "updateUserInfo";
	
//	用户修改密码
	public static final String USER_CHANGE_PASSWORD = BASE_URL + "changePwd";
	
//  密码重置
	public static final String RESETPASSWORD = BASE_URL + "resetPassword";
	
//  发送邮件验证码
	public static final String SENDEMAILCAPTCHA = BASE_URL + "sendEmailCaptcha";
	
//  验证邮件验证码
	public static final String CHECKEMAILCAPTCHA = BASE_URL + "checkEmailCaptcha";
	
	
//	用户签到
	public static final String SIGNIN = BASE_URL + "signin";
	
//	获取用户签到记录
	public static final String USER_SIGNIN_LIST = BASE_URL + "getUserSigninList";
	
//	获取交易记录
	public static final String USER_TRADE_LIST = BASE_URL + "getUserTradeList";
	
//	我的星迹
	public static final String MYSTAR = BASE_URL + "getUserTracks";
	
//	赠送礼物
	public static final String SEND_GIFT_URL = BASE_URL + "sendGift";
	
//	获取用户未打开礼包
	public static final String USER_UNOPENED_GIFT = BASE_URL + "getUserUnOpenedGift";
	
//  获取用户发送礼包列表
	public static final String USER_SENDGIFT_LIST = BASE_URL + "getUserSendGiftList";
	
//  获取用户接收礼包列表
	public static final String USER_RECEIVEGIFT_LIST = BASE_URL + "getUserReceiveGiftList";
	
//	打开礼包
	public static final String OPEN_GIFT = BASE_URL + "openGift";
	
//	心跳
	public static final String HEARD_BEAT = BASE_URL + "heartbeat";
	
//  检车更新
	public static final String CHECK_UPDATE = BASE_URL + "checkUpdate";
	
//	获取我的评论
	public static final String MY_COMMENTS = BASE_URL + "getUserReplyList";
	
//	获取我的喜欢列表
	public static final String MY_LIKES = BASE_URL + "getUserLikeList";
	
	
//  发送私信	
	public static final String SEND_MESSAGE = BASE_URL + "sendMessage";
	
//  获取用户未读消息
	public static final String USER_UNREAD_MESSAGE = BASE_URL + "getUserUnreadMessage";	
	
//  获取用户特定好友私信列表
	public static final String USER_FRIEND_MESSAGE = BASE_URL + "getUserFriendMessage";
	
//	获取用户发送私信列表
	public static final String USER_SEND_HISTORY_MESSAGE = BASE_URL + "getUserSendHistoryMessage";
	
//	获取用户接收私信列表
	public static final String USER_RECEIVE_HISTORY_MESSAGE = BASE_URL + "getUserReceiveHistoryMessage";	
	
//  获取最新事件
	public static final String GET_RECENTEVENT = BASE_URL + "getRecentEvent";
	
//	意见反馈
	public static final String FEEDBACK = BASE_URL + "feedback";
	
//	版本检测
	public static final String VERSION = BASE_URL + "version";
	
//	获取用户未读信息列表
	public static final String USERUNREAD_MESSAGE = BASE_URL + "getUserUnreadMessage";
	
//	判断token是否存在
	public static final String CHECK_TOKEN = BASE_URL + "checkToken";
	
//  获取最近搜索热词
	public static final String GETHOTWORDS = BASE_URL + "getHotWords";
	
}
