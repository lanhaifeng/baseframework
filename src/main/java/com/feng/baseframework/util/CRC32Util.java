package com.feng.baseframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * crc32校验工具类
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/9/2 14:18创建:CRC32Util
 * @since 1.0
 */
public class CRC32Util {

    private static Logger logger = LoggerFactory.getLogger(CRC32Util.class);
    private final static CRC32 crc32 = new CRC32();

    /**
     * 获取文件CRC32值
     *
     * @param filePath          文件路径
     * @author lanhaifeng
     * @since 1.0
     * @return 返回crc32值
     */
    public static long getFileCRC32(String filePath) {
        long crc32Value = 0L;
        try {
            crc32.reset();
            File file = new File(filePath);
            int fileLen = (int) file.length();
            InputStream in = new FileInputStream(file);
            //分段进行crc校验
            int let = 10 * 1024 * 1024;
            int sum = fileLen / let + 1;
            for (int i = 0; i < sum; i++) {
                if (i == sum - 1) {
                    let = fileLen - (let * (sum - 1));
                }
                byte[] b = new byte[let];
                in.read(b, 0, let);
                crc32.update(b);
            }
            crc32Value = crc32.getValue();
        } catch (Exception e) {
            logger.error("crc32检验异常：", e);
        }
        return crc32Value;
    }

    /**
     * 获取字节CRC32值
     *
     * @param data              数据字节数组
     * @author lanhaifeng
     * @since 1.0
     * @return long
     */
    public static long getCRC32(byte[] data) {
        crc32.reset();
        crc32.update(data);
        return crc32.getValue();
    }

    /**
     * 获取字符串CRC32值
     *
     * @param data              数据字符串
     * @author lanhaifeng
     * @since 1.0
     * @return long
     */
    public static long getCRC32(String data) {
        return getCRC32(data.getBytes(StandardCharsets.UTF_8));
    }
}
