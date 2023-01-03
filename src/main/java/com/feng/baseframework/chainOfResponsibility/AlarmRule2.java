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
public class AlarmRule2 implements AlarmFilter {

	@Override
	public void execute(Alarm alarm, AlarmFilterChain chain) {
		//规则内容：告警名称为：光功率衰耗，描述信息为:割接 则该告警变为确认状态
		if (alarm.getAlarmName().contains("光功率衰耗") && alarm.getDesc().contains("割接")) {
			alarm.setAlarmAck(0);
			System.out.println("执行规则2");
		}

		//注意回调FilterChain的doFilter方法，让FilterChain继续执行下一个Filter
		chain.doFilter(alarm, chain);
	}
}
