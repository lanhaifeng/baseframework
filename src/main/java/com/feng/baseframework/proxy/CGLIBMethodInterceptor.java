package com.feng.baseframework.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * baseframework
 * 2020/11/3 10:09
 * CGLIB方法拦截器
 *
 * @author lanhaifeng
 * @since
 **/
public class CGLIBMethodInterceptor implements MethodInterceptor {

	private static Logger logger = LoggerFactory.getLogger(CGLIBMethodInterceptor.class);

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		try {
			logger.info("CGLIB proxy before");
			Object object = methodProxy.invokeSuper(o, objects);
			logger.info("CGLIB proxy after");
			return object;
		} finally {
			logger.info("CGLIB proxy after return");
		}
	}
}
