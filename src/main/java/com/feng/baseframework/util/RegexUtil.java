package com.feng.baseframework.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * baseframework
 * 2020/6/6 9:29
 * 正则工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class RegexUtil {

	private static Map<String, Pattern> regexMap = new ConcurrentHashMap<>();
	private static Lock lock = new ReentrantLock();

	/**
	 * 2020/6/6 9:30
	 * 正则匹配
	 *
	 * @author lanhaifeng
	 **/
	public static boolean regexSql(String regex, String sql){
		return regexSql(regex, sql, false);
	}

	/**
	 * 2020/6/6 9:30
	 * 正则匹配
	 *
	 * @author lanhaifeng
	 **/
	public static boolean regexSql(String regex, String sql, boolean ignoreCase){
		Pattern pattern = regexMap.get(regex);
		if(pattern == null){
			lock.lock();
			try{
				if(pattern == null){
					if(ignoreCase){
						pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
					}else {
						pattern = Pattern.compile(regex);
					}
					regexMap.put(regex, pattern);
				}
			}finally {
				lock.unlock();
			}
		}
		Matcher matcher = pattern.matcher(sql);
		return matcher.matches();
	}

	/**
	 * 2020/6/6 9:30
	 * 清空正则缓存
	 *
	 * @author lanhaifeng
	 **/
	public static void clear(){
		regexMap.clear();
	}
}
