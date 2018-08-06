package com.andy.yy.base.mybatis;

import com.andy.yy.base.redis.SessionCacheManager;
import org.apache.ibatis.cache.Cache;
import org.mybatis.caches.redis.RedisCallback;
import org.mybatis.caches.redis.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public final class MybatisRedisCache implements Cache {

	  private final ReadWriteLock readWriteLock = new MybatisReadWriteLock();

	  private String id;

	  private static JedisPool pool;

	  public MybatisRedisCache(final String id) {
	    if (id == null) {
	      throw new IllegalArgumentException("Cache instances require an ID");
	    }
	    this.id = id;
		pool = SessionCacheManager.inst().getPool();
	  }

	  private Object execute(RedisCallback callback) {
		  Jedis jedis = null;
		  try {
			  try {
				  jedis = pool.getResource();
				  return callback.doWithRedis(jedis);
			  } finally {
				  if (jedis != null) {
					  jedis.close();
				  }
			  }
		  } catch(Exception e) {
			 return null;
		  }
	  }

	  @Override
	  public String getId() {
	    return this.id;
	  }

	  @Override
	  public int getSize() {
	    return (Integer) execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        Map<byte[], byte[]> result = jedis.hgetAll(id.toString().getBytes());
	        return result.size();
	      }
	    });
	  }

	  @Override
	  public void putObject(final Object key, final Object value) {
	    execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        jedis.hset(id.toString().getBytes(), key.toString().getBytes(), SerializeUtil.serialize(value));
	        return null;
	      }
	    });
	  }

	  @Override
	  public Object getObject(final Object key) {
	    return execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        return SerializeUtil.unserialize(jedis.hget(id.toString().getBytes(), key.toString().getBytes()));
	      }
	    });
	  }

	  @Override
	  public Object removeObject(final Object key) {
	    return execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        return jedis.hdel(id.toString(), key.toString());
	      }
	    });
	  }

	  @Override
	  public void clear() {
	    execute(new RedisCallback() {
	      @Override
	      public Object doWithRedis(Jedis jedis) {
	        jedis.del(id.toString());
	        return null;
	      }
	    });

	  }

	  @Override
	  public ReadWriteLock getReadWriteLock() {
	    return readWriteLock;
	  }

	  @Override
	  public String toString() {
	    return "Redis {" + id + "}";
	  }

	}

