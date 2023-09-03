package com.feng.baseframework.spring;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * baseframework
 * 2022/5/3 17:35
 * 自定义RequestHandlerMapping
 *
 * @author lanhaifeng
 * @since 1.0
 **/
public class MyRequestHandlerMapping extends RequestMappingHandlerMapping {

    public MyRequestHandlerMapping() {
        setOrder(-100);
    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return false;
    }

    /**
     * 注册controller方法
     * 1.使用bean在spring容器中的名字
     * 2.使用实例
     */
    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
//        detectHandlerMethods("myController");
        detectHandlerMethods(new MyController());
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestCondition<?> methodCondition = getCustomMethodCondition(method);
        return createRequestMappingInfo(method.getName(), methodCondition);
    }


    private RequestMappingInfo createRequestMappingInfo(String methodName, RequestCondition<?> customCondition) {
        return new RequestMappingInfo(
                new PatternsRequestCondition(new String[]{methodName}),
                new RequestMethodsRequestCondition(new RequestMethod[]{RequestMethod.GET}),
                new ParamsRequestCondition(new String[]{}),
                new HeadersRequestCondition(new String[]{}),
                new ConsumesRequestCondition(new String[]{}, new String[]{}),
                new ProducesRequestCondition(new String[]{}, new String[]{}, getContentNegotiationManager()),
                customCondition);
    }
}
