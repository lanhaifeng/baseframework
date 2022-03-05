package com.feng.baseframework.autoconfig;

import com.feng.baseframework.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
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
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.FileInputStream;
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
 * @ProjectName: baseframework
 * @Description: restTemplate配置类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/1 23:30
 * @UpdateUser:
 * @UpdateDate: 2018/5/1 23:30
 * @UpdateRemark:
 * @Version: 1.0
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


    public RestTemplate buildRestTemplate(ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(restTemplateErrorHandler());
        List<HttpMessageConverter<?>> converterList=restTemplate.getMessageConverters();

        FormHttpMessageConverter fc = new FormHttpMessageConverter();
        StringHttpMessageConverter s = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        List<HttpMessageConverter<?>> partConverters = new ArrayList<HttpMessageConverter<?>>();
        partConverters.add(s);
        partConverters.add(new ResourceHttpMessageConverter());
        fc.setPartConverters(partConverters);

        converterList.add(fc);

        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converterList.add(converter);
        return restTemplate;
    }

    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(3000);// ms
        factory.setConnectTimeout(3000);// ms
        return factory;
    }

    /**
     * 支持https并忽略证书、域名检测
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public ClientHttpRequestFactory sshIgnoreVerificationClientHttpRequestFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpComponentsClientHttpRequestFactory factory = new
                HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(180000);
        // https
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", socketFactory).build();
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
        phccm.setMaxTotal(200);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(socketFactory)
                .setConnectionManager(phccm)
                .setConnectionManagerShared(true)
                //重试机制
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                .build();
        factory.setHttpClient(httpClient);

        return factory;
    }

    public ClientHttpRequestFactory sshIgnoreVerificationClientHttpRequestFactory2() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(180000);

        return factory;
    }

    /**
     * 支持https
     * @return
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public ClientHttpRequestFactory sshClientHttpRequestFactory() throws Exception {
        //在握手期间，如果URL的主机名和服务器的标识主机名不匹配，则验证机制可以回调此接口的实现程序来确定是否应该允许此连接
        HostnameVerifier hv = new HostnameVerifier() {
            @Override
            public boolean verify(String urlHostName, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
        //构建SSL-Socket链接工厂
        SSLConnectionSocketFactory ssLSocketFactory = buildSSLSocketFactory(KeyStore.getDefaultType(),
                keyFile, keyStorePassword, keyPassword,
                Arrays.asList(StringUtils.split(sslProtocols)), trustServer, trustFile, trustPassword, mutualAuthentication);
        //Spring提供HttpComponentsClientHttpRequestFactory指定使用HttpClient作为底层实现创建 HTTP请求
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(
                HttpClients.custom().setSSLSocketFactory(ssLSocketFactory).build()
        );
        //设置传递数据超时时长
        factory.setReadTimeout(180000);
        //设置建立连接超时时长
        factory.setConnectTimeout(3000);
        //设置获取连接超时时长
        factory.setConnectionRequestTimeout(1000);

        return factory;
    }

    /**
     * 构建SSLSocketFactory
     *
     * @param keyStoreType
     * @param keyFilePath
     * @param keyPassword
     * @param sslProtocols
     * @param auth 是否需要client默认相信不安全证书
     * @return
     * @throws Exception
     */
    private SSLConnectionSocketFactory buildSSLSocketFactory(String keyStoreType, String keyFilePath,
                                                             String keyStorePassword, String keyPassword, List<String> sslProtocols, boolean auth, String trustFilePath, String trustPassword, boolean mutualAuthentication) throws Exception {
        //证书管理器，指定证书及证书类型
        KeyManagerFactory keyManagerFactory = null;
        //信任证书管理器
        TrustManagerFactory trustManagerFactory = null;
        //KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
        KeyStore keyStore = null;
        KeyStore trustKeyStore = null;

        if (mutualAuthentication) {
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            //KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
            keyStore = KeyStore.getInstance(keyStoreType);
            InputStream inputStream = FileUtils.getResource(keyFilePath);
            try {
                //添加证书
                keyStore.load(inputStream, keyStorePassword.toCharArray());
            } finally {
                inputStream.close();
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
            InputStream inputStream = FileUtils.getResource(trustFilePath);
            try {
                //添加证书
                trustKeyStore.load(inputStream, trustPassword.toCharArray());
            } finally {
                inputStream.close();
            }
            trustManagerFactory.init(trustKeyStore);
            sslContext.init(keyManagerFactory == null ? null : keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        }

        //忽略主机认证
        //方法1
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext, sslProtocols.toArray(new String[sslProtocols.size()]),
                        null,
                        new HostnameVerifier() {
                            // 这里不校验hostname
                            @Override
                            public boolean verify(String urlHostName, SSLSession session) {
                                return true;
                            }
                        });

        //方法2
        //sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        return sslConnectionSocketFactory;
    }

    private DefaultResponseErrorHandler restTemplateErrorHandler(){
        return new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                super.handleError(response);
            }
        };
    }
}


class AuthX509TrustManager implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
            throws java.security.cert.CertificateException {
        return;
    }
}