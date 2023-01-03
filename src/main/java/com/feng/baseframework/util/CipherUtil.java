package com.feng.baseframework.util;

import com.feng.baseframework.constant.CipherType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * baseframework
 * 2019/3/14 10:59
 * 加密解密数据工具类
 *
 * @author lanhaifeng
 * @since  v1.0
 **/
@Slf4j
public final class CipherUtil {

	/**
	 * 2019/8/6 10:07
	 * 非对称解密
	 *
	 * @param key			秘钥
	 * @param data			待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] decryptRSA(Key key, byte[] data) {
		return decryptOrEncrypt(key, "RSA", data, CipherType.DECRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 非对称解密
	 *
	 * @param key			秘钥
	 * @param algorithm		算法
	 * @param data			待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] decryptRSA(Key key, String algorithm, byte[] data) {
		return decryptOrEncrypt(key, algorithm, data, CipherType.DECRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 非对称加密
	 *
	 * @param key						秘钥
	 * @param data						待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] encryptRSA(Key key, byte[] data) {
		return decryptOrEncrypt(key, "RSA", data, CipherType.ENCRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 非对称加密
	 *
	 * @param key						秘钥
	 * @param algorithm					算法
	 * @param data						待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] encryptRSA(Key key, String algorithm, byte[] data) {
		return decryptOrEncrypt(key, algorithm, data, CipherType.ENCRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 对称解密
	 *
	 * @param key			秘钥
	 * @param data			待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] decryptAES(Key key, byte[] data) {
		return decryptOrEncrypt(key, "AES", data, CipherType.DECRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 对称加密
	 *
	 * @param key						秘钥
	 * @param data						待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] encryptAES(Key key, byte[] data) {
		return decryptOrEncrypt(key, "AES", data, CipherType.ENCRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 对称解密
	 *
	 * @param key			秘钥
	 * @param algorithm		算法
	 * @param data			待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] decryptAES(Key key, String algorithm, byte[] data) {
		return decryptOrEncrypt(key, algorithm, data, CipherType.DECRYPT);
	}

	/**
	 * 2019/8/6 10:07
	 * 对称加密
	 *
	 * @param key						秘钥
	 * @param algorithm					算法
	 * @param data						待处理字符串
	 * @author lanhaifeng
	 * @return byte[]
	 */
	public static byte[] encryptAES(Key key, String algorithm, byte[] data) {
		return decryptOrEncrypt(key, algorithm, data, CipherType.ENCRYPT);
	}

	/**
	 * 2019/8/6 10:06
	 * 加解密
	 *
	 * @param key						秘钥
	 * @param algorithm					加密算法，还可以使用类似，算法/模式/填充方式，如：Cipher.getInstance("DES/ECB/PKCS5Padding")
	 * @param data						待处理字节数组
	 * @param cipherType				处理类型，加密、解密
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static byte[] decryptOrEncrypt(Key key, String algorithm, byte[] data, CipherType cipherType) {
		try {
			// 创建Cipher对象，ECB模式的DES算法。
			Cipher cipher = Cipher.getInstance(algorithm);
			//初始化Cipher对象
			cipher.init(cipherType.getValue(), key);

			return cipher.doFinal(data);
		}catch (NoSuchAlgorithmException e) {
			log.error("无此加密算法");
		} catch (NoSuchPaddingException e) {
			log.error("加密算法初始化失败");
		} catch (InvalidKeyException e) {
			log.error("非法私钥");
		} catch (IllegalBlockSizeException e) {
			log.error("密文长度非法");
		} catch (BadPaddingException e) {
			log.error("密文数据已损坏");
		}

		return null;
	}

	/**
	 * 字符转字节数组
	 * @param src       待处理字符串
	 * @param charset   字符集
	 * @param isHexStr  是否16进制字符串转2进制数组
	 * @return
	 */
	public static byte[] parseStr2Byte(String src, String charset, boolean isHexStr){
		if(isHexStr){
			return StringUtil.parseHexStr2Byte(src);
		}
		if(StringUtils.isNotBlank(charset)){
			try {
				return src.getBytes(charset);
			} catch (UnsupportedEncodingException e) {
				log.error("无此字符集");
			}
		}
		return src.getBytes();
	}

	/**
	 * 字节数组转字符串
	 * @param src		  待处理2进制数组
	 * @param charset 	  字符集
	 * @param isToHexStr  是否2进制数组转16进制字符串
	 * @return
	 */
	public static String parseByte2Str(byte[] src, String charset, boolean isToHexStr){
		if(isToHexStr){
			return StringUtil.bytesToHexString(src);
		}
		if(StringUtils.isNotBlank(charset)){
			try {
				return new String(src, charset);
			} catch (UnsupportedEncodingException e) {
				log.error("无此字符集");
			}
		}
		return new String(src);
	}

}
