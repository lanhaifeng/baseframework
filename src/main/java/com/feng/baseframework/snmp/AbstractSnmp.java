package com.feng.baseframework.snmp;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * baseframework
 * 2020/8/6 16:00
 *
 * @author lanhaifeng
 * @since
 **/
public abstract class AbstractSnmp {

    protected static byte[] localEngineID;

    static {
        localEngineID = MPv3.createLocalEngineID();
    }

	public final static String ADDRESS_TEMPLATE = "udp:%s/%s";
	protected Snmp snmp = null;

	public void initComm(SnmpAuth snmpAuth) throws IOException {
		TransportMapping<? extends Address> transport = new DefaultUdpTransportMapping();
		snmp = new Snmp(transport);
		if (snmpAuth.getVersion() == SnmpConstants.version3) {
			USM usm = snmp.getUSM();
			if (Objects.isNull(usm)) {
				usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
			}
			SecurityModels.getInstance().addSecurityModel(usm);
			usm.addUser(buildUsmUser(snmpAuth));
		}
		transport.listen();
	}

    /**
     * 2020/8/6 15:58
     * 构建v3 USM认证用户
     *
     * @param securityLevel
     * @param userName
     * @param authType
     * @param authPass
     * @param privType
     * @param privPass
     * @author lanhaifeng
     * @return org.snmp4j.security.UsmUser
     */
    public static UsmUser buildUsmUser(SecurityLevel securityLevel, String userName, OID authType, String authPass, OID privType, String privPass){
        UsmUser usmUser = null;
        //构建默认值
        securityLevel = Optional.ofNullable(securityLevel).orElse(SecurityLevel.authPriv);
        authType = Optional.ofNullable(authType).orElse(AuthMD5.ID);
        privType = Optional.ofNullable(privType).orElse(PrivDES.ID);

        validate(securityLevel, userName, authType, authPass, privType, privPass);
        switch (securityLevel){
            case noAuthNoPriv:
                usmUser = new UsmUser(new OctetString(userName),
                        null, null, null, null);
                break;
            case authNoPriv:
                usmUser = new UsmUser(new OctetString(userName), authType,
                        new OctetString(authPass), null, null);
                break;
            case authPriv:
                usmUser = new UsmUser(new OctetString(userName),authType,
                        new OctetString(authPass), privType, new OctetString(privPass));
                break;
        }

        return usmUser;
    }

    /**
     * 2020/8/6 15:58
     * 构建v3 USM认证用户
     *
     * @param userName
     * @param authPass
     * @param privPass
     * @author lanhaifeng
     * @return org.snmp4j.security.UsmUser
     */
    public static UsmUser buildUsmUser(String userName, String authPass, String privPass){
        return buildUsmUser(SecurityLevel.authPriv, userName, AuthMD5.ID, authPass, PrivDES.ID, privPass);
    }

    /**
     * 2020/12/12 15:58
     * 构建v3 USM认证用户
     *
     * @param snmpAuth
     * @author lanhaifeng
     * @return org.snmp4j.security.UsmUser
     */
    public static UsmUser buildUsmUser(SnmpAuth snmpAuth){
        OID snmpV3AuthType = Optional.ofNullable(SnmpV3AuthType.get(snmpAuth.getAuthType())).map(authType->authType.oid).orElse(null);
        OID snmpV3PrivType = Optional.ofNullable(SnmpV3PrivType.get(snmpAuth.getPrivType())).map(privType->privType.oid).orElse(null);
        return buildUsmUser(SecurityLevel.get(snmpAuth.getSecurityLevel()),
                snmpAuth.getSecurityName(), snmpV3AuthType, snmpAuth.getPassAuth(),
                snmpV3PrivType, snmpAuth.getPrivPass());
    }

    /**
     * 参数校验
     * @param securityLevel
     * @param userName
     * @param authType
     * @param authPass
     * @param privType
     * @param privPass
     */
    private static void  validate(SecurityLevel securityLevel, String userName, OID authType, String authPass, OID privType, String privPass){
        Predicate<String> notEmpty = StringUtils::isBlank;
        Predicate<Object> notNull = Objects::isNull;
        switch (securityLevel){
            case authPriv:
                if(notEmpty.test(userName) && notNull.test(authType)
                        && notEmpty.test(authPass) && notNull.test(privType) && notEmpty.test(privPass)){
                    throw new BusinessException(ResultEnum.PARAM_NULL_ERROR);
                }
                break;
            case authNoPriv:
                if(notEmpty.test(userName) && notNull.test(authType)
                        && notEmpty.test(authPass)){
                    throw new BusinessException(ResultEnum.PARAM_NULL_ERROR);
                }
                break;
            case noAuthNoPriv:
                if(notEmpty.test(userName) && notNull.test(authType)){
                    throw new BusinessException(ResultEnum.PARAM_NULL_ERROR);
                }
                break;
        }
    }

    public static enum SnmpV3AuthType{
        MD5(1, AuthMD5.ID),
        SHA1(2, AuthSHA.ID),
        SHA2(3, null),
        ;

        private int code;
        private OID oid;

        SnmpV3AuthType(int code, OID oid) {
            this.code = code;
            this.oid = oid;
        }

        public static SnmpV3AuthType get(String name){
            for (SnmpV3AuthType snmpV3AuthType : values()) {
                if(snmpV3AuthType.toString().equalsIgnoreCase(name)){
                    return snmpV3AuthType;
                }
            }

            return null;
        }
    }

    public static enum SnmpV3PrivType{
        DES(1, PrivDES.ID),
        AES128(2, PrivAES128.ID),
        AES192(3, PrivAES192.ID),
        AES256(4, PrivAES256.ID),
        ;

        private int code;
        private OID oid;

        SnmpV3PrivType(int code, OID oid) {
            this.code = code;
            this.oid = oid;
        }

        public static SnmpV3PrivType get(String name){
            for (SnmpV3PrivType snmpV3PrivType : values()) {
                if(snmpV3PrivType.toString().equalsIgnoreCase(name)){
                    return snmpV3PrivType;
                }
            }

            return null;
        }
    }
}
