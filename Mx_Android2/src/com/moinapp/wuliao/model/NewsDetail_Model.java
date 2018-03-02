package com.moinapp.wuliao.model;

import java.io.Serializable;

public class NewsDetail_Model implements Serializable {

	private NewsDetail_Content_Model detail;
	private Reply_Model reply;
	private RecentUser_Model recentUser;
	private Recommend_Model recommend;

	public NewsDetail_Content_Model getDetail() {
		return detail;
	}

	public void setDetail(NewsDetail_Content_Model detail) {
		this.detail = detail;
	}

	public Reply_Model getReply() {
		return reply;
	}

	public void setReply(Reply_Model reply) {
		this.reply = reply;
	}

	public RecentUser_Model getRecentUser() {
		return recentUser;
	}

	public void setRecentUser(RecentUser_Model recentUser) {
		this.recentUser = recentUser;
	}

	public Recommend_Model getRecommend() {
		return recommend;
	}

	public void setRecommend(Recommend_Model recommend) {
		this.recommend = recommend;
	}

}
