package com.andy.yy.base.utils;

import java.util.UUID;

public class UUIDUtil {

	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}

	public static String upperUuid() {
		return uuid().toUpperCase();
	}
}
