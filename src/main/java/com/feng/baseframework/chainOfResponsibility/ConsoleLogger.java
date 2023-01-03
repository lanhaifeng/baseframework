package com.feng.baseframework.chainOfResponsibility;

/**
 * baseframework
 * 2020/11/10 16:09
 * 控制台输出
 *
 * @author lanhaifeng
 * @since
 **/
public class ConsoleLogger extends AbstractLogger {

	public ConsoleLogger(int level){
		this.level = level;
	}

	@Override
	public void write(String message) {
		System.out.println("Standard Console::Logger: " + message);
		System.out.println("");
	}
}
