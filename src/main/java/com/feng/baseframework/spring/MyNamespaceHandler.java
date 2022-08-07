package com.feng.baseframework.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * baseframework
 * 2022/8/7 20:53
 * 自定义标签命名空间处理器
 *
 * @author lanhaifeng
 * @since
 **/
public class MyNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("version", new DreamBeanDefinitionParser());
    }
}
