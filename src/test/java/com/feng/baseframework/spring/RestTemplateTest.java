/**
 * 版权：Copyright © 2010 浙江邦盛科技有限公司
 */

package com.feng.baseframework.spring;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * 类名:RestTemplateTest <br/>
 * 描述:测试RestTemplate使用http2
 * 时间:2023/11/2 18:49
 *
 * @author lanhaifeng
 * @since v3.0
 */
class RestTemplateTest {

    @Test
    void testRestTemplateHttp2() throws NoSuchAlgorithmException, KeyManagementException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(SSLSocketFactoryUtils.getIgnoreInitedSslContext().getSocketFactory(), SSLSocketFactoryUtils.IGNORE_SSL_TRUST_MANAGER_X509);
        builder.hostnameVerifier(SSLSocketFactoryUtils.getIgnoreSslHostnameVerifier());
        builder.protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));

        OkHttpClient okHttpClient = builder.build();

        OkHttp3ClientHttpRequestFactory factory = new OkHttp3ClientHttpRequestFactory(okHttpClient);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .customizers(restTemplate1 -> {
                    restTemplate1.getInterceptors().add((request, body, execution) -> {
                        System.out.println(request.getHeaders());
                        ClientHttpResponse response = execution.execute(request, body);
                        System.out.println(response.getHeaders());
                        return response;
                    });
                })
                .build();
        restTemplate.setRequestFactory(factory);
        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        //https时使用HTTP2
        ResponseEntity<String> responseEntity = restTemplate.exchange("https://192.168.91.66:9090/globalMgr/about", HttpMethod.GET, requestEntity, String.class);
        System.out.println(responseEntity.getBody());
        System.out.println(responseEntity.getHeaders());
    }
}