package com.feng.baseframework.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt工具
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/1/5 15:32创建:BCryptutil
 * @since v2.0.0
 */
public final class BCryptutil {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BCryptutil(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public String password2BCrypt(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean match(String password, String encodePassword){
        return bCryptPasswordEncoder.matches(password, encodePassword);
    }
}
