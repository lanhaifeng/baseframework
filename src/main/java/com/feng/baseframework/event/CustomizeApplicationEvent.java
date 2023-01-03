package com.feng.baseframework.event;

import org.springframework.context.ApplicationEvent;

/**
 * baseframework
 * 2020/9/25 17:57
 * 自定义spring事件
 *
 * @author lanhaifeng
 * @since
 **/
public class CustomizeApplicationEvent extends ApplicationEvent {

	public CustomizeApplicationEvent(Object source) {
		super(source);
	}
}
