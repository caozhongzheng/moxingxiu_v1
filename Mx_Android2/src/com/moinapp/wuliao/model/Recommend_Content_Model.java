package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Recommend_Content_Model extends MainTimelineItem_Model implements Serializable {

	private String star_num;

	public String getStar_num() {
		return star_num;
	}

	public void setStar_num(String star_num) {
		this.star_num = star_num;
	}

}
