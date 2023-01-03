package com.feng.baseframework.interceptor;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * baseframework
 * 2019/10/15 18:46
 * 自定义方法拦截器实现aop
 *
 * @author lanhaifeng
 * @since
 **/
public class MyMethodInterceptor extends AbstractMethodInterceptor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void before(MethodInvocation methodInvocation){
		String methodName = methodInvocation.getMethod().getName();
		logger.info("我是方法拦截器：在" + methodName + "方法调用前被调用");
	}

	public void after(MethodInvocation methodInvocation){
		String methodName = methodInvocation.getMethod().getName();
		logger.info("我是方法拦截器：在" + methodName + "方法调用前被调用");
	}

}
