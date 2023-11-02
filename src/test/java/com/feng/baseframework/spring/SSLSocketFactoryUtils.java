/**
 * 版权：Copyright © 2010 浙江邦盛科技有限公司
 */

package com.feng.baseframework.spring;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * 类名:SSLSocketFactoryUtils <br/>
 * 描述:
 * 时间:2023/11/2 16:45
 *
 * @author lanhaifeng
 * @since
 */
public interface SSLSocketFactoryUtils {
    /**
     * X509TrustManager instance which ignored SSL certification
     */
    public static final X509TrustManager IGNORE_SSL_TRUST_MANAGER_X509 = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    };

    /**
     * Get initialized SSLContext instance which ignored SSL certification
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext getIgnoreInitedSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] { IGNORE_SSL_TRUST_MANAGER_X509 }, new SecureRandom());
        return sslContext;
    }

    /**
     * Get HostnameVerifier which ignored SSL certification
     *
     * @return
     */
    public static HostnameVerifier getIgnoreSslHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
    }
}