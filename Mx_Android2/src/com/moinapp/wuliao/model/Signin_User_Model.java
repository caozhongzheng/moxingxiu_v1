package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Signin_User_Model extends Base_Information_Model implements Serializable {

	private String login_time;
	private String signin_time;
	private String is_signin;
	private String create_time;
	private String login_num;
	private String signin_day;
	private String like_num;
	private String hate_num;
	private String reply_num;
	private String share_num;
	private String download_num;

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		this.login_time = login_time;
	}

	public String getSignin_time() {
		return signin_time;
	}

	public void setSignin_time(String signin_time) {
		this.signin_time = signin_time;
	}

	public String getLogin_num() {
		return login_num;
	}

	public void setLogin_num(String login_num) {
		this.login_num = login_num;
	}

	public String getSignin_day() {
		return signin_day;
	}

	public void setSignin_day(String signin_day) {
		this.signin_day = signin_day;
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

	public String getIs_signin() {
		return is_signin;
	}

	public void setIs_signin(String is_signin) {
		this.is_signin = is_signin;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
