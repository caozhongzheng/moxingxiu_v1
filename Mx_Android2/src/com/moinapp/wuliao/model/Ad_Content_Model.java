package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Ad_Content_Model implements Serializable {

	private String id;
	private String title;
	private String pic;
	private String descr;
	private String position_id;
	private String relevant_id;
	private String about_type;
	private String about_id;
	private String create_time;
	private String about_title;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getPosition_id() {
		return position_id;
	}
	public void setPosition_id(String position_id) {
		this.position_id = position_id;
	}
	public String getRelevant_id() {
		return relevant_id;
	}
	public void setRelevant_id(String relevant_id) {
		this.relevant_id = relevant_id;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAbout_title() {
		return about_title;
	}
	public void setAbout_title(String about_title) {
		this.about_title = about_title;
	}


}
