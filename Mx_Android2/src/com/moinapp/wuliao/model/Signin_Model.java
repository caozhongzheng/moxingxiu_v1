package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Signin_Model implements Serializable {

	private String result;
	private String msg;
	private Signin_User_Model data;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Signin_User_Model getData() {
		return data;
	}

	public void setData(Signin_User_Model data) {
		this.data = data;
	}

}
