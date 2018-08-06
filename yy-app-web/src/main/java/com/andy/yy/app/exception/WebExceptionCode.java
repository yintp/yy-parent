package com.andy.yy.app.exception;

import com.andy.yy.base.core.ExceptionCode;

/**
 * @author richard
 * @since 2018/2/3 23:05
 */
public class WebExceptionCode extends ExceptionCode {

	private static final long serialVersionUID = 1L;

	public static ExceptionCode SUCCESS = getCode(10001, "成功");
	public static ExceptionCode FAIL = getCode(10002, "失败");
	public static ExceptionCode CONNECTION_TIME_OUT = getCode(10003, "服务连接超时，请稍后请重试");
	public static ExceptionCode NO_LOGIN = getCode(10004, "未登陆");
	public static ExceptionCode TOKEN_EXPIRED = getCode(10005, "登陆状态失效");

	public WebExceptionCode(int code, String message) {
		super(code, message);
	}

	public static ExceptionCode getCode(int index, String message) {
		return ExceptionCode.getCode(index, message);
	}
}
