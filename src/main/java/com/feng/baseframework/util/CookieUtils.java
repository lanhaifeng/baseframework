package com.feng.baseframework.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * baseframework
 * 2019/2/21 16:37
 * cookie工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class CookieUtils {

	private static final Log logger = LogFactory.getLog(CookieUtils.class);

	/**
	 * 获取所有的Cookie集合
	 * @return
	 */
	public static Cookie[] getCookies() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Cookie[] c = request.getCookies();
		return c;
	}

	/**
	 * 获取名为name的cookie值
	 * @param name
	 * @return
	 */
	public static String getCookie(String name) {
		try {
			Cookie[] cookies = getCookies();

			for (int i = 0; i < (cookies == null ? 0 : cookies.length); i++) {
				if ((name).equalsIgnoreCase(cookies[i].getName())) {
					return URLDecoder.decode(cookies[i].getValue(), "UTF-8");
				}
			}
		} catch (Exception e) {
			logger.error("获取cookie失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}

	/**
	 * 添加一个Cookie
	 * @param cookie
	 */
	public static void saveCookie(Cookie cookie) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attributes.getResponse();
		response.addCookie(cookie);
	}

	/**
	 * 添加cookie
	 *
	 * @param name
	 * @param jsonStr
	 */
	public static void addCookie(String name, String jsonStr) {
		try {
			String v = URLEncoder.encode(jsonStr, "UTF-8");

			Cookie cookie = new Cookie(name, v);
			cookie.setPath("/");
			cookie.setMaxAge(Integer.MAX_VALUE);// 设置保存cookie最大时长
			saveCookie(cookie);

		} catch (Exception e) {
			logger.error("添加cookie失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 删除cookie
	 *
	 * @param name
	 */
	public static void removeCookie(String name) {
		try {
			Cookie[] cookies = getCookies();

			for (int i = 0; i < (cookies == null ? 0 : cookies.length); i++) {
				if ((name).equalsIgnoreCase(cookies[i].getName())) {

					Cookie cookie = new Cookie(name, "");
					cookie.setPath("/");
					cookie.setMaxAge(0);// 设置保存cookie最大时长为0，即使其失效
					saveCookie(cookie);
				}
			}

		} catch (Exception e) {
			logger.error("删除cookie失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
	}
}
