package com.feng.baseframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 插入式注解
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/1/4 18:16创建:Setter
 * @since v2.0.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface Setter {
}
