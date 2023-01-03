package com.feng.baseframework.security;

import com.feng.baseframework.util.Base64EncryptUtil;
import com.feng.baseframework.util.CipherUtil;
import com.feng.baseframework.util.SecretKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * baseframework
 * 2019/11/11 15:42
 * 自定义的密码加密方法
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
@Component
public class MyPasswordEncoder implements PasswordEncoder {

	@Value("${spring.security.salt}")
	private String salt;

	@Override
	public String encode(CharSequence charSequence) {
		String encodeStr = charSequence == null ? "" : charSequence.toString();

		try {
			SecretKey secretKey = SecretKeyUtil.generateSecretKey(salt);
			encodeStr = Base64EncryptUtil.encode(CipherUtil.encryptAES(secretKey, encodeStr.getBytes()));
		} catch (Exception e) {
			log.error("解密失败，错误：" + ExceptionUtils.getFullStackTrace(e));
			encodeStr = null;
		}
		return encodeStr;
	}

	@Override
	public boolean matches(CharSequence charSequence, String s) {
		return encode(charSequence).equals(s);
	}
}
