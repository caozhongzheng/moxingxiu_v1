package com.moinapp.wuliao.model;

import java.io.Serializable;

public class FaceDetail_Content_Model extends Detail_Base_Model implements Serializable {

	private String classid;
	private String descr;
	private String addtime;
	private String hits;
	private String download_num;
	private String share_num;
	private String is_tj;
	private String aid;
	private String price;
	private String big_pic;
	private String small_pic;
	private String filepath;
	private String filedir;
	private String filename;
	private FaceDetail_Content_Faces faces;
	private String update_time;
	private String classname;
	private String star_num;
	private String filesize;

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
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

	public String getDownload_num() {
		return download_num;
	}

	public void setDownload_num(String download_num) {
		this.download_num = download_num;
	}
	
	public String getShare_num() {
		return share_num;
	}

	public void setShare_num(String share_num) {
		this.share_num = share_num;
	}

	public String getIs_tj() {
		return is_tj;
	}

	public void setIs_tj(String is_tj) {
		this.is_tj = is_tj;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBig_pic() {
		return big_pic;
	}

	public void setBig_pic(String big_pic) {
		this.big_pic = big_pic;
	}

	public String getSmall_pic() {
		return small_pic;
	}

	public void setSmall_pic(String small_pic) {
		this.small_pic = small_pic;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getFiledir() {
		return filedir;
	}

	public void setFiledir(String filedir) {
		this.filedir = filedir;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public FaceDetail_Content_Faces getFaces() {
		return faces;
	}

	public void setFaces(FaceDetail_Content_Faces faces) {
		this.faces = faces;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getStar_num() {
		return star_num;
	}

	public void setStar_num(String star_num) {
		this.star_num = star_num;
	}

	public String getFilesize() {
		return filesize;
	}

	public void setFilesize(String filesize) {
		this.filesize = filesize;
	}
}