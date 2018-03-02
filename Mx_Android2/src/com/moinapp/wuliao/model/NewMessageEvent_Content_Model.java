package com.moinapp.wuliao.model;

import java.io.Serializable;

public class NewMessageEvent_Content_Model implements Serializable {
	private String id;
	private String from;
	private String to;
	private String message;
	private String status;
	private String addtime;
	
	
	public NewMessageEvent_Content_Model(String id, String from, String to,
			String message, String status, String addtime) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.message = message;
		this.status = status;
		this.addtime = addtime;
	}
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
	
	
}
