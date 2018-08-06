package com.andy.yy.app.bean;

import com.andy.yy.base.core.ExceptionCode;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author richard
 * @since 2018/2/3 22:58
 */
public class WebJsonBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private int Code;
	private String desc;
	private String tid = UUID.randomUUID().toString();
	private Object data;
	
	public WebJsonBean() {}
	
	public WebJsonBean(ExceptionCode code) {
		this.Code = code.getIndex();
		this.desc = code.getMessage();
	}
	
	public WebJsonBean(int Code, String desc) {
		this.Code = Code;
		this.desc = desc;
	}
	
	public WebJsonBean(int Code, String desc, Object data) {
		this.Code = Code;
		this.desc = desc;
		this.data = data;
	}
	
	public WebJsonBean(ExceptionCode code, Object data) {
		this.Code = code.getIndex();
		this.desc = code.getMessage();
		this.data = data;
	}

	public int getCode() {
		return Code;
	}

	public void setCode(int Code) {
		this.Code = Code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
