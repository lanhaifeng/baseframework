package com.feng.baseframework.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * baseframework
 * 2019/3/11 18:03
 * 证书工具类
 *
 * @author lanhaifeng
 * @since  v1.0
 **/
public class CertificateUtil {

	private static final Log logger = LogFactory.getLog(CertificateUtil.class);

	/**
	 * 生成签名
	 * @param priKey		私钥
	 * @param info			签名内容
	 * @param algorithm		签名算法，如：MD5WithRSA，见javadoc<a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#Signature" />
	 * @return				签名
	 */
	public static String generateSign(PrivateKey priKey, String info, String algorithm){
		String base64Sign = "";
		InputStream fis = null;
		try {
			// 签名
			Signature sign = Signature.getInstance(algorithm);
			sign.initSign(priKey);
			byte[] bysData = info.getBytes();
			sign.update(bysData);
			byte[] signByte = sign.sign();

			base64Sign = StringUtil.bytesToHexString(signByte);
		} catch (Exception e) {
			logger.error("签名出现错误：" + ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error("关闭流出现错误：" + ExceptionUtils.getFullStackTrace(e));
				}
			}
		}
		return base64Sign;
	}

	/**
	 * 验证签名
	 *
	 * @param sign 签名
	 * @param info 实际信息
	 * @param publicKey 公钥
	 * @param algorithm 签名算法，如：MD5WithRSA，见javadoc<a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#Signature" />
	 * @return true 验证成功 false 验证失败
	 */
	public static boolean validateSign(String sign, String info, PublicKey publicKey, String algorithm) {
		try {
			// 初始一个Signature对象,并用公钥和签名进行验证
			Signature signetcheck = Signature.getInstance(algorithm);
			// 初始化验证签名的公钥
			signetcheck.initVerify(publicKey);
			// 使用指定的 byte 数组更新要签名或验证的数据
			signetcheck.update(info.getBytes());
			// 验证传入的签名
			return signetcheck.verify(StringUtil.parseHexStr2Byte(sign));
		} catch (Exception e) {
			logger.error("验证签名出现错误：" + ExceptionUtils.getFullStackTrace(e));
			return false;
		}
	}

}
