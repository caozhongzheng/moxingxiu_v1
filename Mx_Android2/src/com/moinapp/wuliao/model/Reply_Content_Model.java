package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Reply_Content_Model implements Serializable {

	private String id;
	private String uid;
	private String about_type;
	private String about_id;
	private String content;
	private String reply_to_uid;
	private String good_num;
	private String bad_num;
	private String addtime;
	private String nickname;
	private String sex;
	private String avatar;
	private String isFriend;
	private String action;
	private String reply_to_nickname;
	private String reply_to_sex;
	private String reply_to_avatar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReply_to_uid() {
		return reply_to_uid;
	}

	public void setReply_to_uid(String reply_to_uid) {
		this.reply_to_uid = reply_to_uid;
	}

	public String getGood_num() {
		return good_num;
	}

	public void setGood_num(String good_num) {
		this.good_num = good_num;
	}

	public String getBad_num() {
		return bad_num;
	}

	public void setBad_num(String bad_num) {
		this.bad_num = bad_num;
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
	
	public String getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getReply_to_nickname() {
		return reply_to_nickname;
	}

	public void setReply_to_nickname(String reply_to_nickname) {
		this.reply_to_nickname = reply_to_nickname;
	}

	public String getReply_to_sex() {
		return reply_to_sex;
	}

	public void setReply_to_sex(String reply_to_sex) {
		this.reply_to_sex = reply_to_sex;
	}

	public String getReply_to_avatar() {
		return reply_to_avatar;
	}

	public void setReply_to_avatar(String reply_to_avatar) {
		this.reply_to_avatar = reply_to_avatar;
	}

}
