package com.feng.baseframework.job;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;

/**
 * bean后置处理器初始job
 *
 * @author lanhaifeng
 * @apiNote 时间:2022/11/26 1:01创建:JobInitBeanPostProcessor
 * @since 1.0
 * @version 1.0
 */
@Component
public class JobInitBeanPostProcessor implements BeanPostProcessor {

    private final SchedulerManager schedulerManager;
    private final ConfigurableBeanFactory configurableBeanFactory;

    public JobInitBeanPostProcessor(SchedulerManager schedulerManager, ConfigurableBeanFactory configurableBeanFactory) {
        this.schedulerManager = schedulerManager;
        this.configurableBeanFactory = configurableBeanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    @Nullable
    public Object postProcessAfterInitialization(Object bean, @Nonnull String beanName) throws BeansException {
        Map<Method, JobScheduler> methods = MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<JobScheduler>) method ->
                AnnotationUtils.findAnnotation(method, JobScheduler.class)
        );
        methods.forEach((key, value) -> {
            SchedulerJob schedulerJob = new SchedulerJob();
            JobDataVo jobDataVo = new JobDataVo();
            jobDataVo.setBeanCls(AopUtils.getTargetClass(bean).getName());
            jobDataVo.setBeanName(beanName);
            jobDataVo.setMethodName(key.getName());

            schedulerJob.setJobDataVo(jobDataVo);
            schedulerJob.setDesc(value.desc());
            schedulerJob.setCron(Optional.ofNullable(value.cron()).map(configurableBeanFactory::resolveEmbeddedValue).orElse(""));
            schedulerJob.setGroup(beanName);
            schedulerJob.setName(key.getName());
            schedulerJob.setJobClass(value.disallowConcurrent() ? DisallowConcurrentJob.class.getName() : ConcurrentJob.class.getName());
            schedulerManager.activeJob(schedulerJob);
        });
        return bean;
    }

}
