package com.feng.baseframework.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * baseframework
 * 2019/12/4 9:51
 * keyStore实体
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Setter
public class KeyStoreParam extends BaseSecretKeyParam implements Serializable {

	//文件路径
	@NonNull
	private String filePath;
	//秘钥库密码
	private String storePassword;
	//别名密码
	private String keyPassword;
	//别名
	@NonNull
	private String alias;
	//密钥仓库类型
	private String storeType;

	//名字与姓氏
	@NonNull
	private String commonName;
	//组织单位名称
	@NonNull
	private String organizationalUnit;
	//组织名称
	@NonNull
	private String organization;
	//城市或区域名称
	@NonNull
	private String city;
	//省/市/自治区名称
	@NonNull
	private String province;
	//双字母国家/地区代码
	@NonNull
	private String country;

	//证书有效期,单位天
	private long validity;

	public static KeyStoreParam buildDefault(){
		KeyStoreParam keyStoreParam = new KeyStoreParam();
		keyStoreParam.setKeySize(512);
		keyStoreParam.setAlgorithm("RSA");

		keyStoreParam.setFilePath("/data/" + UUID.randomUUID().toString() + ".keystore");
		keyStoreParam.setStorePassword("feng!@#");
		keyStoreParam.setKeyPassword("feng!@#456");
		keyStoreParam.setAlias("testca");
		keyStoreParam.setStoreType("jks");

		keyStoreParam.setCommonName("com.feng.baseframework");
		keyStoreParam.setOrganizationalUnit("feng");
		keyStoreParam.setOrganization("feng");
		keyStoreParam.setCity("HZ");
		keyStoreParam.setProvince("ZJ");
		keyStoreParam.setCountry("CN");

		keyStoreParam.setValidity(365);
		return keyStoreParam;
	}

	@Override
	public String toString() {
		return "KeyStoreParam{" +
				"filePath='" + filePath + '\'' +
				", storePassword='" + storePassword + '\'' +
				", keyPassword='" + keyPassword + '\'' +
				", alias='" + alias + '\'' +
				", storeType='" + storeType + '\'' +
				", commonName='" + commonName + '\'' +
				", organizationalUnit='" + organizationalUnit + '\'' +
				", organization='" + organization + '\'' +
				", city='" + city + '\'' +
				", province='" + province + '\'' +
				", country='" + country + '\'' +
				", validity=" + validity +
				'}';
	}
}
