package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * baseframework
 * 2020/11/5 16:09
 * 告警实体
 *
 * @author lanhaifeng
 * @since
 **/
@Accessors(chain = true)
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class Alarm extends EntityBase {

	//告警id
	private Integer id;
	//告警事件总数：这条告警是有几条事件合并而成的
	private Integer eventNumber;
	//告警名称
	private String alarmName;
	//告警发生位置
	private String alarmAddress;
	//是否确认告警 0:确认 1：未确认
	private Integer alarmAck;
	//告警等级 1：可疑 2：高危 3：严重 4：紧急
	private Integer alarmLevel;
	//告警类型 1:停电 2：硬件 3：软件
	private Integer alarmType;
	//告警发送时间
	private Date date;
	private String desc;

	@Override
	public String toString() {
		return "Alarm{" +
				"id=" + id +
				", eventNumber=" + eventNumber +
				", alarmName='" + alarmName + '\'' +
				", alarmAddress='" + alarmAddress + '\'' +
				", alarmAck=" + alarmAck +
				", alarmLevel=" + alarmLevel +
				", alarmType=" + alarmType +
				", date=" + date +
				", desc='" + desc + '\'' +
				'}';
	}

	@Override
	public String preErrorDesc() {
		return "告警";
	}

	@Override
	public List<String> customValidate(Class groupCls) {
		return null;
	}
}
