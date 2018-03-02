package com.moinapp.wuliao.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GameDetail_Content_Model extends Detail_Base_Model implements Serializable {

	private String version;
	private String size;
	private String updatetime;
	private String price;
	private String icon;
	private String star_num;
	private String url;
	private String top_pic;
	private String descr;
	private String classname;
	private ArrayList<String> pics;
	private String download_num;
	private String addtime;
	private String update_time;
	private String big_pic;
	

	public String getBig_pic() {
		return big_pic;
	}

	public void setBig_pic(String big_pic) {
		this.big_pic = big_pic;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getStar_num() {
		return star_num;
	}

	public void setStar_num(String star_num) {
		this.star_num = star_num;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTop_pic() {
		return top_pic;
	}

	public void setTop_pic(String top_pic) {
		this.top_pic = top_pic;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public ArrayList<String> getPics() {
		return pics;
	}

	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}

	public String getDownload_num() {
		return download_num;
	}

	public void setDownload_num(String download_num) {
		this.download_num = download_num;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
}
