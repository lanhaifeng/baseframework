package com.feng.baseframework.snmp;

import org.junit.Test;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;

public class SnmpTemplateTest {

	@Test
	public void snmpGet() {
		//net-snmp-create-v3-user -ro -A auth123456 -X priv123456 -a MD5 -x DES lan
		SnmpAuth snmpAuthOne = SnmpAuth.SnmpAuthBuilder.builder().ip("192.168.230.156").port(161).version(SnmpConstants.version3)
				.securityLevel(SecurityLevel.AUTH_PRIV).securityModel(SecurityModel.SECURITY_MODEL_USM)
				.community("hzmc+Ra2$yuL")
				.securityName("fx").authType(AbstractSnmp.SnmpV3AuthType.MD5.toString()).passAuth("auth123456")
				.privType(AbstractSnmp.SnmpV3PrivType.DES.toString()).privPass("priv123456")
				.build();

		SnmpAuth snmpAuthTwo = SnmpAuth.SnmpAuthBuilder.builder().ip("192.168.230.157").port(161).version(SnmpConstants.version3)
				.securityLevel(SecurityLevel.AUTH_PRIV).securityModel(SecurityModel.SECURITY_MODEL_USM)
				.community("hzmc+Ra2$yuL")
				.securityName("fx").authType(AbstractSnmp.SnmpV3AuthType.MD5.toString()).passAuth("auth123457")
				.privType(AbstractSnmp.SnmpV3PrivType.DES.toString()).privPass("priv123457")
				.build();

		SnmpTemplate snmpTemplateOne = new SnmpTemplate(snmpAuthOne);
		SnmpTemplate snmpTemplateTwo = new SnmpTemplate(snmpAuthTwo);

		long count = snmpTemplateOne.snmpGet("1.3.6.1.4.1.2021.11.11.0");
		System.out.println(String.format("idle cpu:%s", (count)) + "%");
		System.out.println(String.format("used cpu:%s", (100 - count)) + "%");

		count = snmpTemplateTwo.snmpGet("1.3.6.1.4.1.2021.4.5.0");
		System.out.println(String.format("total memory:%s", (count/1024.0/1024)));
	}
}