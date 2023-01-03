package com.feng.baseframework.chainOfResponsibility;

/**
 * baseframework
 * 2020/11/10 16:18
 * 记录器
 *
 * @author lanhaifeng
 * @since
 **/
public interface Logger {
	void write(String message);

	void logMessage(int level, String message);
}
