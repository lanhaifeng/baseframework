package com.feng.baseframework.jdk8.lambda;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @ProjectName: baseframework
 * @Description: 函数式接口Function测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:44
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:44
 * @UpdateRemark:
 * @Version: 1.0
 */
public class FunctionTest {
    /**
     * 方法引用
     * 1.构造器的引用：Supplier<Date> supplier = Date::new;
     * 2.静态方法的引用：Supplier<LocalDate> supplier = LocalDate::now;
     * 3.特定类任意对象的方法引用：
     *  3.1无参数Function<LocalDate, Integer> function = LocalDate::getYear;
     *      相当于LocalDate的实例调用getYear方法返回值类型为Integer
     *  3.2有一个参数BiFunction<LocalDate, DateTimeFormatter, String> biFunction = LocalDate::format;
     *      相当于LocalDate的实例调用format参数为DateTimeFormatter实例，返回值类型为String
     * 4.特定对象的方法引用
     *  LocalDate endTime = LocalDate.now();
     *  4.1无参数Supplier<Integer> function = endTime::getYear;
     *      相当于endTime.getYear()
     *  4.2有一个参数Function<DateTimeFormatter, String> function = endTime::format;
     *      相当于endTime.format(DateTimeFormatter)
     */

    @Test
    public void testConstruct() {
        Supplier<Date> supplier = Date::new;
        System.out.println(supplier.get().getTime());
    }

    @Test
    public void testStatic() {
        Supplier<LocalDate> supplier = LocalDate::now;
        System.out.println(supplier.get().getYear());
    }

    @Test
    public void testAnyObjectReference1() {
        Function<LocalDate, Integer> function = LocalDate::getYear;
        System.out.println(function.apply(LocalDate.now()));
    }

    @Test
    public void testAnyObjectReference2() {
        BiFunction<LocalDate, DateTimeFormatter, String> biFunction = LocalDate::format;
        System.out.println(biFunction.apply(LocalDate.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    @Test
    public void testSpecialObjectReference1() {
        LocalDate endTime = LocalDate.now();
        Supplier<Integer> function = endTime::getYear;
        System.out.println(function.get());
    }

    @Test
    public void testSpecialObjectReference2() {
        LocalDate endTime = LocalDate.now();
        Function<DateTimeFormatter, String> function = endTime::format;
        System.out.println(endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
