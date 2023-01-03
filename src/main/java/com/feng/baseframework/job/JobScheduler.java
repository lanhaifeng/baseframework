package com.feng.baseframework.job;

import java.lang.annotation.*;

/**
 * 标识quartz任务注解
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:54创建:JobScheduler
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobScheduler {

    /**
     * 禁止并发执行，默认不禁止
     * @return 返回是否禁用并发执行
     */
    boolean disallowConcurrent() default true;

    /**
     * cron表达式
     * @return  返回cron表达式
     */
    String cron() default "";

    /**
     * 描述信息
     * @return  返回任务描述信息
     */
    String desc();

    /**
     * 任务执行间隔时间
     * @return  返回任务执行间隔时间
     */
    long fixedDelay() default -1;

}
