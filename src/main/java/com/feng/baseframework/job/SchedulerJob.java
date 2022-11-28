package com.feng.baseframework.job;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * job信息
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:33创建:SchedulerJob
 * @since 1.0
 */
@Getter
@Setter
public class SchedulerJob implements Serializable {



    /**
     * job执行参数键名
     */
    public static final String JOB_DATA_VO_KEY = "JOB_DATA_VO_KEY";

    /**
     * 名字
     */
    private String name;
    /**
     * 分组
     */
    private String group;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 工作类
     */
    private String jobClass;
    /**
     * 描述信息
     */
    private String desc;
    /**
     * 间隔时长
     */
    private Integer interval;

    /**
     * job参数
     */
    private JobDataVo jobDataVo;

    /**
     * 非空判断
     */
    private Predicate<String> hasText = StringUtils::isNotBlank;

    public boolean validate() {
        return hasText.test(name) && hasText.test(group) && hasText.test(jobClass)
                && hasText.test(desc) && (hasText.test(cron) || Objects.nonNull(interval));
    }

    public boolean isCron() {
        return hasText.test(cron);
    }

    public boolean isInterval() {
        return Objects.nonNull(interval);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SchedulerJob that = (SchedulerJob) o;

        return new EqualsBuilder().append(name, that.name).append(group, that.group).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(group).toHashCode();
    }
}

