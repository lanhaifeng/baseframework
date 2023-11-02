package com.feng.baseframework.filter;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

class RBloomFilterTest {
    public static void main(String[] args) {
        // Redis连接配置，无密码
        Config config = new Config();
        config.useSingleServer().setAddress("redis://10.100.1.166:6379");
        config.useSingleServer().setPassword("passwd123");

        /** 预计插入的数据 */
        int expectedInsertions = 10000;
        /** 误判率 */
        double fpp = 0.01;

        // 初始化布隆过滤器
        RedissonClient client = Redisson.create(config);
        RBloomFilter<Object> bloomFilter = client.getBloomFilter("user");
        bloomFilter.tryInit(expectedInsertions, fpp);

        // 布隆过滤器增加元素
        for (Integer i = 0; i < expectedInsertions; i++) {
            bloomFilter.add(i);
        }

        // 统计元素
        int count = 0;
        for (int i = expectedInsertions; i < expectedInsertions*2; i++) {
            if (bloomFilter.contains(i)) {
                count++;
            }
        }
        System.out.println("误判次数" + count);
    }
}