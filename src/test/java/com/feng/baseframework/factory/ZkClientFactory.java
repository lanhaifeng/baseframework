/**
 * 版权：Copyright © 2010 浙江邦盛科技有限公司
 */

package com.feng.baseframework.factory;

import com.feng.baseframework.autoconfig.ZkClientProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.client.ZKClientConfig;

import java.util.Optional;

/**
 * 类名:ZkClientFactory <br/>
 * 描述:zookeeper客户端工厂
 * 时间:2023/12/8 13:48
 *
 * @author lanhaifeng
 * @since v1.0.0
 */
public class ZkClientFactory {

    private static volatile CuratorFramework zkClient;

    /**
     * 获取基础zk客户端
     * @param properties  配置
     * @author lanhf
     * @since 1.0.0
     * @return CuratorFramework
     */
    public static CuratorFramework getSimpleClient(ZkClientProperties properties) {
        if (zkClient == null) {
            synchronized (ZkClientFactory.class) {
                if (zkClient == null) {
                    createSimple(properties);
                }
            }
        }
        return zkClient;
    }

    /**
     * 获取zk客户端
     * @param properties  配置
     * @author lanhf
     * @since 1.0.0
     * @return CuratorFramework
     */
    public static CuratorFramework getClient(ZkClientProperties properties) {
        if (zkClient == null) {
            synchronized (ZkClientFactory.class) {
                if (zkClient == null) {
                    createWithOptions(properties);
                }
            }
        }
        return zkClient;
    }


    /**
     * 创建简单zk
     *
     * @param properties 属性
     */
    private static void createSimple(ZkClientProperties properties) {
        //重试策略: 第一次重试等待1秒，第二次重试等待2秒，第三次重试等待4秒
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(
                properties.getBaseSleepTime(), properties.getMaxRetries());
        zkClient = CuratorFrameworkFactory.newClient(properties.getHostUrl(),
                properties.getSessionTimeout(), properties.getConnectionTimeout(), retryPolicy);
        zkClient.start();
    }

    /**
     * 使用选项创建zk
     *
     * @param properties 属性
     */
    private static void createWithOptions(ZkClientProperties properties) {
        Optional.ofNullable(properties.getProperties()).ifPresent(p -> System.getProperties().putAll(p));
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTime(), properties.getMaxRetries());
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(properties.getHostUrl())
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(properties.getConnectionTimeout()) //连接超时
                .sessionTimeoutMs(properties.getSessionTimeout()) //会话超时
                .zkClientConfig(new ZKClientConfig())
                .build();
        zkClient.start();
    }
}