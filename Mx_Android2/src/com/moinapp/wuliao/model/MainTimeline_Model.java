package com.moinapp.wuliao.model;

import java.io.Serializable;

public class MainTimeline_Model implements Serializable {

	private MainTimeline_Content_Model timeline;
	private Ad_Model advert;
	private Recommend_Model recommend;

	public MainTimeline_Content_Model getTimeline() {
		return timeline;
	}

	public void setTimeline(MainTimeline_Content_Model timeline) {
		this.timeline = timeline;
	}

	public Ad_Model getAdvert() {
		return advert;
	}

	public void setAdvert(Ad_Model advert) {
		this.advert = advert;
	}

	public Recommend_Model getRecommend() {
		return recommend;
	}

	public void setRecommend(Recommend_Model recommend) {
		this.recommend = recommend;
	}

}
