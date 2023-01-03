package com.feng.baseframework.snmp;

import com.feng.baseframework.constant.IpType;
import org.apache.commons.lang.StringUtils;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * baseframework
 * 2020/8/6 16:05
 * snmp认证domain
 *
 * @author lanhaifeng
 * @since
 **/
public class SnmpAuth implements Serializable {

	//版本，[SnmpConstants.version1, SnmpConstants.version2c, SnmpConstants.version3]
	private int version;
	//团体名
	private String community;
	//授权模式,见org.snmp4j.security.SecurityModel
    //SECURITY_MODEL_SNMPv1 v1
    //SECURITY_MODEL_SNMPv2c v2c
    //SECURITY_MODEL_USM v3  默认授权模式
    //SECURITY_MODEL_TSM v3  用于SSH or DTLS
    //SECURITY_MODEL_KSM v3  用于支持Kerberos
	private int securityModel;
	//snmpv3使用，认证级别,见org.snmp4j.security.SecurityLevel
	private int securityLevel;
	//snmpv3使用，认证用户，是否需要传值由securityLevel决定
	private String securityName;
    //snmpv3使用，认证方式，见com.feng.baseframework.snmp.AbstractSnmp.SnmpV3AuthType
	private String authType;
	//snmpv3使用，认证密码，是否需要传值由securityLevel决定
	private String passAuth;
    //snmpv3使用，加密方式，见com.feng.baseframework.snmp.AbstractSnmp.SnmpV3PrivType
    private String privType;
	//snmpv3使用，加密密码，是否需要传值由securityLevel决定
	private String privPass;
	//snmp服务端ip
	private String ip;
	//snmp服务端端口
	private int port;
	//ip类型，见com.feng.baseframework.constant.IpType
	private int ipType;

	public boolean validate(){
		boolean validateResult = true;
		if(Objects.isNull(IpType.get(getIpType()))){
			validateResult = false;
		}
		if(StringUtils.isBlank(getIp()) || getPort() < 1 || getPort() > 65535
				|| !Arrays.asList(SnmpConstants.version1, SnmpConstants.version2c, SnmpConstants.version3).contains(getVersion())){
			validateResult = false;
		}
		if ((getVersion() == SnmpConstants.version1 || getVersion() == SnmpConstants.version2c) && StringUtils.isBlank(getCommunity())) {
			validateResult = false;
		}
		if(getVersion() == SnmpConstants.version1 && getSecurityModel() != SecurityModel.SECURITY_MODEL_SNMPv1 ||
				getVersion() == SnmpConstants.version2c && getSecurityModel() != SecurityModel.SECURITY_MODEL_SNMPv2c
				|| getVersion() == SnmpConstants.version3 && getSecurityModel() != SecurityModel.SECURITY_MODEL_USM){
			validateResult = false;
		}
		if(getVersion() == SnmpConstants.version3){
			if (SecurityLevel.get(getSecurityLevel()) == SecurityLevel.undefined || StringUtils.isBlank(getSecurityName())) {
				return false;
			}

			if ((SecurityLevel.get(getSecurityLevel()) == SecurityLevel.authNoPriv || SecurityLevel.get(getSecurityLevel()) == SecurityLevel.authPriv)
					&& (StringUtils.isBlank(getPassAuth()) || Objects.isNull(AbstractSnmp.SnmpV3AuthType.get(getAuthType())))) {
				return false;
			}

			if (SecurityLevel.get(getSecurityLevel()) == SecurityLevel.authPriv
					&& (StringUtils.isBlank(getPrivPass()) || Objects.isNull(AbstractSnmp.SnmpV3PrivType.get(getPrivType())))) {
				return false;
			}
		}


		return validateResult;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	public int getSecurityModel() {
		return securityModel;
	}

	public void setSecurityModel(int securityModel) {
		this.securityModel = securityModel;
	}

	public int getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(int securityLevel) {
		this.securityLevel = securityLevel;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public String getPassAuth() {
		return passAuth;
	}

	public void setPassAuth(String passAuth) {
		this.passAuth = passAuth;
	}

	public String getPrivPass() {
		return privPass;
	}

	public void setPrivPass(String privPass) {
		this.privPass = privPass;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getPrivType() {
        return privType;
    }

    public void setPrivType(String privType) {
        this.privType = privType;
    }

	public int getIpType() {
		return ipType;
	}

	public void setIpType(int ipType) {
		this.ipType = ipType;
	}

	public static final class SnmpAuthBuilder {
		//版本，[SnmpConstants.version1, SnmpConstants.version2c, SnmpConstants.version3]
		private int version;
		//团体名
		private String community;
		//授权模式,见org.snmp4j.security.SecurityModel
		//SECURITY_MODEL_SNMPv1 v1
		//SECURITY_MODEL_SNMPv2c v2c
		//SECURITY_MODEL_USM v3  默认授权模式
		//SECURITY_MODEL_TSM v3  用于SSH or DTLS
		//SECURITY_MODEL_KSM v3  用于支持Kerberos
		private int securityModel;
		//snmpv3使用，认证级别,见org.snmp4j.security.SecurityLevel
		private int securityLevel;
		//snmpv3使用，认证用户，是否需要传值由securityLevel决定
		private String securityName;
		//snmpv3使用，认证方式，见com.feng.baseframework.snmp.AbstractSnmp.SnmpV3AuthType
		private String authType;
		//snmpv3使用，认证密码，是否需要传值由securityLevel决定
		private String passAuth;
		//snmpv3使用，加密方式，见com.feng.baseframework.snmp.AbstractSnmp.SnmpV3PrivType
		private String privType;
		//snmpv3使用，加密密码，是否需要传值由securityLevel决定
		private String privPass;
		//snmp服务端ip
		private String ip;
		//snmp服务端端口
		private int port;
		//ip类型，见com.feng.baseframework.constant.IpType
		private int ipType;

		private SnmpAuthBuilder() {
		}

		public static SnmpAuthBuilder builder() {
			return new SnmpAuthBuilder();
		}

		public SnmpAuthBuilder version(int version) {
			this.version = version;
			return this;
		}

		public SnmpAuthBuilder community(String community) {
			this.community = community;
			return this;
		}

		public SnmpAuthBuilder securityModel(int securityModel) {
			this.securityModel = securityModel;
			return this;
		}

		public SnmpAuthBuilder securityLevel(int securityLevel) {
			this.securityLevel = securityLevel;
			return this;
		}

		public SnmpAuthBuilder securityName(String securityName) {
			this.securityName = securityName;
			return this;
		}

		public SnmpAuthBuilder authType(String authType) {
			this.authType = authType;
			return this;
		}

		public SnmpAuthBuilder passAuth(String passAuth) {
			this.passAuth = passAuth;
			return this;
		}

		public SnmpAuthBuilder privType(String privType) {
			this.privType = privType;
			return this;
		}

		public SnmpAuthBuilder privPass(String privPass) {
			this.privPass = privPass;
			return this;
		}

		public SnmpAuthBuilder ip(String ip) {
			this.ip = ip;
			return this;
		}

		public SnmpAuthBuilder port(int port) {
			this.port = port;
			return this;
		}

		public SnmpAuthBuilder ipType(int ipType) {
			this.ipType = ipType;
			return this;
		}

		public SnmpAuth build() {
			SnmpAuth snmpAuth = new SnmpAuth();
			snmpAuth.setVersion(version);
			snmpAuth.setCommunity(community);
			snmpAuth.setSecurityModel(securityModel);
			snmpAuth.setSecurityLevel(securityLevel);
			snmpAuth.setSecurityName(securityName);
			snmpAuth.setAuthType(authType);
			snmpAuth.setPassAuth(passAuth);
			snmpAuth.setPrivType(privType);
			snmpAuth.setPrivPass(privPass);
			snmpAuth.setIp(ip);
			snmpAuth.setPort(port);
			snmpAuth.setIpType(ipType);
			return snmpAuth;
		}
	}
}
