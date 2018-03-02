package com.moinapp.wuliao.model;

import java.io.Serializable;

public class SearchHotWords_Content_Model implements Serializable {
	
	private String word;
	private String cnt;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	
}
