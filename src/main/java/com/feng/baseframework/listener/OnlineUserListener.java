package com.feng.baseframework.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @ProjectName: baseframework
 * @Description: 监听httpSession事件
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/9 23:04
 * @UpdateUser:
 * @UpdateDate: 2018/10/9 23:04
 * @UpdateRemark:
 * @Version: 1.0
 */
public class OnlineUserListener implements HttpSessionListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.info("监听到httpSession创建事件，sessionId为" + httpSessionEvent.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.info("监听到httpSession销毁事件，sessionId为" + httpSessionEvent.getSession().getId());
    }
}
