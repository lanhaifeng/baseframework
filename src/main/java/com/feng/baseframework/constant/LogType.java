package com.feng.baseframework.constant;

import java.util.logging.Logger;

/**
 * baseframework
 * 2020/9/4 16:02
 * log类型枚举
 *
 * @author lanhaifeng
 * @since
 **/
public enum LogType {
	JUL(Logger.class.getName(), "java util log"),
	JCL("org.apache.commons.logging.log", "java common log"),
	LOG4J("org.apache.log4j.Logger", "log4j log"),
	;

	private String className;
	private String desc;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	LogType(String className, String desc) {
		this.className = className;
		this.desc = desc;
	}
}
