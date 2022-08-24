package com.feng.baseframework.proxy;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2020/10/29 15:30
 * CGLIB动态代理
 *
 * @author lanhaifeng
 * @since
 **/
public class DynamicCGLIBProxy {
	
	private static Logger logger = LoggerFactory.getLogger(DynamicCGLIBProxy.class);

	@SuppressWarnings("unchecked")
	public static <T> T getProxyObject(Class targetCls, List<Callback> callbacks){
		if(Objects.isNull(targetCls) || Objects.isNull(callbacks))
			return null;
		if(callbacks.isEmpty()){
			try {
				return (T)targetCls.newInstance();
			} catch (Exception e) {
				logger.error("创建代理失败，错误：" + ExceptionUtils.getFullStackTrace(e));
				return null;
			}
		}
		//创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
		Enhancer enhancer = new Enhancer();
		//设置目标类的字节码文件
		enhancer.setSuperclass(targetCls);
		//设置回调函数
		enhancer.setCallbacks(callbacks.toArray(new Callback[callbacks.size()]));

		//这里的create方法就是正式创建代理类
		return (T)enhancer.create();
	}
}
