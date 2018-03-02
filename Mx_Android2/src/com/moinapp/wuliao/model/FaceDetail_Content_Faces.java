package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FaceDetail_Content_Faces implements Serializable {

	private String total;
	private ArrayList<FaceDetail_Content_Faces_List> list;

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public ArrayList<FaceDetail_Content_Faces_List> getList() {
		return list;
	}

	public void setList(ArrayList<FaceDetail_Content_Faces_List> list) {
		this.list = list;
	}

}
