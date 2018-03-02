package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RecentUser_Model implements Serializable {

	private int total;

	private ArrayList<RecentUser_List_Model> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<RecentUser_List_Model> getList() {
		return list;
	}

	public void setList(ArrayList<RecentUser_List_Model> list) {
		this.list = list;
	}

}
