package com.feng.baseframework.annotation;

import java.lang.annotation.*;

/**
 * 切面标注类，用于标注需要切入的类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface ClassLevelAdviceTag {

	Class value() default Object.class;
}
