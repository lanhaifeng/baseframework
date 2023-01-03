package com.feng.baseframework.util;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import java.security.MessageDigest;

/**
 * @ProjectName: svc-search-biz
 * @description: MD5加密工具类
 * @author: lanhaifeng
 * @create: 2018/5/16 19:15
 * @UpdateUser:
 * @UpdateDate: 2018/5/16 19:15
 * @UpdateRemark:
 **/
public class MD5Util {

    public static String string2MD5(String inStr){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }

    public static String password2MD5(String password){
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder.encodePassword(password,null);
    }

    public static String password2MD5(String password, Object salt){
        Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
        return md5PasswordEncoder.encodePassword(password,salt);
    }

    public static void main(String[] args) {
        System.out.println(password2MD5("audit"));
        System.out.println(password2MD5("audit","hzmcAudit_12F"));
    }
}
