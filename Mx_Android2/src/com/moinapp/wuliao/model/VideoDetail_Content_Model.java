package com.moinapp.wuliao.model;

import java.io.Serializable;

public class VideoDetail_Content_Model extends Detail_Base_Model implements Serializable {

	private String source;
	private String source_url;
	private String update_time;
	private String big_pic;
	
	

	public String getBig_pic() {
		return big_pic;
	}

	public void setBig_pic(String big_pic) {
		this.big_pic = big_pic;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

}
