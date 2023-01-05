package com.feng.baseframework.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BCryptutilTest {

    @Test
    void match() {
        BCryptutil bCryptutil = new BCryptutil(new BCryptPasswordEncoder());

        //加密
        String encode = bCryptutil.password2BCrypt("123456");

        //解密
        assertTrue(bCryptutil.match("123456", encode), "解密失败");
    }
}