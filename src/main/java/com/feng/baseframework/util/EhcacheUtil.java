package com.feng.baseframework.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

/**
 * baseframework
 * 2019/7/8 10:45
 * 缓存工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class EhcacheUtil {

	public static String cacheName = "ehcache";

	private static final String path = "/cache/ehcache.xml";
	private URL url;
	private CacheManager manager;
	private static EhcacheUtil ehCache;

	static {
		System.setProperty(net.sf.ehcache.CacheManager.ENABLE_SHUTDOWN_HOOK_PROPERTY, "true");
	}

	private EhcacheUtil(String path) {
		url = getClass().getResource(path);
		manager = CacheManager.create(url);
	}

	public static EhcacheUtil getInstance() {
		if (ehCache == null) {
			synchronized (EhcacheUtil.class){
				if (ehCache == null) {
					ehCache = new EhcacheUtil(path);
				}
			}
		}
		return ehCache;
	}

	public void put(String cacheName, String key, Object value) {
		put(cacheName, key, value, -1, -1, false);
	}

	public void putEternal(String cacheName, String key, Object value) {
		put(cacheName, key, value, -1, -1, true);
	}

	public void put(String cacheName, String key, Object value, int timeout) {
		put(cacheName, key, value, timeout, -1, false);
	}

	/**
	 * put方法
	 * @param cacheName  cache名
	 * @param key		 缓存key
	 * @param value		 缓存对象
	 * @param timeToIdle 空闲过期时间，即最后一次访问后过期时间
	 * @param timeToLive 存活时间，即创建后过期时间
	 * @
	 */
	public void put(String cacheName, String key, Object value, int timeToIdle, int timeToLive, boolean eternal) {
		Cache cache = manager.getCache(cacheName);
		Element element = new Element(key, value);
		if(!eternal){
			if(timeToIdle >= 0){
				element.setTimeToIdle(timeToIdle);
			}

			if(timeToLive >= 0){
				element.setTimeToLive(timeToLive);
			}
		}else {
			element.setEternal(eternal);
		}

		cache.put(element);
	}

	public Object get(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		Element element = cache.get(key);
		return element == null ? null : element.getObjectValue();
	}

	public Cache get(String cacheName) {
		return manager.getCache(cacheName);
	}

	public void remove(String cacheName, String key) {
		Cache cache = manager.getCache(cacheName);
		cache.remove(key);
	}
}
