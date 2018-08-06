package com.andy.yy.base.config;


import com.andy.yy.base.log.LoggerUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvApplicationConfigs {
	
	private static final LoggerUtils logger = LoggerUtils.newLogger(EnvApplicationConfigs.class);

	private static Properties prop = new Properties();
	
	static {
		String env = System.getProperty("env");
		if(env == null || env.trim().isEmpty()) {
			env = "${env}";
		}
		String fileName = "/env-" + env.trim() + ".properties";

		logger.info("load application config file:{}", fileName);
		InputStream in = EnvApplicationConfigs.class.getResourceAsStream(fileName);
		if(in != null) {
			try {
				prop.load(in);
			} catch (IOException e) {
				logger.error("prop load exception", e);
			}
		} else {
			logger.error("cannot find properties:{}", fileName);
		}
	}
	
	public static String getConfig(String key) {
		return prop.getProperty(key);
	}
}
