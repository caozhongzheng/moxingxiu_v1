package com.moinapp.wuliao.model;

import java.io.Serializable;

public class MainTimelineItem_Model extends Base_Model implements Serializable {

	private String about_type;
	private String about_id;
	private String addtime;
	private String hits_num;
	private String like_num;
	private String hate_num;
	private String share_num;
	private String download_num;
	private String reply_num;

	public String getAbout_type() {
		return about_type;
	}

	public void setAbout_type(String about_type) {
		this.about_type = about_type;
	}

	public String getAbout_id() {
		return about_id;
	}

	public void setAbout_id(String about_id) {
		this.about_id = about_id;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
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

	public String getShare_num() {
		return share_num;
	}

	public void setShare_num(String share_num) {
		this.share_num = share_num;
	}

	public String getDownload_num() {
		return download_num;
	}

	public void setDownload_num(String download_num) {
		this.download_num = download_num;
	}

	public String getReply_num() {
		return reply_num;
	}

	public void setReply_num(String reply_num) {
		this.reply_num = reply_num;
	}

}
