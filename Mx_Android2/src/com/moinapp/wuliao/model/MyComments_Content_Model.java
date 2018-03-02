package com.moinapp.wuliao.model;

import java.io.Serializable;

public class MyComments_Content_Model implements Serializable {

	private String id;
	private String content;
	private String addtime;
	private String about_type;
	private String about_id;
	private String title;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
