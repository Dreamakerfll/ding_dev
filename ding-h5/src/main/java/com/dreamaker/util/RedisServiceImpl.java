package com.dreamaker.util;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl<K, V> implements RedisService<K, V> {

private static String redisCode = "utf-8";
	
	private long WAIT_FOR_LOCK = 300;
	
	//锁超时时间 100 s
	private long LOCK_TIME_OUT = 100l;

	/**
	 * @param key
	 */
	@Override
	public long del(final String... keys) {
		
		return redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				long result = 0;
				for (int i = 0; i < keys.length; i++) {
					result = connection.del(keys[i].getBytes());
				}
				return result;
			}
		});
	}
	/**
	 * 是否存储Key锁
	 * @param key
	 * @return
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午12:56:50
	 */
	@Override
	public boolean existsLock(final String key){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] cacheLockName = getKeyLockBytes(key.getBytes());
				return connection.exists(cacheLockName);
			}
			
		});
	}
	/**
	 * 存放Key锁
	 * @param key
	 * @return
	 *
	 * @author zelinyang
	 * @date 2016年1月28日 下午12:56:50
	 */
	@Override
	public boolean putLock(final String key){
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				//设置 开锁
				byte[] cacheLockName = getKeyLockBytes(key.getBytes());
				connection.set(cacheLockName,cacheLockName);
				connection.expire(cacheLockName, LOCK_TIME_OUT);
				
				return true;
			}
		});
	}
	
	@Override
	public boolean waitForLock(String key) {
		return waitForLock(key.getBytes());
	}
	public boolean waitForLock(final byte[] keys) {
		boolean getLock = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				boolean getLock = waitForLock(connection, keys);
				return getLock;
			}
		});
		return getLock;
	}

	public boolean waitForLock(RedisConnection connection, byte[] keys) {

		boolean retry;
		boolean foundLock = false;
		do {
			byte[] cacheLockName = getKeyLockBytes(keys);
			retry = false;
			foundLock = false;
			if (connection.exists(cacheLockName)) {
				foundLock = true;
				try {
					Thread.sleep(WAIT_FOR_LOCK);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				retry = true;
			}
		} while (retry);
		
		return foundLock;
	}
	
	private byte[] getKeyLockBytes(byte[] keys){
		try {
			byte[] cacheLockName = (new String(keys,redisCode)+"~lock").getBytes();
			
			return cacheLockName;
		} catch (UnsupportedEncodingException e) {
			return (new String(keys)+"~lock").getBytes();
		}
	}
	
	@Override
	public void cleanLock(final String key){
		boolean cleaLock = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] cacheLockName =  getKeyLockBytes(key.getBytes());
				connection.del(cacheLockName);
				return true;
			}
		});
		
	}
	
	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@Override
	public void set(final byte[] key, final byte[] value, final long liveTime) {
		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set(key, value);
				if (liveTime > 0) {
					connection.expire(key, liveTime);
				}
				return 1L;
			}
		});
	}

	/**
	 * @param key
	 * @param value
	 * @param liveTime
	 */
	@Override
	public void set(String key, String value, long liveTime) {
		this.set(key.getBytes(), value.getBytes(), liveTime);
	}

	/**
	 * @param key
	 * @param value
	 */
	@Override
	public void set(String key, String value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @param value
	 */
	@Override
	public void set(byte[] key, byte[] value) {
		this.set(key, value, 0L);
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public String get(final String key) {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					byte[] data = connection.get(key.getBytes());
					
					if(data == null) {
						return null;
					}
					return new String(data, redisCode);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return "";
			}
		});
	}

	/**
	 * @param pattern
	 * @return
	 */
	@Override
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(final String key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	/**
	 * @return
	 */
	@Override
	public String flushDb() {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	@Override
	public String flushAll() {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.flushAll();
				return "ok";
			}
		});
	}

	/**
	 * @return
	 */
	@Override
	public long dbSize() {
		Long execute = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Long dbSize = connection.dbSize();
				return dbSize;
			}
		});
		return execute;
	}

	/**
	 * @return
	 */
	@Override
	public String ping() {
		return redisTemplate.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				return connection.ping();
			}
		});
	}

	public RedisServiceImpl() {

	}

	@Autowired
	private RedisTemplate<String, ?> redisTemplate;

	@Override
	public long ttl(final String key) {
		Long ttl = redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				Long ttl = connection.ttl(key.getBytes());
				return ttl;
			}
		});
		return ttl;
	}
	
	@Override
	public boolean existsLockSync(RedisConnection conn, String key) {
		return conn.exists(key.getBytes());
	}
	
	@Override
	public boolean waitLockSync(RedisConnection conn, String key) {
		boolean retry;
		boolean foundLock = false;
		do {
			retry = false;
			foundLock = false;
			if (conn.exists(key.getBytes())) {
				foundLock = true;
				try {
					Thread.sleep(30);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				retry = true;
			}
		} while (retry);
		return false;
	}
	
	@Override
	public void putLockSync(RedisConnection conn, String key) {
		conn.set(key.getBytes(), key.getBytes());
	}
	
	@Override
	public void cleanLockSync(RedisConnection conn, String key) {
		conn.del(key.getBytes());
	}
	
	@Override
	public RedisConnection getConnection() {
		RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
		RedisConnection connection = factory.getConnection();
		return connection;
	}
	
	@Override
	public void closeConnection(RedisConnection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public String queuePop(RedisConnection conn, String key) {
		try {
			byte[] value = conn.lPop(key.getBytes());
			return new String(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String queueGet(RedisConnection conn, String key, long position) {
		try {
			byte[] value = conn.lIndex(key.getBytes(), position);
			return new String(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Long queuePush(RedisConnection conn, String key, String value) {
		try {
			return conn.rPush(key.getBytes(), value.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void queueCleanLock(RedisConnection conn, String key, String value) {
		try {
			conn.lRem(key.getBytes(), 0, value.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Integer hashGetSync(RedisConnection conn, String key, String field) {
		if (conn != null) {
			byte[] bytes = conn.hGet(key.getBytes(), field.getBytes());
			if(bytes!=null){
				return Integer.parseInt(new String(bytes));
				
			}else{
				return null;
			}
		}
		return null;
	}
	@Override
	public void hashSetSync(RedisConnection conn, String key, String field, int value) {
		if (conn != null) {
			conn.hSet(key.getBytes(), field.getBytes(), (value+"").getBytes());
		}
	}
	@Override
	public void hashDelSync(RedisConnection conn, String key, String field) {
		if (conn != null) {
			conn.hDel(key.getBytes(), field.getBytes());
		}
	}
	@Override
	public boolean hashExistsSync(RedisConnection conn, String key, String field) {
		if (conn != null) {
			return conn.hExists(key.getBytes(), field.getBytes());
		}
		return false;
	}
	@Override
	public void hashincrSync(RedisConnection conn, String key, String field) {
		hashincrSync(conn, key, field, 1);
	}
	@Override
	public void hashincrSync(RedisConnection conn, String key, String field, int increment) {
		if (conn != null) {
			conn.hIncrBy(key.getBytes(), field.getBytes(), increment);
		}
		
	}
}
