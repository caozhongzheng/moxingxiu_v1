package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MainTimeline_Content_Model implements Serializable {

	private int total;

	private ArrayList<MainTimelineItem_Model> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<MainTimelineItem_Model> getList() {
		return list;
	}

	public void setList(ArrayList<MainTimelineItem_Model> list) {
		this.list = list;
	}

}
