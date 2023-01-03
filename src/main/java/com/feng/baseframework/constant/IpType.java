package com.feng.baseframework.constant;

/**
 * baseframework
 * 2021/1/8 16:28
 * ip类型枚举
 *
 * @author lanhaifeng
 * @since
 **/
public enum IpType {
	IPV4(1),
	IPV6(2)
	;


	private Integer  value;

	private IpType(Integer value){
		this.value = value;
	}

	public static IpType get(int value) {
		for (IpType sub : IpType.values()) {
			if (sub.value.intValue() == value){
				return sub;
			}
		}
		return IPV4;
	}

	public Integer getValue() {
		return value;
	}
}
