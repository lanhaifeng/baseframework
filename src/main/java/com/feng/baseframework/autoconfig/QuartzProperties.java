package com.feng.baseframework.autoconfig;

import com.feng.baseframework.job.SchedulerJob;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * quartz配置类
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 1:07创建:QuartzProperties
 * @since 1.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "org.quartz.jobs")
public class QuartzProperties implements Serializable {

    /**
     * 调度任务信息
     */
    private List<SchedulerJob> jobs;

    public List<SchedulerJob> getJobs() {
        return jobs;
    }

    public void setJobs(List<SchedulerJob> jobs) {
        this.jobs = jobs;
    }

}
