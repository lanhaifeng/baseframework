package com.feng.baseframework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;

/**
 * @ProjectName: baseframework
 * @description: 获取bean工具类
 * @author: lanhaifeng
 * @create: 2018-05-16 18:33
 * @UpdateUser:
 * @UpdateDate: 2018/5/16 18:33
 * @UpdateRemark:
 **/
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(!Optional.ofNullable(SpringUtil.applicationContext).isPresent()) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @author: lanhaifeng
     * @description 通过bean name获取对象
     * @date: 2018/5/16 18:36
     * @param name
     * @return java.lang.Object
     */
    public static Object getBeanByName(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     * @author: lanhaifeng
     * @description 通过bean type获取对象
     * @date: 2018/5/16 18:36
     * @param clazz
     * @return T
     */
    public static <T> T getBeanByType(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     * @author: lanhaifeng
     * @description 通过bean name and type获取对象
     * @date: 2018/5/16 18:36
     * @param name
     * @param clazz
     * @return T
     */
    public static <T> T getBeanByNameAndType(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

    /**
     * 2019/02/18 16:09
     * 获取当前环境
     *
     * @param
     * @author lanhaifeng
     * @return java.lang.String
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }
}
