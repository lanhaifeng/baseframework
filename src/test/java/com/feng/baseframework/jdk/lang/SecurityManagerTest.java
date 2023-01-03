package com.feng.baseframework.jdk.lang;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;

/**
 * @ProjectName: baseframework
 * @Description: 安全与认证测试
 * @Author: lanhaifeng
 * @CreateDate: 2019/6/23 17:57
 * @UpdateUser:
 * @UpdateDate: 2019/6/23 17:57
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SecurityManagerTest {

    @Test
    public void testSecurityManager(){
        Assert.isNull(System.getSecurityManager(), "安全管理器为空");
    }
}
