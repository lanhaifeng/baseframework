package com.feng.baseframework.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MD5UtilTest {

    @Test
    void password2MD5() {
        //未指定salt，随机生成salt
        Assertions.assertNotEquals("$1$hhM/3hHD$Y2H2dB0Oqi02Ks7oQcObE.", MD5Util.password2MD5("audit"),
                "md5失败");
        Assertions.assertEquals("$1$hzmcAudi$24b/nxRh2z4YsiG3.vLeD/",
                MD5Util.password2MD5("audit","hzmcAudit_12F"), "md5失败");

        //未指定salt，随机生成salt
        Assertions.assertNotEquals("$apr1$bJPTCzO7$OL7ejRrzG1fvm5ijOexJO0", MD5Util.password2Apr1("audit"));
        Assertions.assertEquals("$apr1$hzmcAudi$Q9dvtmXlzDLGCYnK9a.SJ0", MD5Util.password2Apr1("audit", "hzmcAudit_12F"));
    }
}