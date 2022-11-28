package com.feng.baseframework.job;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.Assert;
import com.feng.baseframework.exception.BusinessException;
import com.feng.baseframework.util.JacksonUtil;
import org.quartz.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * job调度器
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:32创建:SchedulerManager
 * @since 1.0
 */
public interface SchedulerManager {
    /**
     * 激活任务
     *
     * @param schedulerJob      任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回激活任务结果
     */
    boolean activeJob(SchedulerJob schedulerJob);

    /**
     * 暂停任务
     *
     * @param jobKey      job信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回暂停任务结果
     */
    boolean pauseJob(JobKey jobKey);

    /**
     * 恢复任务
     *
     * @param  jobKey      job信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回恢复任务结果
     */
    boolean resumeJob(JobKey jobKey);

    /**
     * 创建任务
     *
     * @param schedulerJob      任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回创建任务结果
     */
    boolean createJob(SchedulerJob schedulerJob);

    /**
     * 更新任务
     *
     * @param schedulerJob      任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回跟新任务结果
     */
    boolean updateJob(SchedulerJob schedulerJob);

    /**
     * 删除任务
     *
     * @param jobKeys           任务键值集合
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回删除任务结果
     */
    boolean deleteJobs(List<JobKey> jobKeys);

    /**
     * 删除任务
     *
     * @param jobKey           任务键值
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回删除任务结果
     */
    boolean deleteJob(JobKey jobKey);

    /**
     * 返回job执行细节
     *
     * @param schedulerJob 任务信息
     * @param clazz        执行类名
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回job执行细节
     */
    default JobDetail getJobDetail(SchedulerJob schedulerJob, Class<Job> clazz) {
        JobDataMap jobDataMap = new JobDataMap();
        Optional.ofNullable(schedulerJob.getJobDataVo()).ifPresent(
                j -> jobDataMap.put(SchedulerJob.JOB_DATA_VO_KEY, JacksonUtil.bean2Json(j)));
        return JobBuilder.newJob()
                .ofType(clazz)
                .withIdentity(schedulerJob.getName(), schedulerJob.getGroup())
                .withDescription(schedulerJob.getDesc())
                .usingJobData(jobDataMap)
                .build();
    }

    /**
     * 返回触发器
     *
     * @param schedulerJob 任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回触发器
     */
    default Trigger getCronTrigger(SchedulerJob schedulerJob) {
        ScheduleBuilder<? extends Trigger> scheduleBuilder = null;
        if (schedulerJob.isInterval()) {
            scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                    .withInterval(schedulerJob.getInterval(), DateBuilder.IntervalUnit.SECOND)
                    .withMisfireHandlingInstructionIgnoreMisfires();
        }
        if (schedulerJob.isCron()) {
            scheduleBuilder = CronScheduleBuilder.cronSchedule(schedulerJob.getCron())
                    //任务错过执行策略，以错过的第一个频率时间立刻开始执行，重做错过的所有频率周期后，当下一次触发频率发生时间大于当前时间后，再按照正常的Cron频率依次执行
                    .withMisfireHandlingInstructionIgnoreMisfires();
        }
        Assert.state(Objects.nonNull(scheduleBuilder), BusinessException::new, ResultEnum.UNKONW_ERROR);
        return TriggerBuilder.newTrigger()
                .withIdentity(schedulerJob.getName(), schedulerJob.getGroup())
                .withDescription(schedulerJob.getDesc())
                .withSchedule(scheduleBuilder)
                .build();
    }

    /**
     * 构建job键值
     *
     * @param schedulerJobs     任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回job键值
     */
    default List<JobKey> buildJobKeys(SchedulerJob ...schedulerJobs) {
        Set<SchedulerJob> schedulerJobSet = new HashSet<>();
        Optional.ofNullable(schedulerJobs).ifPresent(jobs -> Arrays.stream(jobs).filter(SchedulerJob::validate).forEach(schedulerJobSet::add));
        Assert.state(!schedulerJobSet.isEmpty(), BusinessException::new, ResultEnum.PARAM_ILLEGAL_ERROR);
        return schedulerJobSet.stream().map(j -> JobKey.jobKey(j.getName(), j.getGroup())).collect(Collectors.toList());
    }

    /**
     * 构建job键值
     *
     * @param schedulerJob     任务信息
     * @author lanhaifeng
     * @since 1.6.0
     * @return 返回job键值
     */
    default JobKey buildJobKey(SchedulerJob schedulerJob) {
        Assert.state(Objects.nonNull(schedulerJob) && schedulerJob.validate(), BusinessException::new, ResultEnum.PARAM_ILLEGAL_ERROR);
        return JobKey.jobKey(schedulerJob.getName(), schedulerJob.getGroup());
    }

}
