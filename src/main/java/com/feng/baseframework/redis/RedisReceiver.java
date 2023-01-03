package com.feng.baseframework.redis;

/**
 * baseframework
 * 2018/8/21 16:12
 * 创建监听之后的receiver方法类
 *
 * @author lanhaifeng
 * @since
 **/
public class RedisReceiver {
    public void receiveMessage(String message) {
        System.out.println("message:"+message);
    }
}
