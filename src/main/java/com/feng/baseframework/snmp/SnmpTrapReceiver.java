package com.feng.baseframework.snmp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import java.io.IOException;
import java.util.List;

/**
 * baseframework
 * 2020/8/4 19:44
 * snmp的trap监听
 *
 * @author lanhaifeng
 * @since
 **/
public class SnmpTrapReceiver extends AbstractSnmp implements CommandResponder {

	private static Logger log = LoggerFactory.getLogger(SnmpTrapReceiver.class);

	public SnmpTrapReceiver() {
	}

	private void init(String ip, int port, SnmpAuth snmpAuth) throws IOException {
		ThreadPool threadPool = ThreadPool.create("Trap", 2);
		MultiThreadedMessageDispatcher dispatcher = new MultiThreadedMessageDispatcher(threadPool,
				new MessageDispatcherImpl());

		// 监听IP与监听端口
		Address listenAddress = GenericAddress.parse(System.getProperty(
				"snmp4j.listenAddress", String.format(ADDRESS_TEMPLATE, ip, port)));
		TransportMapping<? extends Address> transport;
		// 对TCP与UDP协议进行处理
		if (listenAddress instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping(
					(UdpAddress) listenAddress);
		} else {
			transport = new DefaultTcpTransportMapping(
					(TcpAddress) listenAddress);
		}
		snmp = new Snmp(dispatcher, transport);
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3
				.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		usm.addUser(buildUsmUser(snmpAuth.getSecurityName(), snmpAuth.getPassAuth(), snmpAuth.getPrivPass()));
		snmp.listen();
	}


	public void run(String ip, int port, SnmpAuth snmpAuth) {
		try {
			init(ip, port, snmpAuth);
			snmp.addCommandResponder(this);
			log.info("开始监听Trap信息!");
		} catch (Exception ex) {
			log.error("初始化失败，错误：" + ExceptionUtils.getFullStackTrace(ex));
		}
	}

	/**
	 * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息
	 * 当接收到trap时，会自动进入这个方法
	 * @param respEvnt
	 */
	@Override
	public void processPdu(CommandResponderEvent respEvnt) {
		// 解析Response
		if (respEvnt != null && respEvnt.getPDU() != null) {
			List<? extends VariableBinding> recVBs = respEvnt.getPDU().getVariableBindings();
			for (int i = 0; i < recVBs.size(); i++) {
				try{
					VariableBinding recVB = recVBs.get(i);
					if(recVB.getVariable() instanceof OctetString){
						OctetString result = (OctetString)recVB.getVariable();
						if(!result.isPrintable()){
							log.info(recVB.getOid() + " : " + new String(result.getValue()));
						}
					}else {
						log.info(recVB.getOid() + " : " + recVB.getVariable().toString());
					}
				}catch(Exception e){
				    log.error("" + ExceptionUtils.getFullStackTrace(e));
				}
			}
		}
	}

	public static void main(String[] args) {
		try {
			String ip = "192.168.61.47";
			int port = 162;

			String community = "hzmc+Ra2$yuL";
			int securityLevel = SecurityLevel.AUTH_PRIV;
			int securityModel = SecurityModel.SECURITY_MODEL_USM;
			String securityName = "test";
			String passAuth = "098f6bcd4621d373cade4e832627b4f6";
			String privPass = "PzMY6G9gKK2N52wfH7aANg==";
			SnmpAuth.SnmpAuthBuilder snmpAuthBuilder = SnmpAuth.SnmpAuthBuilder.builder();
			SnmpAuth snmpAuth = new SnmpAuth();
			snmpAuthBuilder.securityLevel(securityLevel).securityModel(securityModel)
					.community(community)
					.securityName(securityName).passAuth(passAuth).privPass(privPass);

			SnmpTrapReceiver snmpTrapReceiver = new SnmpTrapReceiver();
			snmpTrapReceiver.run(ip, port, snmpAuth);
		} catch (Exception e) {
			log.error("监听trap失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
	}
}
