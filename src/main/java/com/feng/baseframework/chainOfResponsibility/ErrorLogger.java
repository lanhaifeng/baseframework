package com.feng.baseframework.chainOfResponsibility;

/**
 * baseframework
 * 2020/11/10 16:11
 * error输出
 *
 * @author lanhaifeng
 * @since
 **/
public class ErrorLogger extends AbstractLogger {

	public ErrorLogger(int level){
		this.level = level;
	}

	@Override
	public void write(String message) {
		System.out.println("Error Console::Logger: " + message);
		System.out.println("");
	}
}
