package com.andy.yy.base.core;

/**
 * @author richard
 * @since 2018/1/31 17:08
 */
public class ServiceException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	private ExceptionCode code;

	public ServiceException(ExceptionCode code) {
		super(code == null ? "" : code.getMessage());
		this.code = code;
	}
	public ServiceException(ExceptionCode code, Throwable cause){
		super(code == null ? "" : code.getMessage(), cause);
		this.code = code;
	}

	public Integer getCodeIndex() {
		return code == null ?  -1 : code.getIndex();
	}
	public String getCodeMessage() {
		return code == null ? "" : code.getMessage();
	}
}
