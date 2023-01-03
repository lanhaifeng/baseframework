package com.feng.baseframework.interceptor;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.annotation.MethodAdvice;
import io.jsonwebtoken.lang.Assert;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * baseframework
 * 2019/10/17 17:22
 * 自定义方法拦截器抽象类
 *
 * @author lanhaifeng
 * @since
 **/
public abstract class AbstractMethodInterceptor implements MethodInterceptor {

	protected boolean isInterceptor(MethodInvocation methodInvocation){
		boolean isInterceptor = false;
		Class sourceClass = methodInvocation.getMethod().getDeclaringClass();
		Annotation methodAnnotation = methodInvocation.getMethod().getAnnotation(ClassLevelAdviceTag.class);
		Annotation classAnnotation = sourceClass.getAnnotation(ClassLevelAdviceTag.class);
		if(methodAnnotation == null && classAnnotation == null){
		    return isInterceptor;
        }
        Class interceptorHandler =   ((ClassLevelAdviceTag)Optional.ofNullable(methodAnnotation).orElse(classAnnotation)).value();

		Assert.notNull(interceptorHandler, "切点拦截处理器不能为空");
		if(Object.class.equals(interceptorHandler) || AbstractMethodInterceptor.class.isAssignableFrom(interceptorHandler)){
			isInterceptor = true;
		}

		return isInterceptor;
	}

	protected abstract void before(MethodInvocation methodInvocation);

	protected abstract void after(MethodInvocation methodInvocation);

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		if(!isInterceptor(methodInvocation)) return methodInvocation.proceed();
		Object result = null;
		MethodAdvice methodAdvice = getMethodAdvice(methodInvocation);
		if(methodAdvice == null){
			before(methodInvocation);
			result = methodInvocation.proceed();
			after(methodInvocation);
		}else {
			if(methodAdvice.before()){
				before(methodInvocation);
			}
			result = methodInvocation.proceed();
			if(methodAdvice.after()){
				after(methodInvocation);
			}
		}

		return result;
	}

	protected MethodAdvice getMethodAdvice(MethodInvocation methodInvocation){
		return methodInvocation.getMethod().getAnnotation(MethodAdvice.class);
	}
}
