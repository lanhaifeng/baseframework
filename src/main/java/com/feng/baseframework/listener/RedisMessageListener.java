package com.feng.baseframework.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * baseframework
 * 2018/8/21 15:02
 * redis订阅监听
 *
 * @author lanhaifeng
 * @since
 **/
public class RedisMessageListener implements MessageListener {

    public RedisMessageListener(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public RedisMessageListener() {
    }


    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer<?> serializer = stringRedisTemplate.getValueSerializer();
        Object channel = serializer.deserialize(message.getChannel());
        String body = (String)serializer.deserialize(message.getBody());
        System.out.println("主题: " + channel);
        System.out.println("消息内容: " + String.valueOf(body));
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
