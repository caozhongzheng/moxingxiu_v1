package com.moinapp.wuliao.model;

import java.io.Serializable;

public class UserInformation_Model extends Base_Information_Model implements Serializable {

	private String username;
	private String birthday;
	private String deviceid;
	private String create_time;
	private String share_num;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	
	public String getShare_num() {
		return share_num;
	}

	public void setShare_num(String share_num) {
		this.share_num = share_num;
	}
}
