package com.feng.baseframework.job;

import com.feng.baseframework.autoconfig.QuartzProperties;
import com.feng.baseframework.util.JacksonUtil;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * 从配置初始化job
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 1:03创建:JobConfigInitialization
 * @since 1.0
 */
@Component
public class JobConfigInitialization implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(JobConfigInitialization.class);
    private final SchedulerManager schedulerManager;
    private final QuartzProperties quartzProperties;
    private final Scheduler scheduler;
    private final ApplicationContext applicationContext;

    public JobConfigInitialization(SchedulerManager schedulerManager, QuartzProperties quartzProperties, Scheduler scheduler, ApplicationContext applicationContext) {
        this.schedulerManager = schedulerManager;
        this.quartzProperties = quartzProperties;
        this.scheduler = scheduler;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional.ofNullable(quartzProperties.getJobs()).ifPresent(
                j -> j.stream().filter(SchedulerJob::validate).forEach(schedulerManager::activeJob));

        for (String jobGroupName : scheduler.getJobGroupNames()) {
            try {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroupName))) {
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
                    String param = Optional.ofNullable(
                            jobDetail.getJobDataMap()).map(j -> j.getString(SchedulerJob.JOB_DATA_VO_KEY)).orElse(null);
                    JobDataVo jobDataVo = StringUtils.hasText(param) ? JacksonUtil.json2pojo(param, JobDataVo.class) : null;
                    if(Objects.nonNull(jobDataVo)) {
                        if(!jobDataVo.validate() || !validateBean(jobDataVo)) {
                            if(!triggerState.equals(Trigger.TriggerState.PAUSED)) {
                                scheduler.pauseJob(jobKey);
                            }
                        } else {
                            if(triggerState.equals(Trigger.TriggerState.PAUSED)) {
                                scheduler.resumeJob(jobKey);
                            }
                        }
                    } else {
                        if(triggerState.equals(Trigger.TriggerState.PAUSED)) {
                            scheduler.resumeJob(jobKey);
                        }
                    }
                }
            } catch (SchedulerException e) {
                logger.error("初始化job失败，错误：", e);
            }
        }
    }

    public boolean validateBean(JobDataVo jobDataVo) {
        Object obj = null;
        try {
            if(StringUtils.hasText(jobDataVo.getBeanName()) && StringUtils.hasText(jobDataVo.getBeanCls())) {
                obj = applicationContext.getBean(jobDataVo.getBeanName(), Class.forName(jobDataVo.getBeanCls()));
            }
            if(Objects.isNull(obj) && StringUtils.hasText(jobDataVo.getBeanName())) {
                obj = applicationContext.getBean(jobDataVo.getBeanName());
            }
            if(Objects.isNull(obj) && StringUtils.hasText(jobDataVo.getBeanCls())) {
                obj = applicationContext.getBean(Class.forName(jobDataVo.getBeanCls()));
            }
            return Objects.nonNull(obj) && Objects.nonNull(ReflectionUtils.findMethod(obj.getClass(), jobDataVo.getMethodName()));
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
