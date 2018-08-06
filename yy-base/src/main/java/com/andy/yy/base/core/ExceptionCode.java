package com.andy.yy.base.core;

import java.io.Serializable;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
public class ExceptionCode implements Serializable {

	private static final long serialVersionUID = 1L;

	private int index;
	private String message;

	public ExceptionCode(int index, String message) {
		this.index = index;
		this.message = message;
	}

	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public static ExceptionCode getCode(int index, String message) {
		return new ExceptionCode(index, message);
	}

	public boolean equals(ExceptionCode code) {
		if (code != null) {
			if (this.getIndex() == code.getIndex()) {
				return true;
			}
		}
        return false;
    }
}
