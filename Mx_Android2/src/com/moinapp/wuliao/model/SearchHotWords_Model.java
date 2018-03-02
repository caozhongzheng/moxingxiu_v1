package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchHotWords_Model implements Serializable {
	
	private ArrayList<SearchHotWords_Content_Model> list;
	private String total;
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public ArrayList<SearchHotWords_Content_Model> getList() {
		return list;
	}
	public void setList(ArrayList<SearchHotWords_Content_Model> list) {
		this.list = list;
	}
}
