package com.feng.baseframework.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * job调度器实现
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:52创建:SchedulerManagerImpl
 * @since 1.0
 */
@Component
public class SchedulerManagerImpl implements SchedulerManager {

    private final static Logger logger = LoggerFactory.getLogger(SchedulerManager.class);
    private final Scheduler scheduler;

    public SchedulerManagerImpl(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public boolean activeJob(SchedulerJob schedulerJob) {
        JobKey jobKey = JobKey.jobKey(schedulerJob.getName(), schedulerJob.getGroup());
        try {
            if (scheduler.checkExists(jobKey)) {
                return updateJob(schedulerJob);
            }else {
                return createJob(schedulerJob);
            }
        } catch (SchedulerException e) {
            logger.error("activeJob error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean createJob(SchedulerJob schedulerJob) {
        JobKey jobKey = JobKey.jobKey(schedulerJob.getName(), schedulerJob.getGroup());
        try {
            if (scheduler.checkExists(jobKey)) {
                return Boolean.TRUE;
            }
            Class<?> clazz = Class.forName(schedulerJob.getJobClass());
            JobDetail jobDetail = getJobDetail(schedulerJob, (Class<Job>) clazz);
            Trigger cronTrigger = getCronTrigger(schedulerJob);
            //加入调度器
            scheduler.scheduleJob(jobDetail, cronTrigger);
            return Boolean.TRUE;
        } catch (ClassNotFoundException | SchedulerException e) {
            logger.error("createJob error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean updateJob(SchedulerJob schedulerJob) {
        TriggerKey triggerKey = TriggerKey.triggerKey(schedulerJob.getName(), schedulerJob.getGroup());
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return Boolean.FALSE;
            }
            //查询cron
            String oldCron = ((CronTrigger)trigger).getCronExpression();
            //没有变化则返回
            if (oldCron.equals(schedulerJob.getCron())){
                return Boolean.TRUE;
            }
            Trigger cronTrigger = getCronTrigger(schedulerJob);
            //加入调度器
            scheduler.rescheduleJob(triggerKey, cronTrigger);
            return Boolean.TRUE;
        } catch (SchedulerException e) {
            logger.error("updateJob error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean pauseJob(JobKey jobKey) {
        try {
            scheduler.pauseJob(jobKey);
            return Boolean.TRUE;
        } catch (SchedulerException e) {
            logger.error("pauseJob error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean resumeJob(JobKey jobKey) {
        try {
            scheduler.resumeJob(jobKey);
            return Boolean.TRUE;
        } catch (SchedulerException e) {
            logger.error("pauseJob error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean deleteJobs(List<JobKey> jobKeys) {
        try {
            return scheduler.deleteJobs(jobKeys);
        } catch (SchedulerException e) {
            logger.error("deleteJobs error:", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public boolean deleteJob(JobKey jobKey) {
        try {
            return scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("deleteJobs error:", e);
            return Boolean.FALSE;
        }
    }

}
