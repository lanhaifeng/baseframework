package com.feng.baseframework.util;

import com.feng.baseframework.constant.LogType;

/**
 * baseframework
 * 2020/9/4 14:32
 * 获取运行时信息工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class RuntimeInfoUtil {

	public static StackTraceElement getStackTraceElement(String className){
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		for (StackTraceElement stackTrace : stackTraces) {
			if(stackTrace.getClassName().equals(className)){
				return stackTrace;
			}
		}
		return null;
	}

	public static StackTraceElement getLogStackTraceElement(LogType logType){
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		int found = -1;
		//查找真实日志类调用的序号，既logType
		for (int i = 0; i < stackTraces.length; i++) {
			if(stackTraces[i].getClassName().equals(logType.getClassName())){
				found = i;
				break;
			}
		}
		//第一个调用logType的类，既className不等于logType对应类，就是调用日志打印的类
		for (int i = found + 1; i < stackTraces.length; i++) {
			if(!stackTraces[i].getClassName().equals(logType.getClassName())){
				found = i;
				break;
			}
		}
		return found != -1 ? stackTraces[found] : null;
	}
}
