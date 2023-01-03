package com.feng.baseframework.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class CRC32UtilTest {

    @Test
    public void getFileCRC32() {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/application.properties";
        long target = 1748656647;
        Assert.assertEquals("不是期待crc32，内容被修改", target, CRC32Util.getCRC32(filePath));
    }

    @Test
    public void getCRC32() {
        String json = "json";
        long target = 1795630405;
        Assert.assertEquals("不是期待crc32，内容被修改", target, CRC32Util.getCRC32(json));
    }

    @Test
    public void testGetCRC32() {
        String json = "json";
        long target = 1795630405;
        Assert.assertEquals("不是期待crc32，内容被修改", target, CRC32Util.getCRC32(json.getBytes(StandardCharsets.UTF_8)));
    }
}