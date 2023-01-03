package com.feng.baseframework.chainOfResponsibility;

/**
 * baseframework
 * 2020/11/10 16:12
 * file输出
 *
 * @author lanhaifeng
 * @since
 **/
public class FileLogger extends AbstractLogger {

	public FileLogger(int level){
		this.level = level;
	}

	@Override
	public void write(String message) {
		System.out.println("File::Logger: " + message);
		System.out.println("");
	}
}