package com.feng.baseframework.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/**
 * baseframework
 * 2019/2/21 16:40
 * base64编码解码工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class Base64EncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(Base64EncryptUtil.class);

	public static void main(String[] args) {
		String sql = "SELECT T.INST_ID ||T.SID ||T.SERIAL #||T.AUDSID AS SSID FROM GV$SESSION T UNION ALL SELECT T.INST_ID ||T.SID ||T.SERIAL #||TO_CHAR ( T.LOGON_TIME , ? ) AS SSID FROM GV$SESSION T";
		System.out.println(encode(sql.getBytes()));

		sql = "IydTRUxFQ1QgKiBGUk9NIERFUFQgV0hFUkUgREVQVE5PID0gMTAgQU5EIDE0MzMgPSBEQk1TX1BJ\\r\\nUEUuUkVDRUlWRV9NRVNTQUdFICggQ0hSICggNzEgKSB8fENIUiAoIDEwMCApIHx8Q0hSICggNzAg\\r\\nKSAsIDUgKQ==";
		System.out.println(decode(sql));

		sql = "eQySDnqLR5vcShmZ/HX+coSw9rupETKWjruw57O/yAFHD/SdV3ljYiMkKZchetcBx2CKcbSb2/yWyjnTDHhLVBa+sBtyr3eF/IsBHMLs44bl2JGzj5AJosSGcobGMVqRouLUQbOI+1dzeWCVEBWFfhfOOKu78kpDqX4iYh8jD9rZjpNQLrfbEz3CtX3iXNWrsWhvaC24t9aTjOLDcG7YpelFygkd9OvA9U+fr3s87ylqJQRzWRMS1Tf4aiioO+dicYmfqut/LcsZ2/fiJLKFVYkck5m1a/ZxOY7gX3pYL0THUDG6UDZNty1KEG2EzzD3en6u4VqB7ZhV3ha2eTsMMg==";
		System.out.println(encode(sql.getBytes()));
	}

	/**
	 * 编码
	 *
	 * @param bstr
	 * @return String
	 */
	public static String encode(byte[] bstr) {
		try {
			return new BASE64Encoder().encode(bstr);
		} catch (Exception e) {
			logger.error("编码失败：" + ExceptionUtils.getFullStackTrace(e));
		}
		return "";
	}

	/**
	 * 解码
	 *
	 * @param str
	 * @return string
	 */
	public static String decode(String str) {
		byte[] bt = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			bt = decoder.decodeBuffer(str);
			return new String(bt);
		} catch (IOException e) {
			logger.error("解码失败：" + ExceptionUtils.getFullStackTrace(e));
			return "";
		}

	}

	public static byte[] decodeToBytes(String str) {
		byte[] bt = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			bt = decoder.decodeBuffer(str);
			return bt;
		} catch (IOException e) {
			logger.error("解码失败：" + ExceptionUtils.getFullStackTrace(e));
			return null;
		}

	}
}
