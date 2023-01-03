package com.feng.baseframework.snmp;

import com.feng.baseframework.constant.IpType;
import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2020/12/12 11:12
 * snmp模板类
 *
 * @author lanhaifeng
 */
public class SnmpTemplate extends AbstractSnmp {

    private static final Logger log = LoggerFactory.getLogger(SnmpTemplate.class);
    private String ipFormate = IP_V4_FORMATE;
    private static String IP_V4_FORMATE = "udp:%s/%s";
    private static String IP_V6_FORMATE = "udp:[%s]/%s";
    private Snmp snmp;
    private SnmpAuth snmpAuth;

    /**
     * 初始化方法
     * @param snmpAuth
     */
    private void init(SnmpAuth snmpAuth){
        if(Objects.isNull(snmpAuth) || !snmpAuth.validate()){
            throw new BusinessException(ResultEnum.PARAM_ILLEGAL_ERROR);
        }
        if(snmpAuth.getIpType() == IpType.IPV6.getValue()){
            ipFormate = IP_V6_FORMATE;
        }
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            if (snmpAuth.getVersion() == SnmpConstants.version3) {
                if(snmpAuth.getSecurityModel() == SecurityModel.SECURITY_MODEL_TSM){
                    throw new BusinessException("不支持授权模式：" + snmpAuth.getSecurityModel());
                }
            }
            // 开始监听消息
            transport.listen();
        } catch (Exception e) {
            log.error("初始化snmp失败，错误：" + ExceptionUtils.getFullStackTrace(e));
        }
    }

    public SnmpTemplate(SnmpAuth snmpAuth) {
        this.snmpAuth = snmpAuth;
        init(snmpAuth);
    }

    private Target createTarget() {
        Target target = null;
        if (snmpAuth.getVersion() == SnmpConstants.version3) {
            target = new UserTarget();
            // 设置安全级别
            target.setSecurityLevel(snmpAuth.getSecurityLevel());
            // 设置认证类型
            target.setSecurityModel(snmpAuth.getSecurityModel());
            // 设置认证名
            target.setSecurityName(new OctetString(snmpAuth.getSecurityName()));
            // 设置安全模式
            //USM模式
            if(snmpAuth.getSecurityModel() == SecurityModel.SECURITY_MODEL_USM){
                USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(localEngineID), 0);
                SecurityModels.getInstance().addSecurityModel(usm);
                // 添加用户，用户需要在每次发送请求时指定，否则两个不同主机认证名相同，密码不同时会报错
                snmp.getUSM().addUser(new OctetString(snmpAuth.getSecurityName()), buildUsmUser(snmpAuth));
            }
        } else {
            target = new CommunityTarget();
            if (snmpAuth.getVersion() == SnmpConstants.version1) {
                target.setVersion(SnmpConstants.version1);
                ((CommunityTarget) target).setCommunity(new OctetString(snmpAuth.getCommunity()));
            } else {
                target.setVersion(SnmpConstants.version2c);
                ((CommunityTarget) target).setCommunity(new OctetString(snmpAuth.getCommunity()));
            }
        }
        target.setVersion(snmpAuth.getVersion());
        //必须指定，没有设置就会报错。
        target.setAddress(GenericAddress.parse(String.format(ipFormate, snmpAuth.getIp(), snmpAuth.getPort())));
//		target.setRetries(3);  //不停轮询所以无需重试
        target.setTimeout(1000);
        return target;
    }

    private PDU createPDU(int type, String oid){
        PDU pdu = null;
        if (snmpAuth.getVersion() == SnmpConstants.version3) {
            pdu = new ScopedPDU();
        }else {
            pdu = new PDUv1();
        }
        pdu.setType(type);
        //可以添加多个变量oid
        pdu.add(new VariableBinding(new OID(oid)));
        return pdu;
    }

    public long snmpGet(String oid){
        long count;
        if(log.isDebugEnabled()){
            log.debug("snmpget发送PDU");
        }
        try {
            //1、初始化snmp,并开启监听
            //initSnmp(SnmpConstants.version2c);
            //2、创建目标对象
            Target target = createTarget();
            //3、创建报文
            PDU pdu = createPDU(PDU.GET, oid);
            //4、发送报文，并获取返回结果
            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();
            if(log.isDebugEnabled()){
                log.debug("snmpget返回结果：" + response);
            }
            if(response == null || !SnmpResponseValidate.validateResponse(oid, response)){
                return -1;
            }
            count = response.get(0).getVariable().toLong();
        } catch (Exception e) {
            count = -1;
            log.error("snmpGet出现异常：" + ExceptionUtils.getFullStackTrace(e));
        }
        return count;
    }

    public String snmpGetString(String oid){
        String result = null;
        try {
            //2、创建目标对象
            Target target = createTarget();
            //3、创建报文
            PDU pdu = createPDU(PDU.GET, oid);
            //4、发送报文，并获取返回结果
            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();
            if(log.isDebugEnabled()){
                log.debug("snmpGetString返回结果：" + response);
            }
            if(response == null || !SnmpResponseValidate.validateResponse(oid, response)){
                return result;
            }
            result = response.get(0).getVariable().toString();
        } catch (Exception e) {
            result = null;
            log.error("snmpGet出现异常：" + ExceptionUtils.getFullStackTrace(e));
        }
        return result;
    }

    public List<Entry> snmpWalk(String oid) {
        List<Entry> result = new ArrayList<>();
        if(log.isDebugEnabled()){
            log.debug("snmpwalk发送PDU,oid" + oid);
        }
        try {
            //创建目标对象
            Target target = createTarget();
            //创建报文
            PDU pdu = createPDU(PDU.GETNEXT, oid);

            //发送报文，并获取返回结果
            boolean matched = true;

            while (matched) {
                ResponseEvent responseEvent = snmp.send(pdu, target);
                if (responseEvent == null || responseEvent.getResponse() == null) {
                    break;
                }
                PDU response = responseEvent.getResponse();
                if(log.isDebugEnabled()){
                    log.debug("snmpWalk返回结果：" + response);
                }
                String nextOid = null;
                List<? extends VariableBinding> variableBindings = response.getVariableBindings();
                for (VariableBinding variableBinding : variableBindings) {
                    Variable variable = variableBinding.getVariable();
                    nextOid = variableBinding.getOid().toDottedString();
                    //如果不是这个节点下的oid则终止遍历，否则会输出很多，直到整个遍历完。
                    if (!nextOid.startsWith(oid) && SnmpResponseValidate.checkWalkFinished(new OID(oid), pdu, variableBinding)) {
                        matched = false;
                        break;
                    }
                    if(log.isDebugEnabled()){
                        log.debug("variable：" + variable.toString());
                    }
                    result.add(new Entry(oid, nextOid, variable.toString()));
                }
                pdu.clear();
                pdu.add(new VariableBinding(new OID(nextOid)));
            }
        } catch (Exception e) {
            log.error("snmp walk失败，错误：" + ExceptionUtils.getFullStackTrace(e));
            return null;
        }

        return result;
    }

    static class Entry{
        String sourceOid;
        String nextOid;
        String index;
        String value;

        Entry(String sourceOid, String nextOid, String value) {
            this.sourceOid = sourceOid;
            this.nextOid = nextOid;
            parseIndex(sourceOid, nextOid);
            this.value = value;
        }

        private void parseIndex(String sourceOid, String nextOid){
            String index = "";
            if(StringUtils.isNotBlank(sourceOid) && StringUtils.isNotBlank(nextOid) && !sourceOid.equals(nextOid)
                    && nextOid.compareTo(sourceOid) > 0){
                index = nextOid.replace(sourceOid + ".", "");
            }

            this.index = index;
        }
    }
}
