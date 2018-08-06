package com.andy.yy.base.db;

import com.andy.yy.base.log.LoggerUtils;

/**
 * @author richard
 * @since 2018/1/31 15:38
 */
public class DataSourceContext {

	private static final LoggerUtils logger = LoggerUtils.newLogger(DataSourceContext.class);
	private static final ThreadLocal<String> context = new ThreadLocal<>();

	public static void setDataSourceKey(String key) {
		logger.info("set data source key: {}", key);
		context.set(key);
	}
	public static String getDataSourceKey() {
		String key = context.get();
		logger.info("get data source key: {}", key);
		return key;
	}
	public static void clearDataSourceKey() {
		context.remove();
	}
}
