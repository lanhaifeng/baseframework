package com.feng.baseframework.model;

import java.io.Serializable;
import java.util.Optional;

/**
 * ds-spring-boot
 * 2021/5/13 10:50
 * 进程执行结果
 *
 * @author lanhaifeng
 * @since
 **/
public class ProcessResult implements Serializable {

	public static final int EXIT_VALUE_SUCCESS = 0;

	//进程退出值
	private Integer exitValue;
	//进程返回值
	private String result;
	//进程错误输出
	private String errorMessage;

	public boolean success(){
		return Optional.ofNullable(getExitValue()).orElse(-1).equals(EXIT_VALUE_SUCCESS);
	}

	public Integer getExitValue() {
		return exitValue;
	}

	public void setExitValue(Integer exitValue) {
		this.exitValue = exitValue;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
