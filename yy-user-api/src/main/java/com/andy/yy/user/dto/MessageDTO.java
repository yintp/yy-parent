package com.andy.yy.user.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String type;                     // 消息类型 消息:MSG 添加好友:ADD_FRIEND
	private Long from;                       // 发送人
	private Long to;                         // 接收人
	private String content;                  // 消息内容(json格式)

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
