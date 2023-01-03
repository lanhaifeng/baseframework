package com.feng.baseframework.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * baseframework
 * 2018/9/19 11:30
 * 全局属性配置
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class GlobalPropertyConfig {

    /**
     * 认证开关，true开启认证，false关闭认证
     */
    @Value("${security.mode}")
    private String securityMode;

    public String getSecurityMode() {
        return securityMode;
    }
}
