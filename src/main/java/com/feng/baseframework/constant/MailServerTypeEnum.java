package com.feng.baseframework.constant;

/**
 * mail-spring-boot
 * 2019/5/10 11:24
 * 邮件服务器类型
 *
 * @author lanhaifeng
 * @since
 **/
public enum MailServerTypeEnum {
	SMTP,EXCHANGE;

	public static MailServerTypeEnum getEnum(String value) {
		for (MailServerTypeEnum a : values()) {
			if (a.toString().equals(value)) {
				return a;
			}
		}
		return null;
	}
}
