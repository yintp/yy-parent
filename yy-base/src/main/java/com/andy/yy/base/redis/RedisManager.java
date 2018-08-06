package com.andy.yy.base.redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisPubSub;

import java.util.*;

public class RedisManager {

	public static int FIFTEEN_DAY = 60 * 60 * 24 * 15;
	public static int EXP_ONE_DAY = 60 * 60 * 24;
	public static int EXP_ONE_HOURS = 60 * 60;
	public static int EXP_ONE_MIN = 60;

	/**
	 * 增加对象缓存 无过期时间
	 */
	public static void set(String key, Object val) {
		set(key, val, 0);
	}

	/**
	 * 增加对象缓存
	 */
	public static void set(String key, Object val, int exptime) {
		SessionCacheManager.inst().set(key, val, exptime);
	}

	/**
	 * 刷新超时时间
	 */
	public static void setExpire(String key, int exptime) {
		SessionCacheManager.inst().expire(key, exptime);
	}

	public static int getInt(String key) {
		Object v = get(key);
		if (v == null)
			return 0;
		return (Integer) v;
	}

	public static String getString(String key) {
		Object v = get(key);
		if (v == null)
			return null;
		return (String) v;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getObject(String key, Class<T> clazz) {
		Object v = get(key);
		if (v == null)
			return null;
		return (T) v;
	}

	/**
	 * 提取对象缓存
	 */
	public static Object get(String key) {
		if (SessionCacheManager.inst().get(key) == null || SessionCacheManager.inst().get(key).equals("null")) {
			return null;
		} else {
			return SessionCacheManager.inst().get(key);
		}
	}

	/**
	 * 删除对象缓存
	 */
	public static void delete(String key) {
		SessionCacheManager.inst().delete(key);
	}

	/**
	 * 删除有序集合中的某一个值
	 */
	public static void zdelete(String key, String member) {
		SessionCacheManager.inst().zdelete(key, member);
	}

	/**
	 * 有序集合 累加数值 无过期时间
	 */
	public static void zadd(String key, long sn, String uid) {
		zadd(key, sn, uid, 0);
	}

	/**
	 * 有序集合 累加数值
	 */
	public static void zadd(String key, long sn, String member, int exptime) {
		SessionCacheManager.inst().zincrby(key, sn, member, exptime);
	}

	/**
	 * 无序集合加入值
	 **/
	public static void sadd(String key, String member, int exptime) {
		SessionCacheManager.inst().sadd(key, member, exptime);
	}

	public static void sadd(int exptime, String key, String... members) {
		SessionCacheManager.inst().sadd(exptime, key, members);
	}

	public static void srem(String key, String member) {
		SessionCacheManager.inst().srem(key, member);
	}

	@SuppressWarnings("rawtypes")
	public static Set zrevrank(String key, int pi, int pageSize) {
		return SessionCacheManager.inst().zrevrank(key, pi, pageSize);
	}

	public static Long zrevrank(String key, String member) {
		return SessionCacheManager.inst().zrevrank(key, member);
	}

	/**
	 * 获取无序集合
	 */
	public static Set<String> smembers(String key) {
		return SessionCacheManager.inst().smembers(key);
	}

	/**
	 * 有序集合 覆盖数值 无过期时间
	 */
	public static void zset(String key, long gold, String uid) {
		zset(key, gold, uid, 0);
	}

	/**
	 * 有序集合 覆盖数值
	 */
	public static void zset(String key, long gold, String uid, int exptime) {
		SessionCacheManager.inst().zadd(key, gold, uid, exptime);
	}

	/**
	 * 有序列表
	 */
	public static void setList(String key, Object value, int exptime) {
		SessionCacheManager.inst().setList(key, value, exptime);
	}

	/**
	 * lpush
	 **/
	public static void setList(String key, Object value) {
		SessionCacheManager.inst().setList(key, value);
	}

	public static Object getLeftList(String key) {
		return SessionCacheManager.inst().getListL(key);
	}

	/*** 获取列表长度 */
	public static long getLLen(String key) {
		return SessionCacheManager.inst().getListLen(key);
	}

	/**
	 * 获取类别头部并移除
	 **/
	public static Object getListHead(String key) {
		return SessionCacheManager.inst().getLpop(key);
	}

	/**
	 * 获取列表尾部并移除
	 **/
	public static Object getListTail(String key) {
		return SessionCacheManager.inst().getRpop(key);
	}

	public static void ldelete(String key, Object value, int count) {
		SessionCacheManager.inst().ldelete(key, value, count);
	}

	/**
	 * 移除并返回列表的最后一个元素
	 **/
	public static Object lrpop(String key) {
		return SessionCacheManager.inst().rpop(key);
	}

	/**
	 * 提取有序集合中的数值
	 */
	public static Double get(String key, String uid) {
		Double v = SessionCacheManager.inst().zscore(key, uid);
		return v == null ? 0 : v;
	}

	public static Double getHaveNull(String key, String uid) {
		Double v = SessionCacheManager.inst().zscore(key, uid);
		return v;
	}

	public static <T> List<T> getList(String key) {
		return SessionCacheManager.inst().<T>getListByType(key);
	}

	public static List<Object> getList(String key, int start, int end) {
		return SessionCacheManager.inst().getList(key, start, end);
	}

	/**
	 * 提取有序集合排序后的名次区间 返回的uid
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> sort(String key, int start, int end) {
		List<String> list = new ArrayList<String>();
		Set set = zrevrank(key, start, end);
		Object[] objs = set.toArray();
		for (int i = 0; i < set.size(); i++) {
			byte[] val = (byte[]) objs[i];
			String uid = new String(val);
			list.add(uid);
		}

		return list;
	}

	/**
	 * 获取有序集合的大小
	 */
	public static long getSize(String key) {
		return SessionCacheManager.inst().getSize(key);
	}

	/**
	 * 设置多个哈希字段的多个值
	 **/
	@SuppressWarnings("rawtypes")
	public static void hmset(String key, Map map) {
		SessionCacheManager.inst().hmset(key, map);
	}

	/**
	 * 设置哈希字段的字符串值
	 **/
	public static void hset(String key, String field, String value) {
		SessionCacheManager.inst().hset(key, field, value);
	}

	/**
	 * 获得所有给定的哈希字段的值
	 **/
	public static String hget(String key, String field) {
		return SessionCacheManager.inst().hget(key, field);
	}

	/**
	 * 获取哈希字段数
	 **/
	public static int hlen(String key) {
		return SessionCacheManager.inst().hlen(key);
	}

	/**
	 * 判断一个哈希字段存在与否
	 **/
	public static boolean hexists(String key, String field) {
		return SessionCacheManager.inst().hexists(key, field);
	}

	/**
	 * 获取所有的哈希字段
	 **/
	public static Set<String> hkeys(String key) {
		return SessionCacheManager.inst().hkeys(key);
	}

	/**
	 * 判断 member 元素是否集合 key 的成员
	 */
	public static boolean sismember(String key, String uid) {
		return SessionCacheManager.inst().sismember(key, uid);
	}

	/**
	 * 为哈希表key中的域field的值加上增量num
	 **/
	public static void hincrby(String key, String field, long num, int exptime) {
		if (StringUtils.isEmpty(field))
			return;
		SessionCacheManager.inst().hincrby(key, field, num, exptime);
	}

	public static Long publish(String channel, String message) {
		return SessionCacheManager.inst().publish(channel, message);
	}

	public static void subscribe(JedisPubSub jedisPubSub, String... channels) {
		SessionCacheManager.inst().subscribe(jedisPubSub, channels);
	}

	/**
	 * 获得距离24点的时间（秒）
	 *
	 * @return
	 */
	public static int getLastTimeToDay() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) ((cal.getTimeInMillis() - new java.util.Date().getTime()) / 1000);
	}

	/**
	 * 计算当前到下一个月1号零点的剩余秒数
	 */
	public static int getLastSecondsToMonthEnd() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
	}

	/**
	 * 获得距离下一个时间点的剩余时间（秒）
	 *
	 * @param timepoint 时间点
	 * @return s 剩余秒数
	 */
	public static int getLastTimeToTimePoint(int timepoint) {
		final Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);// 分
		int second = cal.get(Calendar.SECOND);// 秒
		int s = 0;
		if (hour < timepoint) {
			s = (timepoint - hour) * 3600;
		} else {
			s = (24 + timepoint - hour) * 3600;
		}
		s = s - (minute * 60) - second;
		return s;
	}
}
