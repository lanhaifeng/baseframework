package com.feng.baseframework.spring;

import com.feng.baseframework.model.Version;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * baseframework
 * 2022/8/7 23:01
 * 测试自定义标签
 *
 * @author lanhaifeng
 * @since
 **/
public class TestCustomTag {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new
                ClassPathXmlApplicationContext("classpath:customTag.xml");

        Version version = (Version) applicationContext.getBean("myVersion");

        System.out.println("myVersionTest = " + version);
    }
}
