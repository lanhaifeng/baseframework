package com.feng.baseframework.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

/**
 * baseframework
 * 2019/7/1 10:18
 * jasypt加密解密工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class JasyptUtils {

	/**
	 * Jasypt生成加密结果
	 *
	 * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
	 * @param value    待加密值
	 * @return
	 */
	public static String encryptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
		encryptOr.setConfig(cryptOr(password));
		String result = encryptOr.encrypt(value);
		return result;
	}

	/**
	 * 解密
	 *
	 * @param password 配置文件中设定的加密密码 jasypt.encryptor.password
	 * @param value    待解密密文
	 * @return
	 */
	public static String decyptPwd(String password, String value) {
		PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
		encryptOr.setConfig(cryptOr(password));
		String result = encryptOr.decrypt(value);
		return result;
	}

	public static SimpleStringPBEConfig cryptOr(String password) {
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(password);
		config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
		config.setKeyObtentionIterations("1000");
		config.setPoolSize("1");
		config.setProviderName("SunJCE");
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		config.setStringOutputType("base64");
		return config;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException {
		// 加密
		System.out.println(encryptPwd("pantherpantherpanther", "rootrootrootroot"));
		// 解密
		System.out.println(decyptPwd("pantherpantherpanther", "3RbBF1ePIPLuE6/jvT1htg=="));

		SecretKey secretKey = SecretKeyUtil.generateSecretKey("AES", "pantherpantherpanther");
		String result = Base64EncryptUtil.encode(CipherUtil.encryptAES(secretKey, "AES/CBC/PKCS5Padding", "rootrootrootroot".getBytes()));
		System.out.println(result);
		result = Base64EncryptUtil.encode(CipherUtil.encryptAES(secretKey,"AES/CBC/PKCS5Padding", "rootrootrootroot".getBytes()));
		System.out.println(result);
	}
}
