package com.feng.baseframework.jdk8.util;

import org.junit.Test;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @ProjectName: baseframework
 * @Description: Base64测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:56
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:56
 * @UpdateRemark:
 * @Version: 1.0
 */
public class Base64Test {
    /**
     * base64
     * Java 8 内置了 Base64 编码的编码器和解码器
     * Base64工具类提供了一套静态方法获取下面三种BASE64编解码器：
     * 1.基本
     *  输出被映射到一组字符A-Za-z0-9+/，编码不添加任何行标，输出的解码仅支持A-Za-z0-9+/
     * 2.URL
     *  输出映射到一组字符A-Za-z0-9+_，输出是URL和文件
     * 3.MIME
     *  输出隐射到MIME友好格式。输出每行不超过76字符，并且使用'\r'并跟随'\n'作为分割。编码输出最后没有行分割
     */

    @Test
    public void baseTest() {
        Base64.Encoder encoder = Base64.getEncoder();
        Base64.Decoder decoder = Base64.getDecoder();

        String message = "runoob?java8";
        Assert.state(message.equals(
                new String(
                        decoder.decode(
                                encoder.encode(
                                        message.getBytes(StandardCharsets.UTF_8))))), "base64编码/解码失败");
    }

    @Test
    public void urlTest() {
        Base64.Encoder encoder = Base64.getUrlEncoder();
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String url = "http://127.0.0.1:9090/exploremgr/exploreResult/query?page=2&size=1";
        Assert.state(url.equals(
                new String(
                        decoder.decode(
                                encoder.encode(
                                        url.getBytes(StandardCharsets.UTF_8))))), "base64编码/解码失败");
    }

    @Test
    public void mimeTest() {
        Base64.Encoder encoder = Base64.getMimeEncoder();
        Base64.Decoder decoder = Base64.getMimeDecoder();

        String mimeData = Stream.generate(()->UUID.randomUUID().toString()).limit(10).reduce(String::concat).orElse("");
        Assert.state(mimeData.equals(
                new String(
                        decoder.decode(
                                encoder.encode(
                                        mimeData.getBytes(StandardCharsets.UTF_8))))), "base64编码/解码失败");

        int lineLength = 76;
        byte[] lineSeparator = new byte[] {'\r', '\n'};
        encoder = Base64.getMimeEncoder(lineLength, lineSeparator);
        Assert.state(mimeData.equals(
                new String(
                        decoder.decode(
                                encoder.encode(
                                        mimeData.getBytes(StandardCharsets.UTF_8))))), "base64编码/解码失败");
    }
}
