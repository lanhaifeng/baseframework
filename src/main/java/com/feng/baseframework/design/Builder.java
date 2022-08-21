package com.feng.baseframework.design;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * baseframework
 * 2022/8/21 12:11
 * 利用jdk8实现builder模式
 *
 * @author lanhaifeng
 * @since 1.0
 **/
public class Builder<T> {

    /**
     * 获取实例对象的方法引用
     */
    private final Supplier<T> instantiation;

    /**
     * 修改实例对象的方法引用
     */
    private final List<Consumer<T>> modifiers = new ArrayList<>();

    private Builder(Supplier<T> instantiation) {
        this.instantiation = instantiation;
    }

    /**
     *
     * 2022/8/21 12:55
     * 静态方法，接收实例对象方法引用，构建一个Builder实例
     *
     * @author lanhaifeng
     * @since 1.0
     **/
    public static <T> Builder<T> of(Supplier<T> instantiation) {
        return new Builder<>(instantiation);
    }

    /**
     *
     * 2022/8/21 12:55
     * 执行构建，返回构建后的实例对象
     *
     * @author lanhaifeng
     * @since 1.0
     **/
    public T build() {
        T value = instantiation.get();
        modifiers.forEach(modify -> modify.accept(value));
        return value;
    }

    /**
     *
     * 2022/8/21 12:56
     * 传一个参数的修改方法引用及参数
     *
     * @author lanhaifeng
     * @since 1.0
     **/
    public <P> Builder<T> with(Modifier<T, P> modifier, P p) {
        Consumer<T> modifierConsumer = instance -> modifier.accept(instance, p);
        modifiers.add(modifierConsumer);

        return this;
    }

    /**
     *
     * 2022/8/21 12:56
     * 传两个参数的修改方法引用及参数
     *
     * @author lanhaifeng
     * @since 1.0
     **/
    public <P1, P2> Builder<T> with(BinaryModifier<T, P1, P2> modifier, P1 p1, P2 p2) {
        Consumer<T> modifierConsumer = instance -> modifier.accept(instance, p1, p2);
        modifiers.add(modifierConsumer);

        return this;
    }

    /**
     *
     * 2022/8/21 12:56
     * 传三个参数的修改方法引用及参数
     *
     * @author lanhaifeng
     * @since 1.0
     **/
    public <P1, P2, P3> Builder<T> with(TripleModifier<T, P1, P2, P3> modifier, P1 p1, P2 p2, P3 p3) {
        Consumer<T> modifierConsumer = instance -> modifier.accept(instance, p1, p2, p3);
        modifiers.add(modifierConsumer);

        return this;
    }

    /**
     * 单一参数的修改器
     * @param <T>   实例对象
     * @param <P>   修改器参数类型
     */
    @FunctionalInterface
    public interface Modifier<T, P> {

        /**
         *
         * 2022/8/21 12:58
         * 接收两个参数，实体对象T，参数P，使用参数P修改对象T的属性
         *
         *
         * @author lanhaifeng
         * @since 1.0
         **/
        void accept(T t, P p);
    }

    /**
     * 两个参数的修改器
     * @param <T>   实例对象
     * @param <P1>  修改器参数类型
     * @param <P2>  修改器参数类型
     */
    @FunctionalInterface
    public interface BinaryModifier<T, P1, P2> {

        /**
         *
         * 2022/8/21 12:58
         * 接收三个参数，实体对象T，参数P1、P2，使用参数P修改对象T的属性
         *
         *
         * @author lanhaifeng
         * @since 1.0
         **/
        void accept(T t, P1 p1, P2 p2);
    }

    /**
     * 三个参数的修改器
     * @param <T>   实例对象
     * @param <P1>  修改器参数类型
     * @param <P2>  修改器参数类型
     * @param <P3>  修改器参数类型
     */
    @FunctionalInterface
    public interface TripleModifier<T, P1, P2, P3> {

        /**
         *
         * 2022/8/21 12:58
         * 接收四个参数，实体对象T，参数P1、P2、P3，使用参数P修改对象T的属性
         *
         *
         * @author lanhaifeng
         * @since 1.0
         **/
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }
}
