package com.feng.baseframework.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 测试运行job
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/28 9:41创建:TestJobRun
 * @since 1.0
 */
@Component
public class TestJobRun {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @JobScheduler(cron = "* 0 0/3 * * ?", desc = "测试定时任务")
    public void timeExecute() {
        logger.info("test job execute!");
    }
}
