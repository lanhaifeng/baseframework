package com.feng.baseframework.snmp;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.*;

import java.io.IOException;
import java.util.List;

/**
 * baseframework
 * 2020/8/4 19:49
 * snmp trap发送
 *
 * @author lanhaifeng
 * @since
 **/
public class SnmpTrapSender extends AbstractSnmp {

	private static Logger log = LoggerFactory.getLogger(SnmpTrapSender.class);

	/**
	 * 2020/8/5 16:37
	 * 构建目标地址
	 *
	 * @param targetIp
	 * @param targetPort
	 * @return org.snmp4j.smi.Address
	 * @author lanhaifeng
	 */
	public Address buildAddress(String targetIp, int targetPort) {
		return GenericAddress.parse(String.format(ADDRESS_TEMPLATE, targetIp, String.valueOf(targetPort)));
	}


	/**
	 * 2020/8/5 16:32
	 * 构建target
	 *
	 * @param snmpAuth
	 * @param targetAddress
	 * @return org.snmp4j.CommunityTarget
	 * @author lanhaifeng
	 */
	public Target buildCommunityTarget(SnmpAuth snmpAuth, Address targetAddress) {
		// 设置 target
		Target target = null;

		int version = snmpAuth.getVersion();
		//版本1和2c基于团体名验证
		if (version == SnmpConstants.version1 || version == SnmpConstants.version2c) {
			// 目标地址和团体名
			target = new CommunityTarget(targetAddress, new OctetString(snmpAuth.getCommunity()));
		}
		if (version == SnmpConstants.version3) {
			target = new UserTarget();
			if (snmpAuth.getSecurityLevel() == SecurityLevel.AUTH_NOPRIV
					|| snmpAuth.getSecurityLevel() == SecurityLevel.AUTH_PRIV) {
				target.setSecurityName(new OctetString(snmpAuth.getSecurityName()));
			}
			target.setSecurityLevel(snmpAuth.getSecurityLevel());
			target.setSecurityModel(snmpAuth.getSecurityModel());
			//设置目标地址
			target.setAddress(targetAddress);
		}

		// 通信不成功时的重试次数
		target.setRetries(2);
		// 超时时间
		target.setTimeout(1500);
		// snmp版本
		target.setVersion(version);
		return target;
	}

	/**
	 * 2020/8/5 19:33
	 * 解析返回结果
	 *
	 * @param respEvnt
	 * @return void
	 * @author lanhaifeng
	 */
	public void parseResponse(ResponseEvent respEvnt) {
		if (respEvnt != null && respEvnt.getResponse() != null) {
			List<? extends VariableBinding> recVBs = respEvnt.getResponse()
					.getVariableBindings();
			for (int i = 0; i < recVBs.size(); i++) {
				VariableBinding recVB = recVBs.get(i);
				log.info(recVB.getOid() + " : " + recVB.getVariable());
			}
		}
	}

	/**
	 * 向管理进程发送Trap 1报文
	 *
	 * @param snmpAuth
	 * @param datas
	 * @param notification
	 * @return void
	 * @author lanhaifeng
	 */
	public void sendPDU(SnmpAuth snmpAuth, List<VariableBinding> datas, String notification) throws IOException {
		Address address = buildAddress(snmpAuth.getIp(), snmpAuth.getPort());
		int version = snmpAuth.getVersion();
		Target target = buildCommunityTarget(snmpAuth, address);

		// 创建 PDU
		PDU pdu = null;
		if (version == SnmpConstants.version1) {
			pdu = new PDUv1();
		}
		if (version == SnmpConstants.version2c) {
			pdu = new PDU();
			pdu.setType(PDU.TRAP);
		}
		if (version == SnmpConstants.version3) {
			pdu = new ScopedPDU();
			pdu.setType(PDU.NOTIFICATION);
		}

		if (datas == null || pdu == null) {
			return;
		}

		//处理trap头
		if(version == SnmpConstants.version1){
			//snmptrap -v1 -c 'hzmc+Ra2$yuL' 0.0.0.0 SNMPv2-MIB::snmpTraps.8.2 127.0.0.1 7 0 1000 SNMPv2-MIB::snmpTraps.8.2.1 s test
			//0.0.0.0 agentAddress
			//7 genericTrap
			//0 specificTrap
			//1000指明自代理进程初始化到trap报告的事件发生所经历的时间,timestamp
			//SNMPv2-MIB::snmpTraps.8.2 enterprise
			//SNMPv2-MIB::snmpTraps.8.2.1

			//服务端接收到的信息第一行为trap头信息：SNMPv2-MIB::snmpTrap.8.5 Enterprise Specific Trap (6) Uptime: 0:00:10.00
			//snmpTrap.8.5对应enterprise
			//Enterprise Specific Trap对应genericTrap
			//(6)对应specificTrap
			//Uptime对应上边命令中1000

			//snmptrap.conf中配置traphandle需要对应genericTrap
			//genericTrap为0，traphandle SNMPv2-MIB::coldStart /etc/snmp/lognotify
			//genericTrap为1，traphandle SNMPv2-MIB::warmStart /etc/snmp/lognotify
			//genericTrap为2，traphandle IF-MIB::linkDown /etc/snmp/lognotify
			//genericTrap为3，traphandle IF-MIB::linkUp /etc/snmp/lognotify
			//genericTrap为4，traphandle SNMPv2-MIB::authenticationFailure /etc/snmp/lognotify
			//genericTrap为5，traphandle SNMPv2-MIB::egpNeighborLoss /etc/snmp/lognotify，
			// 还需要在SNMPv2-MIB.txt中添加egpNeighborLoss的trap声明，可以仿照coldStart，将最后一行改为snmpTraps 6即可
			//genericTrap为6时，mib文件中未找到对应定义，即使添加定义也无法触发对应的traphandle处理
			((PDUv1)pdu).setGenericTrap(0);
			((PDUv1)pdu).setSpecificTrap(0);
			((PDUv1)pdu).setEnterprise(new OID(notification));
			((PDUv1)pdu).setTimestamp(System.currentTimeMillis()/1000);
		}

		//处理trap头
		//define snmp trap OID
		//解决问题：Cannot find TrapOID in TRAP2 PDU
        //trap2时需要传oid为SnmpConstants.snmpTrapOID，值为发送trap的oid
		if (version == SnmpConstants.version2c || version == SnmpConstants.version3) {
			datas.add(new VariableBinding(SnmpConstants.snmpTrapOID, new OID(notification)));
		}

		//构造数据
		pdu.addAll(datas);

		// 向Agent发送PDU，并接收Response
		ResponseEvent respEvnt = snmp.send(pdu, target);

		// 解析Response
		parseResponse(respEvnt);
	}

	/**
	 * 2020/8/11 14:51
	 * 关闭snmp
	 *
	 * @param
	 * @return void
	 * @author lanhaifeng
	 */
	public void close() {
		try {
			if (snmp != null) {
				snmp.close();
			}
		} catch (Exception e) {
			log.error("关闭snmp失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
	}

}