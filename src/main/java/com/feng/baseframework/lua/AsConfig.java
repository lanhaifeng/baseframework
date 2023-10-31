package com.feng.baseframework.lua;

import lombok.Data;

/**
 * 类名:AsConfig <br/>
 * 描述:aerospike 配置
 * 时间:2023/10/7 10:37
 *
 * @author zhangqb
 * @since v1.10.0
 */
@Data
public class AsConfig {
    /**
     * as的服务地址
     */
    String nodes;
    /**
     * as的数据库schema
     */
    String namespace;
    /**
     * 访问的key-value表
     */
    String set;
    /**
     * as客户端请求的超时，
     */
    int soTimeout = 1000;
    /**
     * 客户端支持的最大连接数
     */
    int maxConn = 300;

}