package com.andy.yy.app.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author richard
 * @since 2018/2/4 22:44
 */
public class HttpHeaderUtil {
	public static String getToken(HttpServletRequest request) {
		return getData(0, "A-SID", request);
	}

	private static String getData(int index, String headerName, HttpServletRequest request) {
		String header = request.getHeader(headerName);
		if(header != null) {
			String[] headerArr = header.split("/");
			if(headerArr.length <= index) {
				return "";
			} else {
				return headerArr[index];
			}
		} else {
			return "";
		}
	}
}
