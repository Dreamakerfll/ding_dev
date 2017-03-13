package com.dreamaker.util;

import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;

public interface RedisService<K, V> {

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	long del(String... keys);

	/**
	 * 添加key value 并且设置存活时间(byte)
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	void set(byte[] key, byte[] value, long liveTime);

	/**
	 * 添加key value 并且设置存活时间
	 * 
	 * @param key
	 * @param value
	 * @param liveTime
	 *            单位秒
	 */
	void set(String key, String value, long liveTime);

	/**
	 * 添加key value
	 * 
	 * @param key
	 * @param value
	 */
	void set(String key, String value);

	/**
	 * 添加key value (字节)(序列化)
	 * 
	 * @param key
	 * @param value
	 */
	void set(byte[] key, byte[] value);

	/**
	 * 获取redis value (String)
	 * 
	 * @param key
	 * @return
	 */
	String get(String key);

	/**
	 * 通过正则匹配keys
	 * 
	 * @param pattern
	 * @return
	 */
	Set<String> keys(String pattern);

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	boolean exists(String key);

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	String flushDb();

	String flushAll();

	/**
	 * 查看redis里有多少数据
	 */
	long dbSize();

	/**
	 * 检查是否连接成功
	 * 
	 * @return
	 */
	String ping();
	
	/**
	 * 获取Key锁
	 * @param key
	 * @return
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午12:56:50
	 */
	public boolean existsLock(String key);
	
	/**
	 * 同步获取锁
	 * @param conn
	 * @param key
	 * @return
	 */
	public boolean existsLockSync(RedisConnection conn, String key);
	
	
	/**
	 * 同步等待锁操作
	 * @param conn
	 * @param key
	 * @return
	 */
	public boolean waitLockSync(RedisConnection conn, String key);
	
	/**
	 * 获取Key锁
	 * @param key
	 * @return
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午12:56:50
	 */
	public boolean waitForLock(String key);
	
	/**
	 * 存放Key锁
	 * @param key
	 * @return
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午12:56:50
	 */
	public boolean putLock(String key);
	
	/**
	 * 同步存放锁
	 * @param conn
	 * @param key
	 */
	public void putLockSync(RedisConnection conn, String key);
	
	/**
	 * 清除Key 锁
	 * @param key
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午1:00:33
	 */
	public void cleanLock(final String key);
	
	/**
	 * 同步清除锁
	 * @param key
	 * @param conn
	 */
	public void cleanLockSync(RedisConnection conn, final String key);
	
	public long ttl(final String key);
	
	/**
	 * 获取一个redis链接
	 * @return
	 */
	public RedisConnection getConnection();
	
	/**
	 * 关闭一个redis链接
	 * @param conn
	 */
	public void closeConnection(RedisConnection conn);
	
	/**
	 * 从队列中pop一个内容结果
	 * @param conn
	 * @param key
	 * @return
	 */
	public String queuePop(RedisConnection conn, String key);
	
	/**
	 * 从队列中获取指定位置元素
	 * @param conn
	 * @param key
	 * @param position
	 * @return
	 */
	public String queueGet(RedisConnection conn, String key, long position);
	
	/**
	 * 向队列中push一个内容
	 * @param conn
	 * @param key
	 * @param value
	 * @return
	 */
	public Long queuePush(RedisConnection conn, String key, String value);
	
	/**
	 * 强制清楚所有锁
	 * @param conn
	 * @param key
	 * @param value
	 */
	public void queueCleanLock(RedisConnection conn, String key, String value);
	
	
	
	
	
	
	
	/**
	 * 获取Hash中指定field下的value
	 * @param key
	 * @param field
	 * @return
	 */
	public Integer hashGetSync(RedisConnection conn, String key, String field);
	/**
	 * 
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hashSetSync(RedisConnection conn, String key, String field, int value);
	/**
	 * 删除指定field
	 * @param key
	 * @param field
	 */
	public void hashDelSync(RedisConnection conn, String key, String field);
	/**
	 * 判断指定field是否存在
	 * @param key
	 * @param field
	 * @return
	 */
	public boolean hashExistsSync(RedisConnection conn, String key, String field);
	/**
	 * 控制指定hash下的field中的value值递增+1
	 * @param key
	 * @param field
	 */
	public void hashincrSync(RedisConnection conn, String key, String field);
	/**
	 * 控制指定hash下的field中的value值递增+increment
	 * @param key
	 * @param field
	 * @param increment
	 */
	public void hashincrSync(RedisConnection conn, String key, String field, int increment);
}
