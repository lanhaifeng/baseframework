package com.feng.baseframework.listener;

import com.feng.baseframework.event.CustomizeApplicationEvent;
import com.feng.baseframework.event.CustomizeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * baseframework
 * 2020/9/27 16:56
 * 通过监听器实现容器启动后执行方法
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class CustomizeDoApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		if (contextRefreshedEvent.getApplicationContext().getParent() == null){
			contextRefreshedEvent.getApplicationContext().publishEvent(new CustomizeApplicationEvent("ApplicationListener<ContextRefreshedEvent> onApplicationEvent"));
			contextRefreshedEvent.getApplicationContext().publishEvent(new CustomizeEvent("ApplicationListener<ContextRefreshedEvent> onApplicationEvent"));
		}
	}
}
