package com.feng.baseframework.chainOfResponsibility;

import com.feng.baseframework.model.Alarm;

/**
 * baseframework
 * 2020/11/5 16:14
 * 告警过滤器
 *
 * @author lanhaifeng
 * @since
 **/
public interface AlarmFilter {
	void execute(Alarm alarm, AlarmFilterChain chain);
}
