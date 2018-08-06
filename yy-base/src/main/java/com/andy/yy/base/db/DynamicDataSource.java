package com.andy.yy.base.db;

import com.andy.yy.base.log.LoggerUtils;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author richard
 * @since 2018/1/31 15:12
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

	private static final LoggerUtils logger = LoggerUtils.newLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String key = DataSourceContext.getDataSourceKey();
		logger.info("routing key: {}", key);
		return key;
	}
}
