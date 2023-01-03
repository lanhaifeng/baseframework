package com.feng.baseframework.chainOfResponsibility;

import com.feng.baseframework.model.Alarm;

/**
 * baseframework
 * 2020/11/5 16:38
 * 告警处理规则
 *
 * @author lanhaifeng
 * @since
 **/
public class AlarmRule1 implements AlarmFilter {

	@Override
	public void execute(Alarm alarm, AlarmFilterChain chain) {
		//规则内容：如果是政府发生告警。告警等级设为最高
		if (alarm.getAlarmAddress().contains("政府")) {
			alarm.setAlarmLevel(4);
			System.out.println("执行规则1");
		}

		//注意回调FilterChain的doFilter方法，让FilterChain继续执行下一个Filter
		chain.doFilter(alarm, chain);
	}
}
