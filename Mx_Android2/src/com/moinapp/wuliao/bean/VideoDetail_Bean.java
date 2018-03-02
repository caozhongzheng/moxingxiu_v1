package com.moinapp.wuliao.bean;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.model.VideoDetail_Model;
import com.zyh.volleybox.JsonUtil;


public class VideoDetail_Bean {

	private VideoDetail_Model msg;

	public VideoDetail_Model getData() {
		return msg;
	}

	public void setData(VideoDetail_Model msg) {
		this.msg = msg;
	}

	/**
	 * @param data
	 * @return
	 * @throws WHT_Exception 
	 */
	public static VideoDetail_Bean dataParser(String data) throws M_Exception {
		VideoDetail_Bean reigst = new VideoDetail_Bean();

		try {
			VideoDetail_Model myanswerlist = (VideoDetail_Model) JsonUtil.DataToObject(data, new TypeToken<VideoDetail_Model>(){}.getType());
			reigst.setData(myanswerlist);
		} 
		catch(JsonParseException e) {
			throw M_Exception.dataParser(e);
		}
		
		return reigst;
	}
}
