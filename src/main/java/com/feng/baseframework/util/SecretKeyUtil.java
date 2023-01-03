package com.feng.baseframework.util;

import com.feng.baseframework.model.KeyStoreParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Objects;

/**
 * baseframework
 * 2019/12/3 17:12
 * 秘钥工具类，读取和生成非对称公、私钥
 *
 * @author lanhaifeng
 * @since v1.0
 **/
public final class SecretKeyUtil {

	/**
	 * 2019/12/4 16:31
	 * 生成密钥对
	 *
	 * @param algorithm			加密算法，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyPairGenerator" />
	 * @param keySize			秘钥长度
	 * @param random			安全随机源
	 * @author lanhaifeng
	 * @return java.security.KeyPair
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize, SecureRandom random) throws NoSuchAlgorithmException {
		// 获取指定算法的密钥对生成器
		KeyPairGenerator gen = KeyPairGenerator.getInstance(algorithm);

		// 初始化密钥对生成器（指定密钥长度, 指定安全随机数源）
		gen.initialize(keySize, random);

		// 随机生成一对密钥（包含公钥和私钥）
		return gen.generateKeyPair();
	}

	/**
	 * 2019/12/4 15:48
	 * 生成密钥对
	 *
	 * @param algorithm				加密算法，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyPairGenerator" />
 	 * @param keySize				秘钥长度
	 * @author lanhaifeng
	 * @return java.security.KeyPair
	 */
	public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
		// 获取指定算法的密钥对生成器
		KeyPairGenerator gen = KeyPairGenerator.getInstance(algorithm);

		// 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
		gen.initialize(keySize);

		// 随机生成一对密钥（包含公钥和私钥）
		return gen.generateKeyPair();
	}

	/**
	 * 2019/12/4 15:48
	 * 生成密钥对
	 *
	 * @author lanhaifeng
	 * @return java.security.KeyPair
	 */
	public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		return generateKeyPair("RSA", 512);
	}

	/**
	 * 2019/12/4 19:53
	 * 生成对称加密秘钥
	 *
	 * @param password		密码
	 * @author lanhaifeng
	 * @return javax.crypto.SecretKey
	 */
	public static SecretKey generateSecretKey(String password) throws NoSuchAlgorithmException {
		return generateSecretKey("AES", password);
	}

	/**
	 * 2019/12/4 19:53
	 * 生成对称加密秘钥
	 *
	 * @param algorithm			加密算法，如：AES、AES/ECB/PKCS5Padding
	 * @param password			密码
	 * @author lanhaifeng
	 * @return javax.crypto.SecretKey
	 */
	public static SecretKey generateSecretKey(String algorithm, String password) throws NoSuchAlgorithmException {
		KeyGenerator gen = KeyGenerator.getInstance(algorithm);
		gen.init(128, new SecureRandom(password.getBytes()));

		return gen.generateKey();
	}

	/**
	 * 2019/12/4 15:49
	 * 生成一个keystore文件并保存到文件
	 *
	 * @param keyStoreParam		keystore需要的参数
	 * @author lanhaifeng
	 * @return void
	 */
	public static void generateKeyStoreFile(KeyStoreParam keyStoreParam) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, NoSuchProviderException, SignatureException, InvalidKeyException {
		Objects.requireNonNull(keyStoreParam);

		//获取keystore实例，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyStore" />
		KeyStore keyStore = KeyStore.getInstance(keyStoreParam.getStoreType());
		char[] storePassword = StringUtils.isNotBlank(keyStoreParam.getStorePassword()) ? keyStoreParam.getStorePassword().trim().toCharArray() : null;
		//装载keystore
		keyStore.load(null, null);

		//生成秘钥对
		CertAndKeyGen keyPair = new CertAndKeyGen("RSA", "SHA1WithRSA", null);
		keyPair.generate(keyStoreParam.getKeySize());

		//生成私钥
		PrivateKey privateKey = keyPair.getPrivateKey();
		//组织、区域等信息
		X500Name x500Name = new X500Name(keyStoreParam.getCommonName(), keyStoreParam.getOrganizationalUnit(), keyStoreParam.getOrganization(), keyStoreParam.getCity(), keyStoreParam.getProvince(), keyStoreParam.getCountry());
		//构造证书链对象
		X509Certificate[] chain = new X509Certificate[1];
		//生成证书对象
		chain[0] = keyPair.getSelfCertificate(x500Name, new Date(), keyStoreParam.getValidity() * 24 * 60 * 60l);

		FileOutputStream fos = new FileOutputStream(keyStoreParam.getFilePath());
		char[] keyPassword = StringUtils.isNotBlank(keyStoreParam.getKeyPassword()) ? keyStoreParam.getKeyPassword().trim().toCharArray() : null;
		//秘钥对存入keystore
		keyStore.setKeyEntry(keyStoreParam.getAlias(), privateKey, keyPassword, chain);
		//生成keystore文件
		keyStore.store(fos, storePassword);
		fos.close();
	}

	/**
	 * 2019/12/4 15:48
	 * 通过keystore文件路径得到keystore
	 *
	 * @param storePath					秘钥库路径
	 * @param storePassword				秘钥库密码
	 * @param type						类型，如：jks，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyStore" />
	 * @author lanhaifeng
	 * @return java.security.KeyStore
	 */
	public static KeyStore loadKeyStore(String storePath, String storePassword, String type) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
		//获取keystore实例
		KeyStore keyStore = KeyStore.getInstance(type);
		//装载keystore
		keyStore.load(new FileInputStream(new File(storePath)), storePassword.toCharArray());

		return keyStore;
	}

	/**
	 * 2019/12/4 15:48
	 * 通过keystore文件路径得到keystore
	 *
	 * @param storePath					秘钥库路径
	 * @param storePassword				秘钥库密码
	 * @author lanhaifeng
	 * @return java.security.KeyStore
	 */
	public static KeyStore loadKeyStore(String storePath, String storePassword) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
		return loadKeyStore(storePath, storePassword, "jks");
	}

	/**
	 * 2019/12/4 16:02
	 * 从keystore中获取私钥
	 *
	 * @param keyStore				秘钥库管理工具
	 * @param alias					秘钥别名
	 * @param keyPassword			秘钥密码
	 * @author lanhaifeng
	 * @return java.security.PrivateKey
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(KeyStore keyStore, String alias, String keyPassword) throws UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException {
		Objects.requireNonNull(keyStore);
		Assert.state(StringUtils.isNotBlank(alias), "alias参数不能为空");
		return (PrivateKey)keyStore.getKey(alias, StringUtils.isNotBlank(keyPassword) ? keyPassword.toCharArray() : null);
	}

	/**
	 * 2019/12/4 16:03
	 * 从keystore中获取公钥
	 *
	 * @param keyStore			秘钥库管理工具
	 * @param alias				秘钥别名
	 * @author lanhaifeng
	 * @return java.security.PublicKey
	 */
	public static PublicKey getPublicKeyFromKeyStore(KeyStore keyStore, String alias) throws KeyStoreException {
		Objects.requireNonNull(keyStore);
		Assert.state(StringUtils.isNotBlank(alias), "alias参数不能为空");
		return keyStore.getCertificate(alias).getPublicKey();
	}

	/**
	 * 2019/12/4 16:06
	 * 将秘钥转为字符串
	 *
	 * @param key				秘钥实例
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String parseKey(Key key){
		Objects.requireNonNull(key);
		return Base64EncryptUtil.encode(key.getEncoded());
	}

	/**
	 * 2019/12/4 16:16
	 * 用字符串加载公钥
	 *
	 * @param algorithm				加密算法，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyFactory" />
	 * @param keySpec				秘钥编码规则实例
	 * @author lanhaifeng
	 * @return java.security.Key
	 */
	public static PublicKey loadPublicKeyFromString(String algorithm, KeySpec keySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Objects.requireNonNull(keySpec);
		Assert.state(StringUtils.isNotBlank(algorithm), "algorithm参数不能为空");
		//获取指定算法的密钥工厂, 根据已编码的公钥规格, 生成公钥对象
		return KeyFactory.getInstance(algorithm).generatePublic(keySpec);
	}

	/**
	 * 2019/12/4 16:18
	 * 用字符串加载公钥
	 *
	 * @param keyBase64Str			秘钥base64字符串
	 * @author lanhaifeng
	 * @return java.security.PublicKey
	 */
	public static PublicKey loadPublicKeyFromString(String keyBase64Str) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Assert.state(StringUtils.isNotBlank(keyBase64Str), "keyBase64Str参数不能为空");
		byte[] encPubKey = Base64EncryptUtil.decodeToBytes(keyBase64Str);
		//创建已编码的公钥规格
		X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);
		return loadPublicKeyFromString("RSA", encPubKeySpec);
	}

	/**
	 * 2019/12/4 17:05
	 * 读取字符串文件加载公钥
	 *
	 * @param publicKeyPath
	 * @author lanhaifeng
	 * @return java.security.PublicKey
	 */
	public static PublicKey loadPublicKeyFromFile(String publicKeyPath) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		String pubKeyBase64 = FileUtils.readFile(publicKeyPath);
		return loadPublicKeyFromString(pubKeyBase64);
	}

	/**
	 * 2019/12/4 17:05
	 * 读取对象流文件加载公钥
	 *
	 * @param publicKeyPath
	 * @author lanhaifeng
	 * @return java.security.PublicKey
	 */
	public static PublicKey loadPublicKeyFromObjFile(String publicKeyPath) throws IOException, ClassNotFoundException {
		Assert.state(StringUtils.isNotBlank(publicKeyPath), "publicKeyPath参数不能为空");
		return FileUtils.readObject(publicKeyPath);
	}

	/**
	 * 2019/12/4 16:16
	 * 用字符串加载私钥
	 *
	 * @param algorithm				加密算法，见javadoc <a href="/jdk_64_1.8/jdk8docs/technotes/guides/security/StandardNames.html#KeyFactory" />
	 * @param keySpec				秘钥编码规则实例
	 * @author lanhaifeng
	 * @return java.security.Key
	 */
	public static PrivateKey loadPrivateKeyFromString(String algorithm, KeySpec keySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
		Objects.requireNonNull(keySpec);
		Assert.state(StringUtils.isNotBlank(algorithm), "algorithm参数不能为空");
		//获取指定算法的密钥工厂, 根据已编码的私钥规格, 生成私钥对象
		return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
	}

	/**
	 * 2019/12/4 16:18
	 * 用字符串加载私钥
	 *
	 * @param keyBase64Str			秘钥base64字符串
	 * @author lanhaifeng
	 * @return java.security.PublicKey
	 */
	public static PrivateKey loadPrivateKeyFromString(String keyBase64Str) throws InvalidKeySpecException, NoSuchAlgorithmException {
		Assert.state(StringUtils.isNotBlank(keyBase64Str), "keyBase64Str参数不能为空");
		byte[] encPriKey = Base64EncryptUtil.decodeToBytes(keyBase64Str);
		// 创建已编码的私钥规格
		PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);
		return loadPrivateKeyFromString("RSA", encPriKeySpec);
	}

	/**
	 * 2019/12/4 17:05
	 * 读取对象流文件加载公钥
	 *
	 * @param privateKeyPath
	 * @author lanhaifeng
	 * @return java.security.PrivateKey
	 */
	public static PrivateKey loadPrivateKeyFromObjFile(String privateKeyPath) throws IOException, ClassNotFoundException {
		Assert.state(StringUtils.isNotBlank(privateKeyPath), "privateKeyPath参数不能为空");
		return FileUtils.readObject(privateKeyPath);
	}

}
