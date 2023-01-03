package com.feng.baseframework.beanRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * baseframework
 * 读设备接口
 * 2021/1/10 21:52
 *
 * @author lanhaifeng
 */
@Component("device")
public class ReadDevice implements Device {

    private static final Logger logger = LoggerFactory.getLogger(ReadDevice.class);

    @Override
    public void action(String action) {
        logger.info("className:{}, ReadDevice {}", this.getClass().getName(), action);
    }

}
