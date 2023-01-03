package com.feng.baseframework.util;

/**
 * baseframework
 * IO工具类
 * 2020/7/5 18:35
 *
 * @author lanhaifeng
 * @version 1.0
 */
public class IOUtils {

     /**
      * @Description:         扩展字节数据
      *
      * @param destBytes       目标字节数组
      * @param destCount       目标字节数组长度
      * @param space           期待空白长度
      * @return:
      * @Author: lanhaifeng
      * @Date: 2020/7/5 18:39
      */
    public static  byte[] ensureCapacity(byte[] destBytes, int destCount, int space) {
        int newCount = space + destCount;
        if (newCount > destBytes.length) {
            byte[] newbuf = new byte[Math.max(destBytes.length << 1, newCount)];
            System.arraycopy(destBytes, 0, newbuf, 0, destCount);
            return newbuf;
        }
        return destBytes;
    }
}
