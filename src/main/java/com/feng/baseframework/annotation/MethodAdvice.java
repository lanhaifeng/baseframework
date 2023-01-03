package com.feng.baseframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 切面方法控制类，控制方法是前切、后切、还是环绕
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodAdvice {

	boolean before() default true;

	boolean after() default false;
}
