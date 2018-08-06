package com.andy.yy.base.redis;

import com.andy.yy.base.config.EnvApplicationConfigs;
import com.andy.yy.base.log.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class SessionCacheManager extends BaseRedisCache {

	private static final LoggerUtils log = LoggerUtils.newLogger(SessionCacheManager.class);

	private static SessionCacheManager _inst;
	private static Object _lock = new Object();
	private int exp_time = 0;

	public static SessionCacheManager inst() {
		if (_inst != null)
			return _inst;
		synchronized (_lock) {
			if (_inst == null) {
				_inst = new SessionCacheManager();
				_inst.initPool();
			}
		}
		return _inst;
	}

	@Override
	public void initPool() {
		if (pool == null) {
			try {
				String host = EnvApplicationConfigs.getConfig("redis.ip");
				String port = EnvApplicationConfigs.getConfig("redis.port");
				JedisPoolConfig poolConfig = new JedisPoolConfig();
				poolConfig.setMaxTotal(128);
				poolConfig.setMaxIdle(64);
				poolConfig.setMaxWaitMillis(1000l);
				poolConfig.setTestOnBorrow(false);
				poolConfig.setTestOnReturn(false);
				String pwd = EnvApplicationConfigs.getConfig("redis.pwd");
				log.info("redis config: host:{}, port:{}, pwd:{}", host, port, pwd);
				if (StringUtils.isNotEmpty(pwd)) {
					pool = new JedisPool(poolConfig, host, Integer.parseInt(port), 0, pwd);
				} else {
					pool = new JedisPool(poolConfig, host, Integer.parseInt(port), 0);
				}
				log.info("redis init ok, pool:{}", pool);
			} catch (Exception ex) {
				log.error("Couldn't create a connection, bailing out: \nIOException ", ex);
			}
		}
	}

	/**
	 * 返还到连接池
	 *
	 * @param pool
	 * @param redis
	 */
	@SuppressWarnings("deprecation")
	public void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

}