package com.feng.baseframework.chainOfResponsibility;

import com.feng.baseframework.model.Alarm;

import javax.servlet.FilterChain;
import java.util.ArrayList;
import java.util.List;

/**
 * baseframework
 * 2020/11/5 16:15
 * 告警链，基于list
 *
 * @author lanhaifeng
 * @since
 **/
public class AlarmFilterChain {

	//规则过滤器列表，实现Filter接口的过滤器将真正执行对事件的处理
	private List<AlarmFilter> filters = new ArrayList<>();
	//过滤器列表的索引
	private int index;

	//向责任链中加入过滤器（单个）
	public AlarmFilterChain addFilter(AlarmFilter filter) {
		this.filters.add(filter);
		return this;
	}

	//向责任链中加入过滤器（多个）
	public AlarmFilterChain addFilters(List<AlarmFilter> filters) {
		this.filters.addAll(filters);
		return this;
	}

	//处理事件（alarm）从FilterChain中获取过滤器，进行处理，处理完成之后过滤器会再调用该方法，
	//继续执行下一个filter.这就需要在每个Filter接口的实现类中最后一句需要回调FilterChain的doFilter方法。
	public void doFilter(Alarm alarm, AlarmFilterChain chain) {
		if (index == filters.size()) {
			return;
		}
		AlarmFilter filter = filters.get(index);
		index++;
		filter.execute(alarm, chain);
	}
}
