package com.feng.baseframework.util;

import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class CertificateUtilTest {

	@Test
	public void generateSignAndValidateSign() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
		KeyStore keyStore = SecretKeyUtil.loadKeyStore(FileUtils.getFile("classpath:ssl/server.keystore").getAbsolutePath(), "server123");
		PrivateKey privateKey = SecretKeyUtil.getPrivateKeyFromKeyStore(keyStore, "server", "server123");
		PublicKey publicKey = SecretKeyUtil.getPublicKeyFromKeyStore(keyStore, "server");

		String data = "hoadjfapdjapd";
		String sign = CertificateUtil.generateSign(privateKey, data, "MD2withRSA");
		boolean result = CertificateUtil.validateSign(sign, data, publicKey, "MD2withRSA");

		Assert.state(StringUtils.isNotBlank(sign), "签名失败");
		Assert.state(result, "验证签名失败");
	}

}