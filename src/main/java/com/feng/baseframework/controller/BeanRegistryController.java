package com.feng.baseframework.controller;

import com.feng.baseframework.beanRegistry.Device;
import com.feng.baseframework.beanRegistry.DeviceDelegate;
import com.feng.baseframework.beanRegistry.WriteDevice;
import com.feng.baseframework.util.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * baseframework
 * bean注册测试控制层
 * 2021/1/10 22:02
 *
 * @author lanhaifeng
 */
@RestController
@RequestMapping("beanRegistry")
public class BeanRegistryController {

    @Autowired
    private DeviceDelegate deviceDelegate;

    @RequestMapping(path = "sourceBean", method = RequestMethod.POST)
    public void deviceActionForSource(@RequestBody String action){
        deviceDelegate.action(action);
    }

    @RequestMapping(path = "newBean", method = RequestMethod.POST)
    public void deviceActionForNew(@RequestBody String action){
        deviceDelegate.action(action);

        //能将spring容器中device的实例替换掉，但是不能将其他已经引用过老device的实例中device实例替换成新的
        BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)SpringUtil.getApplicationContext().getAutowireCapableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(WriteDevice.class);
        beanRegistry.removeBeanDefinition("device");
        beanRegistry.registerBeanDefinition("device", beanDefinitionBuilder.getBeanDefinition());
        deviceDelegate.action(action);

        //已经替换成新的实例，既WriteDevice实例，所以可以通过每次使用实例均从spring容器中取，使得我们可以动态修改实例
        Device device = SpringUtil.getBeanByNameAndType("device", Device.class);
        device.action(action);
    }
}
