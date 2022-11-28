package com.feng.baseframework.autoconfig;

import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.quartz.utils.PropertiesParser;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * quartz配置类
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/28 10:08创建:QuartzConfiguration
 * @since 1.0
 */
@Configuration
public class QuartzConfiguration implements ApplicationContextAware {

    private static final String YML_FILE_EXTENSION = ".yml";
    private static final String PROPERTIES_FILE_EXTENSION = ".properties";
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        String resource = "/application-quartz.yml";
        Properties properties = null;
        if(resource.endsWith(PROPERTIES_FILE_EXTENSION)) {
            //获取配置属性
            PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
            propertiesFactoryBean.setLocation(new ClassPathResource("/application-quartz.yml"));
            //在quartz.properties中的属性被读取并注入后再初始化对象
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        }
        if(resource.endsWith(YML_FILE_EXTENSION)) {
            //获取配置属性
            YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
            yamlPropertiesFactoryBean.setResources(new ClassPathResource("/application-quartz.yml"));
            yamlPropertiesFactoryBean.afterPropertiesSet();
            properties = yamlPropertiesFactoryBean.getObject();
        }

        //创建SchedulerFactoryBean
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setQuartzProperties(properties);
        //支持在JOB实例中注入其他的业务对象
        QuartzSpringBeanJobFactory quartzSpringBeanJobFactory = new QuartzSpringBeanJobFactory();
        quartzSpringBeanJobFactory.setApplicationContext(applicationContext);
        factory.setJobFactory(quartzSpringBeanJobFactory);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
        factory.setWaitForJobsToCompleteOnShutdown(true);
        //是否覆盖己存在的Job
        factory.setOverwriteExistingJobs(false);
        //QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动
        factory.setStartupDelay(10);

        return factory;
    }

    public static void main(String[] args) {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("/application-quartz.yml"));
        yamlPropertiesFactoryBean.afterPropertiesSet();
        Properties properties = yamlPropertiesFactoryBean.getObject();
        PropertiesParser cfg = new PropertiesParser(properties);
        System.out.println(properties);
        System.out.println(cfg.getPropertyGroup("org.quartz.threadPool"));
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     * @throws IOException  io异常
     * @return job调度类
     */
    @Bean(name = "scheduler")
    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws IOException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        return scheduler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static class QuartzSpringBeanJobFactory extends SpringBeanJobFactory {

        private AutowireCapableBeanFactory beanFactory;

        public void setApplicationContext(ApplicationContext context) {
            beanFactory = context.getAutowireCapableBeanFactory();
        }

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        }
    }
}
