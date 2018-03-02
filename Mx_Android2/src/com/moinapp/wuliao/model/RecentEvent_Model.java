package com.moinapp.wuliao.model;

import java.io.Serializable;

public class RecentEvent_Model implements Serializable {

	private NewFriendshipEvent_Model newFriendshipEvent;
	private NewMessageEvent_Model newMessageEvent;
	
	public NewFriendshipEvent_Model getNewFriendshipEvent() {
		return newFriendshipEvent;
	}
	public void setNewFriendshipEvent(NewFriendshipEvent_Model newFriendshipEvent) {
		this.newFriendshipEvent = newFriendshipEvent;
	}
	public NewMessageEvent_Model getNewMessageEvent() {
		return newMessageEvent;
	}
	public void setNewMessageEvent(NewMessageEvent_Model newMessageEvent) {
		this.newMessageEvent = newMessageEvent;
	}

	
}
