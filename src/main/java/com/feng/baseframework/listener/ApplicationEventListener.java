package com.feng.baseframework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionCreationEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

/**
 * @ProjectName: baseframework
 * @Description: spring事件监听
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/9 22:08
 * @UpdateUser:
 * @UpdateDate: 2018/10/9 22:08
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ApplicationEventListener implements ApplicationListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        logger.info("监听到spring的事件");
        if(applicationEvent instanceof SessionCreationEvent){
            logger.info("监听到spring security的session创建事件");
        }
        if(applicationEvent instanceof SessionDestroyedEvent){
            logger.info("监听到spring security的session销毁事件");
        }
        if(applicationEvent instanceof HttpSessionDestroyedEvent){
            logger.info("监听到spring security的httpSession销毁事件");
        }
    }
}
