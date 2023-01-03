package com.feng.baseframework.chainOfResponsibility;

import java.util.Objects;

/**
 * baseframework
 * 2020/11/10 16:13
 * 日志链，基于单向链
 *
 * @author lanhaifeng
 * @since
 **/
public class LoggerChain {

	private AbstractLogger root;

	public LoggerChain setRootLogger(AbstractLogger logger) {
		this.root = logger;
		return this;
	}

	public LoggerChain setNextLogger(AbstractLogger logger) {
		if(Objects.nonNull(root)){
			AbstractLogger next = root.nextLogger;
			AbstractLogger target = root;
			while (Objects.nonNull(next)){
				target = next;
				next = next.nextLogger;
			}
			target.setNextLogger(logger);
		}
		return this;
	}

	public void logMessage(int level, String message) {
		root.logMessage(level, message);
	}
}
