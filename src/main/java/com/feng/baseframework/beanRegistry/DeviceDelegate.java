package com.feng.baseframework.beanRegistry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * baseframework
 * 设备委托类
 * 2021/1/10 22:00
 *
 * @author lanhaifeng
 */
@Component
public class DeviceDelegate {

    @Autowired
    @Qualifier("device")
    private Device device;

    public void action(String action){
        device.action(action);
    }

}
