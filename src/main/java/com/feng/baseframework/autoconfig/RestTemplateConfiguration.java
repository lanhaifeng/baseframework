package com.feng.baseframework.autoconfig;

import com.feng.baseframework.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * restTemplate配置类
 *
 * @author lanhaifeng
 * @apiNote 时间:2023/6/27 20:37创建:RestTemplateTest
 * @date 2023/06/28
 * @since 1.0
 */
@Configuration
public class RestTemplateConfiguration {

    @Value("${spring.https.key-store-password}")
    private String keyStorePassword;
    @Value("${spring.https.key-password}")
    private String keyPassword;
    @Value("${spring.https.key-file}")
    private String keyFile;
    @Value("${spring.https.trust-password}")
    private String trustPassword;
    @Value("${spring.https.trust-file}")
    private String trustFile;
    @Value("${spring.https.ssl-protocols}")
    private String sslProtocols;
    @Value("${spring.https.ciphers}")
    private String ciphers;
    @Value("${spring.https.trust-server}")
    private Boolean trustServer;
    @Value("${spring.https.mutual-authentication}")
    private Boolean mutualAuthentication;

    @Bean("restTemplate")
    @ConditionalOnMissingBean({ RestOperations.class, RestTemplate.class })
    public RestTemplate restTemplate(){
        return buildRestTemplate(simpleClientHttpRequestFactory());
    }

    @Bean("sshIgnoreVerificationRestTemplate")
    public RestTemplate sshIgnoreVerificationRestTemplate() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(sshIgnoreVerificationClientHttpRequestFactory());
    }

    @Bean("sshIgnoreVerificationRestTemplate2")
    public RestTemplate sshIgnoreVerificationRestTemplate2() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return buildRestTemplate(sshIgnoreVerificationClientHttpRequestFactory2());
    }

    @Bean("sshRestTemplate")
    public RestTemplate sshRestTemplate() throws Exception {
        return buildRestTemplate(sshClientHttpRequestFactory());
    }

    /**
     * 构建Rest工具模板
     *
     * @param factory 工厂
     * @return {@link RestTemplate}
     */
    public RestTemplate buildRestTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(@Nullable ClientHttpResponse response) throws IOException {
                assert response != null;
                super.handleError(response);
            }
        });
        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();

        FormHttpMessageConverter fc = new FormHttpMessageConverter();
        StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<HttpMessageConverter<?>> partConverters = new ArrayList<>();
        partConverters.add(s);
        partConverters.add(new ResourceHttpMessageConverter());
        fc.setPartConverters(partConverters);

        converterList.add(fc);

        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(converter);
        return restTemplate;
    }

    /**
     * 简单客户端http请求工厂
     *
     * @return {@link ClientHttpRequestFactory}
     */
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(3000);// ms
        factory.setConnectTimeout(3000);// ms
        return factory;
    }

    /**
     * 支持https并忽略证书、域名检测
     * @return                              返回http连接创建工厂{@link HttpComponentsClientHttpRequestFactory}
     * @throws KeyStoreException            异常
     * @throws NoSuchAlgorithmException     异常
     * @throws KeyManagementException       异常
     */
    public ClientHttpRequestFactory sshIgnoreVerificationClientHttpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // https
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(buildHttpClientConnectionManager(sslConnectionSocketFactory))
                .setConnectionManagerShared(true)
                //重试机制
                .setRetryStrategy(new DefaultHttpRequestRetryStrategy(2, TimeValue.ofMilliseconds(500)))
                .build();

        HttpComponentsClientHttpRequestFactory factory = new
                HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        return factory;
    }

    /**
     * 构建http客户端连接管理器
     *
     * @param sslConnectionSocketFactory ssl连接套接字工厂
     * @return {@link HttpClientConnectionManager}
     */
    public HttpClientConnectionManager buildHttpClientConnectionManager(SSLConnectionSocketFactory sslConnectionSocketFactory) {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory).build();
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
        // 最大连接数
        phccm.setMaxTotal(200);
        // 同路由并发数
        phccm.setDefaultMaxPerRoute(400);

        return phccm;
    }

    /**
     * ssh忽略http请求factory验证客户端
     *
     * @return {@link ClientHttpRequestFactory}
     * @throws KeyStoreException        密钥存储异常
     * @throws NoSuchAlgorithmException 没有这样算法异常
     * @throws KeyManagementException   密钥管理例外
     */
    public ClientHttpRequestFactory sshIgnoreVerificationClientHttpRequestFactory2() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .evictExpiredConnections()
                //空闲连接时间
                .evictIdleConnections(TimeValue.ofMilliseconds(5000))
                //保活策略
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .setConnectionManager(buildHttpClientConnectionManager(sslConnectionSocketFactory))
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        //连接超时时间
        factory.setConnectTimeout(3000);

        return factory;
    }

    /**
     * 支持https
     * @return                          返回http连接创建工厂{@link HttpComponentsClientHttpRequestFactory}
     * @throws KeyStoreException        异常
     * @throws NoSuchAlgorithmException 异常
     * @throws KeyManagementException   异常
     */
    public ClientHttpRequestFactory sshClientHttpRequestFactory() throws Exception {
        //在握手期间，如果URL的主机名和服务器的标识主机名不匹配，则验证机制可以回调此接口的实现程序来确定是否应该允许此连接
        HostnameVerifier hv = (urlHostName, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        //构建SSL-Socket链接工厂
        SSLConnectionSocketFactory ssLSocketFactory = buildSSLSocketFactory(KeyStore.getDefaultType(),
                keyFile, keyStorePassword, keyPassword,
                Arrays.asList(StringUtils.split(sslProtocols)), Arrays.asList(StringUtils.split(ciphers)),
                trustServer, trustFile, trustPassword, mutualAuthentication);

        //Spring提供HttpComponentsClientHttpRequestFactory指定使用HttpClient作为底层实现创建 HTTP请求
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom().setConnectionManager(buildHttpClientConnectionManager(ssLSocketFactory)).build()
        );
        //设置建立连接超时时长
        factory.setConnectTimeout(3000);
        //设置获取连接超时时长
        factory.setConnectionRequestTimeout(1000);

        return factory;
    }

    /**
     * 构建SSLSocketFactory
     *
     * @param keyStoreType          秘钥类型
     * @param keyFilePath           秘钥文件路径
     * @param keyPassword           秘钥密码
     * @param sslProtocols          ssl协议
     * @param auth 是否需要client默认相信不安全证书
     * @return                      返回ssl连接工厂类
     * @throws Exception            抛出异常
     */
    private SSLConnectionSocketFactory buildSSLSocketFactory(String keyStoreType, String keyFilePath,
                                                             String keyStorePassword, String keyPassword,
                                                             List<String> sslProtocols, List<String> ciphers,
                                                             boolean auth,
                                                             String trustFilePath, String trustPassword,
                                                             boolean mutualAuthentication) throws Exception {
        //证书管理器，指定证书及证书类型
        KeyManagerFactory keyManagerFactory = null;
        //信任证书管理器
        TrustManagerFactory trustManagerFactory;
        //KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
        KeyStore keyStore;
        KeyStore trustKeyStore;

        if (mutualAuthentication) {
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            //KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
            keyStore = KeyStore.getInstance(keyStoreType);
            try (InputStream inputStream = FileUtils.getResource(keyFilePath)) {
                //添加证书
                keyStore.load(inputStream, keyStorePassword.toCharArray());
            }
            keyManagerFactory.init(keyStore, keyPassword.toCharArray());
        }

        SSLContext sslContext = SSLContext.getInstance("SSL");
        if (auth) {
            // 设置信任证书（绕过TrustStore验证）
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager trustManager = new AuthX509TrustManager();
            trustAllCerts[0] = trustManager;
            sslContext.init(keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers(), trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        } else {
            //加载证书材料，构建sslContext
            trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustKeyStore = KeyStore.getInstance(keyStoreType);
            try (InputStream inputStream = FileUtils.getResource(trustFilePath)) {
                //添加证书
                trustKeyStore.load(inputStream, trustPassword.toCharArray());
            }
            trustManagerFactory.init(trustKeyStore);
            sslContext.init(keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        }

        //方法1
        //sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        //忽略主机认证
        //方法2
        // 这里不校验hostname
        return new SSLConnectionSocketFactory(sslContext, sslProtocols.toArray(new String[0]),
                ciphers.toArray(new String[0]),
                (urlHostName, session) -> true);
    }

}

class AuthX509TrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }
}
