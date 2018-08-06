package com.andy.yy.base.redis;

import com.andy.yy.base.log.LoggerUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public abstract class BaseRedisCache {

	private static final LoggerUtils log = LoggerUtils.newLogger(BaseRedisCache.class);

	protected JedisPool pool = null;

	public abstract void initPool();

	/**
	 * 哨兵连接池
	 * 
	 * @return
	 */
	public JedisPool getPool() {
		if (pool == null) {
			initPool();
		}
		return pool;
	}

	/**
	 * 返还到连接池
	 * 
	 * @param pool
	 * @param redis
	 */
	public void returnResource(JedisPool pool, Jedis redis) {
		if (redis != null) {
			pool.returnResource(redis);
		}
	}

	public void set(String key, Object value) {
		set(key, value, 0);
	}

	public void expire(String key, int exp_time) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.expire(key.getBytes(), exp_time);

		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void delete(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.del(key.getBytes());

		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void zdelete(String key, String member) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.zrem(key.getBytes(), member.getBytes());

		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/***
	 * 
	 * @Title: zadd
	 * @Description: (覆盖score的值)
	 * @return void 返回类型
	 * @param key
	 * @param score
	 * @param member
	 * @param expire
	 */
	public void zadd(String key, long score, String member, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.zadd(key.getBytes(), score, member.getBytes());
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void zadd(String key, long score, String member) {
		zadd(key, score, member, 0);
	}

	public Double zscore(String key, String member) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			return jedis.zscore(key.getBytes(), member.getBytes());
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	/**
	 * 
	 * @Title: zincrby
	 * @Description: (累加score的值)
	 * @return void 返回类型
	 * @param key
	 * @param score
	 * @param member
	 * @param expire
	 */
	public void zincrby(String key, long score, String member, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.zincrby(key.getBytes(), score, member.getBytes());
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 
	 * @param key
	 * @param val
	 * @param expire
	 */
	public void sadd(String key, String val, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.sadd(key, val);
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}
	
	public void sadd(int expire,String key,String...members) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			
			jedis.sadd(key, members);
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}
	
	/**
	 * 移除无序集合中的元素
	 * @author luobh
	 * @date 2016年9月27日
	 * @param key
	 * @param members
	 */
	public void srem(String key, String... members) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.srem(key, members);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/***
	 * 
	 * @param key
	 * @param val
	 * @param expire
	 */
	public Set<String> smembers(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			return jedis.smembers(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public void zincrby(String key, long score, String member) {
		zincrby(key, score, member, 0);
	}

	/**
	 * 返回分数排名，从大到小降序排列
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set zrevrank(String key, int start, int end) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			return jedis.zrevrange(key.getBytes(), start, end);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	/**
	 * 返回成员排名，从大到小降序排列
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Long zrevrank(String key, String member) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			return jedis.zrevrank(key.getBytes(), member.getBytes());
		} catch (Exception e) {
			log.error(" zrevrank 出错了，pool->" + pool + " jedis->" + jedis + " e->", e);
			returnBrokenResource(pool, jedis);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	private void returnBrokenResource(JedisPool pool, Jedis jedis) {
		if (pool != null && jedis != null) {
			pool.returnBrokenResource(jedis);
		}
	}

	/**
	 * 返回区间内的值
	 * 
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public Set zrangeByScore(String key, double min, double max) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			return jedis.zrangeByScore(key, min, max);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public void incr(String key, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.incr(key.getBytes());
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void incrby(String key, long increment, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.incrBy(key.getBytes(), increment);
			if (expire > 0) {
				jedis.expire(key.getBytes(), expire);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 */
	public void set(String key, Object value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.set(key.getBytes(), SerializeUtil.serialize(value));
			if (expire > 0)
				jedis.expire(key.getBytes(), expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public Object get(String key) {
		return get(key.getBytes());
	}

	public Object get(byte[] key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			byte[] data = jedis.get(key);
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			// 释放redis对象
			log.error("get ex,key:{}", new String(key), e);
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return null;
	}
	/**
	 * set
	 * 
	 * @param key
	 * @param value
	 * @param expire
	 */
	public void setString(String key, String value, int expire) {
	    JedisPool pool = null;
	    Jedis jedis = null;
	    try {
	        pool = getPool();
	        jedis = pool.getResource();
	        
	        jedis.set(key, value);
	        if (expire > 0)
	            jedis.expire(key, expire);
	    } catch (Exception e) {
	        // 释放redis对象
	        pool.returnBrokenResource(jedis);
	        log.error("redis exception", e);
	    } finally {
	        // 返还到连接池
	        returnResource(pool, jedis);
	    }
	}

	public String getString(String key) {
	    JedisPool pool = null;
	    Jedis jedis = null;
	    try {
	        pool = getPool();
	        jedis = pool.getResource();
	        return jedis.get(key);
	    } catch (Exception e) {
	        // 释放redis对象
	        log.error("get ex,key:{}", new String(key), e);
	        pool.returnBrokenResource(jedis);
	        log.error("redis exception", e);
	    } finally {
	        // 返还到连接池
	        returnResource(pool, jedis);
	    }
	    
	    return null;
	}

	public void setList(String key, String value) {
		setList(key, value, 0);
	}

	public void setList(String key, String value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// jedis.del(key);
			jedis.lpush(key, value);
			if (expire > 0)
				jedis.expire(key, expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void setList(String key, Object value) {
		setList(key, value, 0);
	}

	public void setList(String key, Object value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// jedis.del(key);
			jedis.lpush(key.getBytes(), SerializeUtil.serialize(value));
			if (expire > 0)
				jedis.expire(key.getBytes(), expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public List<Object> getList(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		List<Object> resList = new ArrayList<Object>();
		try {
			pool = getPool();
			jedis = pool.getResource();
			List<byte[]> byteList = jedis.lrange(key.getBytes(), 0, -1);
			for (int i = 0; i < byteList.size(); i++) {
				resList.add(SerializeUtil.unserialize(byteList.get(i)));
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return resList;
	}

	public <T> List<T> getListByType(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		List<T> resList = new ArrayList<T>();
		try {
			pool = getPool();
			jedis = pool.getResource();
			List<byte[]> byteList = jedis.lrange(key.getBytes(), 0, -1);
			for (int i = 0; i < byteList.size(); i++) {
				resList.add((T) SerializeUtil.unserialize(byteList.get(i)));
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return resList;
	}

	/**
	 * 移除列表中的元素 从存于 key 的列表里移除前 count 次出现的值为 value 的元素
	 * 
	 * @param key
	 * @param value
	 * @param count
	 *            <br/>
	 *            count > 0: 从头往尾移除值为 value 的元素。 <br/>
	 *            count < 0: 从尾往头移除值为 value 的元素。 <br/>
	 *            count = 0: 移除所有值为 value 的元素。 <br/>
	 */
	public void ldelete(String key, Object value, int count) {
		ldelete(key.getBytes(), value, count);
	}

	/**
	 * Redis RPOP命令删除，并返回列表保存在key的最后一个元素 返回字符串，最后一个元素的值，或者关键不存在返回null。
	 * 
	 * @param key
	 */
	public Object rpop(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.rpop(key.getBytes());
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	/**
	 * 移除列表中的元素 从存于 key 的列表里移除前 count 次出现的值为 value 的元素
	 * 
	 * @param key
	 * @param value
	 * @param count
	 *            <br/>
	 *            count > 0: 从头往尾移除值为 value 的元素。 <br/>
	 *            count < 0: 从尾往头移除值为 value 的元素。 <br/>
	 *            count = 0: 移除所有值为 value 的元素。 <br/>
	 */
	public void ldelete(byte[] key, Object value, int count) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			jedis.lrem(key, count, SerializeUtil.serialize(value));

		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 移除列表中的元素 从存于 key 的列表里移除前 count 次出现的值为 value 的元素
	 * 
	 * @param key
	 * @param value
	 * @param count
	 *            <br/>
	 *            count > 0: 从头往尾移除值为 value 的元素。 <br/>
	 *            count < 0: 从尾往头移除值为 value 的元素。 <br/>
	 *            count = 0: 移除所有值为 value 的元素。 <br/>
	 */
	public Long ldelete(String key, String value, int count) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.lrem(key, count, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return 0l;
	}

	/**
	 * 
	 * @Title: getSize
	 * @Description: (获取key 集合的大小)
	 * @return long 返回类型
	 * @param key
	 * @return
	 */
	public long getSize(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.zcard(key.getBytes());
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return 0;
	}

	/**
	 * hash操作 set
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, String value) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void hset(String key, String field, Object value) {
		hset(key, field, value, 0);
	}

	/**
	 * hash操作 set
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, Object value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
			if (expire > 0)
				jedis.expire(key, expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 添加HashMap
	 * 
	 * @param key
	 * @param map
	 */
	public void hmset(String key, Map map) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.hmset(key, map);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/***
	 * hash操作 get
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public String hget(String key, String field) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.hget(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public Long hdel(String key, String field) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.hdel(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public Object hgetObject(String key, String field) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return SerializeUtil.unserialize(jedis.hget(key.getBytes(), field.getBytes()));
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public <T> Map<String, T> hgetObject(String key, T t) {
		Map<String, T> value = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			Map<byte[], byte[]> map = jedis.hgetAll(key.getBytes());
			if (map != null && map.size() > 0) {
				value = new HashMap<String, T>();
				for (byte[] objKey : map.keySet()) {
					value.put(new String(objKey), (T) SerializeUtil.unserialize(map.get(objKey)));
				}
			}
			return value;
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	/**
	 * 获取哈希字段数
	 * 
	 * @param key
	 * @return
	 */
	public int hlen(String key) {

		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return Integer.parseInt(jedis.hlen(key).toString());
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return 0;
	}

	/**
	 * 判断一个哈希字段存在与否
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hexists(String key, String field) {
		boolean isexit = false;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			isexit = jedis.hexists(key, field);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return isexit;
	}

	/**
	 * 获取哈希的所有字段
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		Set<String> array = null;
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			array = jedis.hkeys(key);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return array;
	}

	/**
	 * 判断 member 元素是否集合 key 的成员 如果 member 元素是集合的成员，返回 1 如果 member 元素不是集合的成员，或
	 * key 不存在，返回 0
	 */
	public boolean sismember(String key, String member) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return jedis.sismember(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			returnResource(pool, jedis);
		}
		return false;
	}

	/**
	 * 为哈希表key中的域field的值加上增量increment。
	 * 
	 * @param key
	 * @param field
	 * @param num
	 *            增量
	 * @param exptime
	 *            过期时间
	 */
	public void hincrby(String key, String field, long num, int exptime) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.hincrBy(key, field, num);
			if (exptime > 0) {
				jedis.expire(key.getBytes(), exptime);
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	/**
	 * 获取表头的数据并移除
	 */
	public Object getListL(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			byte[] data = jedis.lpop(key.getBytes());
			return SerializeUtil.unserialize(data);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return null;
	}

	/**
	 * 获取list长度
	 * 
	 * @param key
	 * @return
	 */
	public long getListLen(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();

			long data = jedis.llen(key.getBytes());
			return data;
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}

		return 0;
	}

	public void setListR(String key, String value) {
		setListR(key, value, 0);
	}

	public void setListR(String key, String value, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// jedis.del(key);
			jedis.rpush(key.getBytes(), SerializeUtil.serialize(value));
			if (expire > 0)
				jedis.expire(key, expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public void lset(String key, Object value, int index) {
		lset(key, value, index, 0);
	}

	public void lset(String key, Object value, int index, int expire) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			// jedis.del(key);
			jedis.lset(key.getBytes(), index, SerializeUtil.serialize(value));
			if (expire > 0)
				jedis.expire(key, expire);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}

	public List<Object> getList(String key, int start, int end) {
		JedisPool pool = null;
		Jedis jedis = null;
		List<Object> resList = new ArrayList<Object>();
		try {
			pool = getPool();
			jedis = pool.getResource();
			List<byte[]> byteList = jedis.lrange(key.getBytes(), start, end);
			for (int i = 0; i < byteList.size(); i++) {
				resList.add(SerializeUtil.unserialize(byteList.get(i)));
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return resList;
	}

	public List<String> getListStr(String key, int start, int end) {
		JedisPool pool = null;
		Jedis jedis = null;
		List<String> resList = new ArrayList<String>();
		try {
			pool = getPool();
			jedis = pool.getResource();
			List<byte[]> byteList = jedis.lrange(key.getBytes(), start, end);
			for (int i = 0; i < byteList.size(); i++) {
				resList.add(new String(byteList.get(i)));
			}
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return resList;
	}

	public Object getRpop(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return SerializeUtil.unserialize(jedis.rpop(key.getBytes()));
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public Object getLpop(String key) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			return SerializeUtil.unserialize(jedis.lpop(key.getBytes()));
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public Long publish(String channel, Object message) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(message);
			String msg1 = baos.toString("ISO-8859-1");// 指定字符集将字节流解码成字符串，否则在订阅时，转换会有问题。
			return jedis.publish(channel.getBytes(), msg1.getBytes());
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
		return null;
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		JedisPool pool = null;
		Jedis jedis = null;
		try {
			pool = getPool();
			jedis = pool.getResource();
			jedis.subscribe(jedisPubSub, channels);
		} catch (Exception e) {
			// 释放redis对象
			pool.returnBrokenResource(jedis);
			log.error("redis exception", e);
		} finally {
			// 返还到连接池
			returnResource(pool, jedis);
		}
	}
}
