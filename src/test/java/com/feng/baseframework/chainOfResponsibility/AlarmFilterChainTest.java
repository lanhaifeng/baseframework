package com.feng.baseframework.chainOfResponsibility;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.model.Alarm;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class AlarmFilterChainTest extends MockitoBaseTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void doFilter() {
		//构造告警事件
		Alarm alarm = new Alarm(1, 10, "光功率衰耗", "省政府23号楼", 1, 1, 1, new Date(), "割接");

		System.out.println(alarm);

		//将规则加入责任链
		AlarmFilterChain filterChain = new AlarmFilterChain();
		filterChain.addFilter(new AlarmRule1()).addFilter(new AlarmRule2());

		//执行责任链
		filterChain.doFilter(alarm, filterChain);

		//输出结果
		System.out.println(alarm);
	}
}