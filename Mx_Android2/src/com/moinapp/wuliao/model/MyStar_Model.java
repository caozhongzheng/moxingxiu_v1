package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyStar_Model implements Serializable {

	private String total;
	private ArrayList<MyStar_Content_Model> list;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<MyStar_Content_Model> getList() {
		return list;
	}

	public void setList(ArrayList<MyStar_Content_Model> list) {
		this.list = list;
	}

}
