package com.feng.baseframework.jdk.lang;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * baseframework
 * 2022/3/5 20:23
 * 描述一下类的用途
 *
 * @author lanhaifeng
 * @since
 **/
public class IntegerTest {

    @Test
    public void testIntegerCache() throws NoSuchFieldException, IllegalAccessException {
        Class<?> cache = Integer.class.getDeclaredClasses()[0];
        Field myCache = cache.getDeclaredField("cache");
        myCache.setAccessible(true);

        //127到-128
        Integer[] newCache = (Integer[])myCache.get(cache);
        newCache[132] = newCache[133];

        int a = 2;
        int b = a + a;
        System.out.printf("%d + %d = %d \n", a, a, b);

        // Integer c相当于Integer.valueOf(100)
        // 而100在127到-128之间，会返回IntegerCache.cache[100 + (-IntegerCache.low)]
        // 因此c,d等等时相同
        // 这么设计的原因是小整数比大整数使用率高，使用相同的底层对象是有价值的，可以减少潜在的内存占用
        Integer c = 100, d = 100;
        System.out.println(c == d);

        c = 200;
        d = 200;
        System.out.println(c == d);
    }
}
