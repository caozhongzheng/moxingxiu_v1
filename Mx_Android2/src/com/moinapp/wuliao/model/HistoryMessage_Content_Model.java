package com.moinapp.wuliao.model;

import java.io.Serializable;

public class HistoryMessage_Content_Model implements Serializable {
	private String id;
	private String from;
	private String to;
	private String message;
	private String status;
	private String addtime;
	private String from_nickname;
	private String from_sex;
	private String from_avatar;
	private String from_addr;
	private String to_nickname;
	private String to_sex;
	private String to_avatar;
	private String to_addr;
	
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
	public String getFrom_nickname() {
		return from_nickname;
	}
	public void setFrom_nickname(String from_nickname) {
		this.from_nickname = from_nickname;
	}
	public String getFrom_sex() {
		return from_sex;
	}
	public void setFrom_sex(String from_sex) {
		this.from_sex = from_sex;
	}
	public String getFrom_avatar() {
		return from_avatar;
	}
	public void setFrom_avatar(String from_avatar) {
		this.from_avatar = from_avatar;
	}
	public String getFrom_addr() {
		return from_addr;
	}
	public void setFrom_addr(String from_addr) {
		this.from_addr = from_addr;
	}
	public String getTo_nickname() {
		return to_nickname;
	}
	public void setTo_nickname(String to_nickname) {
		this.to_nickname = to_nickname;
	}
	public String getTo_sex() {
		return to_sex;
	}
	public void setTo_sex(String to_sex) {
		this.to_sex = to_sex;
	}
	public String getTo_avatar() {
		return to_avatar;
	}
	public void setTo_avatar(String to_avatar) {
		this.to_avatar = to_avatar;
	}
	public String getTo_addr() {
		return to_addr;
	}
	public void setTo_addr(String to_addr) {
		this.to_addr = to_addr;
	}
}
