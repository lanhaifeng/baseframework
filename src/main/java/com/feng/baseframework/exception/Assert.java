package com.feng.baseframework.exception;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * baseframework
 * 2022/7/13 23:03
 * 断言类
 *
 * @author lanhaifeng
 * @since
 **/
public class Assert {
    private Assert() {
    }

    public static <E extends RuntimeException> void state(boolean asserted, Supplier<E> exSupplier) {
        // 断言未通过
        if (!asserted) throw exSupplier.get();
    }

    public static <T, E extends RuntimeException> void isTrue(T t, Predicate<T> verifier, Supplier<E> exSupplier) {
        // 断言结果
        boolean asserted = verifier.test(t);
        // 断言未通过
        if (!asserted) throw exSupplier.get();
    }

    public static <T, E extends RuntimeException> void isTrue(T caller, T reference, BiPredicate<T, T> verifier, Supplier<E> exSupplier) {
        // 断言结果
        boolean asserted = verifier.test(caller, reference);
        // 断言未通过
        if (!asserted) throw exSupplier.get();
    }

    // 对象为空
    public static <T> Boolean isNull(T t) {
        return Objects.isNull(t);
    }

    // 对象非空
    public static <T> Boolean isNotNull(T t) {
        return !isNull(t);
    }

    // 比较后相等
    public static <T extends Comparable<T>> Boolean isEqual(T caller, T reference) {
        return caller.compareTo(reference) == 0;
    }

    // 比较后大于
    public static <T extends Comparable<T>> Boolean isGt(T caller, T reference) {
        return caller.compareTo(reference) == 1;
    }

    // 比较后大于等于
    public static <T extends Comparable<T>> Boolean isGe(T caller, T reference) {
        return caller.compareTo(reference) >= 0;
    }

    // 比较后小于
    public static <T extends Comparable<T>> Boolean isLt(T caller, T reference) {
        return caller.compareTo(reference) < 0;
    }

    // 比较后小于等于
    public static <T extends Comparable<T>> Boolean isLe(T caller, T reference) {
        return caller.compareTo(reference) <= 0;
    }
}
