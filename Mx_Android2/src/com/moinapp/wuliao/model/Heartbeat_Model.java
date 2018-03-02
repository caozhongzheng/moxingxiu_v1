package com.moinapp.wuliao.model;

public class Heartbeat_Model extends UserInformation_Model {

	private String like_num;
	private String hate_num;
	private String reply_num;
	private String download_num;

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

	public String getDownload_num() {
		return download_num;
	}

	public void setDownload_num(String download_num) {
		this.download_num = download_num;
	}

}
