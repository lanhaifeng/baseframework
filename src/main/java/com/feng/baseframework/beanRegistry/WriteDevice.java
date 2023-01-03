package com.feng.baseframework.beanRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * baseframework
 * 写设备接口
 * 2021/1/10 21:52
 *
 * @author lanhaifeng
 */
public class  WriteDevice implements Device {

    private static final Logger logger = LoggerFactory.getLogger(WriteDevice.class);

    @Override
    public void action(String action) {
        logger.info("className:{}, WriteDevice {}", this.getClass().getName(), action);
    }

}
