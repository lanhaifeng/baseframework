package com.feng.baseframework.chainOfResponsibility;

/**
 * baseframework
 * 2020/11/10 15:45
 * 抽象的记录器类
 *
 * @author lanhaifeng
 * @since
 **/
public abstract class AbstractLogger implements Logger {

	public static int INFO = 1;
	public static int DEBUG = 2;
	public static int ERROR = 3;

	protected int level;

	//责任链中的下一个元素
	protected AbstractLogger nextLogger;

	public void setNextLogger(AbstractLogger nextLogger){
		this.nextLogger = nextLogger;
	}

	@Override
	public void logMessage(int level, String message){
		if(this.level <= level){
			write(message);
		}
		if(nextLogger !=null){
			nextLogger.logMessage(level, message);
		}
	}
}
