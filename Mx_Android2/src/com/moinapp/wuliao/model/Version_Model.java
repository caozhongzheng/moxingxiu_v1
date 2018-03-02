package com.moinapp.wuliao.model;

import java.io.Serializable;

public class Version_Model implements Serializable {
	private String appname;
	private String apkFileUrl;
	private String versionNameAndroid;
	private int versionCodeAndroid;
	private String versionNameIos;
	private boolean isForced;

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApkFileUrl() {
		return apkFileUrl;
	}

	public void setApkFileUrl(String apkFileUrl) {
		this.apkFileUrl = apkFileUrl;
	}

	public String getVersionNameAndroid() {
		return versionNameAndroid;
	}

	public void setVersionNameAndroid(String versionNameAndroid) {
		this.versionNameAndroid = versionNameAndroid;
	}

	public int getVersionCodeAndroid() {
		return versionCodeAndroid;
	}

	public void setVersionCodeAndroid(int versionCodeAndroid) {
		this.versionCodeAndroid = versionCodeAndroid;
	}

	public String getVersionNameIos() {
		return versionNameIos;
	}

	public void setVersionNameIos(String versionNameIos) {
		this.versionNameIos = versionNameIos;
	}

	public boolean isForced() {
		return isForced;
	}

	public void setForced(boolean isForced) {
		this.isForced = isForced;
	}

}
