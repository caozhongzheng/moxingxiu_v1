package com.moinapp.wuliao.model;

import java.io.Serializable;

public class OpenGift_List_Model implements Serializable {

	private String id;
	private String from;
	private String to;
	private String about_type;
	private String about_id;
	private String about_value;
	private String message;
	private String status;
	private String addtime;
	private String nickname;
	private String sex;
	private String avatar;
	private String gifttype;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

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

	public String getAbout_value() {
		return about_value;
	}

	public void setAbout_value(String about_value) {
		this.about_value = about_value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGifttype() {
		return gifttype;
	}

	public void setGifttype(String gifttype) {
		this.gifttype = gifttype;
	}

}
