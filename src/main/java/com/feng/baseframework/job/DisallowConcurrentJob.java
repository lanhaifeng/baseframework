package com.feng.baseframework.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 不允许并发执行job
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:55创建:DisallowConcurrentJob
 * @since 1.0
 */
@DisallowConcurrentExecution
public class DisallowConcurrentJob extends ConcurrentJob {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        super.execute(jobExecutionContext);
    }
}

