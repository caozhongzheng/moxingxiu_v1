package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Recommend_Model implements Serializable {

	private int total;

	private ArrayList<Recommend_Content_Model> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<Recommend_Content_Model> getList() {
		return list;
	}

	public void setList(ArrayList<Recommend_Content_Model> list) {
		this.list = list;
	}
	
	
}
