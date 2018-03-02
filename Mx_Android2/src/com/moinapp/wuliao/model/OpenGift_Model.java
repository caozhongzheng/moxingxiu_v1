package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class OpenGift_Model implements Serializable{

	private String total;
	private ArrayList<OpenGift_List_Model> list;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<OpenGift_List_Model> getList() {
		return list;
	}

	public void setList(ArrayList<OpenGift_List_Model> list) {
		this.list = list;
	}

}
