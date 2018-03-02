package com.moinapp.wuliao.model;


public class RememberUser {
	private String username;
	private String uid;
	private boolean isremember;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isIsremember() {
		return isremember;
	}

	public void setIsremember(boolean isremember) {
		this.isremember = isremember;
	}

	public RememberUser(String username, String uid, boolean isremember) {
		this.username = username;
		this.uid = uid;
		this.isremember = isremember;
	}
}
