package com.andy.yy.user.dto;

import java.io.Serializable;

public class FriendDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long friendId;
	private String name;
	private String headImg;
	private String c;

	public Long getFriendId() {
		return friendId;
	}

	public void setFriendId(Long friendId) {
		this.friendId = friendId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}
}
