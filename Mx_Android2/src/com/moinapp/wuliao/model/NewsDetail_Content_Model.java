package com.moinapp.wuliao.model;

import java.io.Serializable;

public class NewsDetail_Content_Model extends Detail_Base_Model implements Serializable {

	private String classid;
	private String content;
	private String descr;
	private String orderid;
	private String update_time;
	private String addtime;
	private String hits;
	private String is_tj;
	private String source;
	private String source_url;
	private String status;
	private String deitor;
	private String verifier;

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	public String getIs_tj() {
		return is_tj;
	}

	public void setIs_tj(String is_tj) {
		this.is_tj = is_tj;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource_url() {
		return source_url;
	}

	public void setSource_url(String source_url) {
		this.source_url = source_url;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeitor() {
		return deitor;
	}

	public void setDeitor(String deitor) {
		this.deitor = deitor;
	}

	public String getVerifier() {
		return verifier;
	}

	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}

}
