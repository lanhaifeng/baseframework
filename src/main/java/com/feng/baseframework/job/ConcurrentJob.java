package com.feng.baseframework.job;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.Assert;
import com.feng.baseframework.exception.BusinessException;
import com.feng.baseframework.util.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * 允许并发执行job
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:56创建:ConcurrentJob
 * @since 1.0
 */
public class ConcurrentJob implements Job {

    private final static Logger logger = LoggerFactory.getLogger(ConcurrentJob.class);

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String param = Optional.ofNullable(jobDataMap).map(j -> j.getString(SchedulerJob.JOB_DATA_VO_KEY)).orElse(null);
        Assert.state(StringUtils.isNotBlank(param), BusinessException::new, ResultEnum.PARAM_ILLEGAL_ERROR);
        JobDataVo jobDataVo = JacksonUtil.json2pojo(param, JobDataVo.class);

        Object executeBean = null;

        if (StringUtils.isNotBlank(jobDataVo.getBeanName()) && Objects.nonNull(jobDataVo.getBeanCls())) {
            Class<?> cls = null;
            try {
                cls = ClassUtils.forName(jobDataVo.getBeanCls(), (ClassLoader)null);
            } catch (ClassNotFoundException e) {
                throw new BusinessException(ResultEnum.ILLEGAL_JOB_ERROR);
            }
            executeBean = applicationContext.getBean(jobDataVo.getBeanName(), cls);
        }
        if(Objects.isNull(executeBean) && StringUtils.isNotBlank(jobDataVo.getBeanName())) {
            executeBean = applicationContext.getBean(jobDataVo.getBeanName());
        }
        if(Objects.isNull(executeBean) && Objects.nonNull(jobDataVo.getBeanCls())) {
            executeBean = applicationContext.getBean(jobDataVo.getBeanCls());
        }
        Assert.state(Objects.nonNull(executeBean) && jobDataVo.validate(), BusinessException::new, ResultEnum.ILLEGAL_JOB_ERROR);
        Method executeMethod = ReflectionUtils.findMethod(executeBean.getClass(), jobDataVo.getMethodName());

        Assert.state(Objects.nonNull(executeMethod) && jobDataVo.validate(), BusinessException::new, ResultEnum.ILLEGAL_JOB_ERROR);
        ReflectionUtils.invokeMethod(executeMethod, executeBean);
    }
}

