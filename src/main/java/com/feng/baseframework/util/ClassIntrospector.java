package com.feng.baseframework.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * baseframework
 * 2019/3/7 16:47
 * 使用unsafe计算对象的内存
 *
 * @author lanhaifeng
 * @since
 **/
public class ClassIntrospector {

	private static final Unsafe unsafe;
	/** Size of any Object reference */
	private static final int objectRefSize;

	static {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			unsafe = (Unsafe) field.get(null);

			objectRefSize = unsafe.arrayIndexScale(Object[].class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static int getObjectRefSize() {
		return objectRefSize;
	}
}
