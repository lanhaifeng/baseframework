package com.feng.baseframework.annotation;

import com.feng.baseframework.condition.ProfileCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(ProfileCondition.class)
@Inherited
public @interface CustomOnProfileCondition {

	/**
	 * 模式
	 * @return
	 */
	String value() default "";

	/**
	 * 模式
	 * @return
	 */
	String[] profiles() default {};
}
