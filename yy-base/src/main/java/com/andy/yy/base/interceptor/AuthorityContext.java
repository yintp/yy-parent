package com.andy.yy.base.interceptor;

import java.io.Serializable;

/**
 * @author richard
 * @since 2018/2/4 22:43
 */
public class AuthorityContext implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	private String userName;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
