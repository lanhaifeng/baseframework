package com.feng.baseframework.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.util.UUID;

/**
 * baseframework
 * 2018/8/22 14:16
 * 字符串工具类
 *
 * @author lanhaifeng
 * @since  v1.0
 **/
public class StringUtil {

    /**
     * 生成uuid
     * @return
     */
    public static String generateUUID(){
        UUID key=UUID.randomUUID();
        String str = key.toString();
        return str.replace("-", "");
    }

    /**
     * 十六进制字符串转化为2进制
     *
     * @param hexStr
     * @return byte[]
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1){
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStrToByteArr(String strIn) 互为可逆的转换过程
     *
     * @param src 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 2020/7/21 16:42
     * 打印调试信息
     *
     * @param logger
     * @param preMessage
     * @param message
     * @param required
     * @author lanhaifeng
     * @return void
     */
    public static void consoleDebugLog(Logger logger, String preMessage, String message, boolean required){
        message = (preMessage != null ? preMessage : "") + (message != null ? message : "");
        if(StringUtils.isBlank(message)) return;
        if(logger != null && logger.isDebugEnabled()){
            logger.debug(message);
        }else {
            if(required){
                System.out.println(message);
            }
        }
    }
}
