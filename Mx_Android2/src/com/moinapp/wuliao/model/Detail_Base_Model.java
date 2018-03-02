package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Detail_Base_Model extends Base_Model implements Serializable {

	private String hits_num;
	private String like_num;
	private String hate_num;
	private String reply_num;
	private String likeSet;
	private String share_url;

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String getHits_num() {
		return hits_num;
	}

	public void setHits_num(String hits_num) {
		this.hits_num = hits_num;
	}

	public String getLike_num() {
		return like_num;
	}

	public void setLike_num(String like_num) {
		this.like_num = like_num;
	}

	public String getHate_num() {
		return hate_num;
	}

	public void setHate_num(String hate_num) {
		this.hate_num = hate_num;
	}

	public String getReply_num() {
		return reply_num;
	}

	public void setReply_num(String reply_num) {
		this.reply_num = reply_num;
	}

	public String getLikeSet() {
		return likeSet;
	}

	public void setLikeSet(String likeSet) {
		this.likeSet = likeSet;
	}

}
