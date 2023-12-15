/**
 * 版权：Copyright © 2010 浙江邦盛科技有限公司
 */

package com.feng.baseframework.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Properties;

/**
 * 类名:ZkClientProperties <br/>
 * 描述:zookeeper属性配置
 * 时间:2023/12/8 13:50
 *
 * @author lanhaifeng
 * @since v1.0.0
 */
@ConfigurationProperties(prefix = "dfp.verification.zk")
@EnableConfigurationProperties(ZkClientProperties.class)
@Getter
@Setter
public class ZkClientProperties {

    //连接地址
    private String hostUrl = "127.0.0.1:2181";
    //连接超时
    private int connectionTimeout;
    //回话超时
    private int sessionTimeout;
    //等待事件的基础单位，单位毫秒
    private int baseSleepTime = 1000;
    //最大重试次数
    private int maxRetries = 3;
    //扩展属性
    private Properties properties;
}