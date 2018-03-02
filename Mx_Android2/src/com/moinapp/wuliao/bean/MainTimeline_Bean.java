package com.moinapp.wuliao.bean;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.moinapp.wuliao.M_Exception;
import com.moinapp.wuliao.model.MainTimeline_Model;
import com.zyh.volleybox.JsonUtil;


public class MainTimeline_Bean {

	private MainTimeline_Model msg;

	public MainTimeline_Model getData() {
		return msg;
	}

	public void setData(MainTimeline_Model msg) {
		this.msg = msg;
	}

	/**
	 * @param data
	 * @return
	 * @throws WHT_Exception 
	 */
	public static MainTimeline_Bean dataParser(String data) throws M_Exception {
		MainTimeline_Bean reigst = new MainTimeline_Bean();

		try {
			MainTimeline_Model myanswerlist = (MainTimeline_Model) JsonUtil.DataToObject(data, new TypeToken<MainTimeline_Model>(){}.getType());
			reigst.setData(myanswerlist);
		} 
		catch(JsonParseException e) {
			throw M_Exception.dataParser(e);
		}
		
		return reigst;
	}
}
