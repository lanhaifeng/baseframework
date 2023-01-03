package com.feng.baseframework.annotation;

import java.lang.annotation.*;

/**
 *
 * 2018/9/18 15:43
 * 用于AOP方法统计时间的注解
 *
 *
 * @author lanhaifeng
 * @since
 **/
@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时依然存在
@Target(ElementType.METHOD)
@Documented
public @interface MethodTimeAop {
}
