package com.feng.baseframework.snmp;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.util.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SnmpTrapSenderTest extends MockitoBaseTest {

	private static Logger log = LoggerFactory.getLogger(SnmpTrapSenderTest.class);
	private String ip;
	private int port;
	private String community;
	private int securityLevel;
	private int securityModel;
	private String userName;
	private String passAuth;
	private String privPass;
	private String notification;

	@Before
	public void setUp() throws Exception {
		ip = "192.168.230.206";
		port = 162;
		community = "hzmc+Ra2$yuL";
		securityLevel = SecurityLevel.AUTH_PRIV;
		securityModel = SecurityModel.SECURITY_MODEL_USM;
		userName = "test";
		passAuth = "098f6bcd4621d373cade4e832627b4f6";
		privPass = "PzMY6G9gKK2N52wfH7aANg==";
        notification = "1.3.6.1.6.3.1.1.4.8.5";
	}

	/**
	 * 2020/8/5 19:37
	 * 构建发送数据
	 *
	 * @param
	 * @return java.util.List<org.snmp4j.smi.VariableBinding>
	 * @author lanhaifeng
	 */
	public List<VariableBinding> buildSendData() {
		List<VariableBinding> datas = new ArrayList<>();
		try {
			String sql = FileUtils.readFile(FileUtils.getFile("classpath:sql/snmpTest.sql").getAbsolutePath());
			OctetString sqlText = new OctetString(sql);
			datas.add(new VariableBinding(new OID(".1.3.6.1.6.3.1.1.4.6.1.0"), sqlText));
			datas.add(new VariableBinding(new OID(".1.3.6.1.6.3.1.1.4.6.1.0"), new OctetString("SnmpTrap2")));
			datas.add(new VariableBinding(new OID(".1.3.6.1.6.3.1.1.4.8.1.1"), new OctetString("SnmpTrap3")));
		} catch (Exception e) {
			log.error("构建发送数据失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}

		return datas;
	}

	public void testSendTrap(String ip, int port, int version, String community,
									Integer securityLevel, Integer securityModel,
									String securityName, String passAuth, String privPass, String notification) throws IOException {
		SnmpAuth.SnmpAuthBuilder snmpAuthBuilder = SnmpAuth.SnmpAuthBuilder.builder();
		snmpAuthBuilder.ip(ip).port(port).version(version).community(community);
		SnmpTrapSender sender = new SnmpTrapSender();
		if(version == SnmpConstants.version3){
			snmpAuthBuilder.securityLevel(securityLevel).securityModel(securityModel)
					.securityName(securityName).passAuth(passAuth).privPass(privPass);
		}
		SnmpAuth snmpAuth = snmpAuthBuilder.build();
		if(snmpAuth.validate()){
			sender.initComm(snmpAuth);
			sender.sendPDU(snmpAuth, buildSendData(), notification);
		}
	}

	@Test
    //TODO 还需要调试如何传送特定的notification
	public void testSendTrap1() throws IOException {
		testSendTrap(ip, port, SnmpConstants.version1, community, null, null, null, null, null, notification);
	}

	@Test
	public void testSendTrap2c() throws IOException {
		testSendTrap(ip, port, SnmpConstants.version2c, community, null, null, null, null, null, notification);
	}

	@Test
	public void testSendTrap3() throws IOException {
		testSendTrap(ip, port, SnmpConstants.version3, community, securityLevel, securityModel, userName, passAuth, privPass, notification);
	}
}