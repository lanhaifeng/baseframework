package com.feng.baseframework.autoconfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feng.baseframework.listener.RedisMessageListener;
import com.feng.baseframework.redis.RedisReceiver;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: redis配置类
 * @Author: lanhaifeng
 * @CreateDate: 2018/4/29 23:13
 * @UpdateUser:
 * @UpdateDate: 2018/4/29 23:13
 * @UpdateRemark:
 * @Version: 1.0
 */
@Configuration
public class RedisConfiguration {
    @Bean
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
        CacheManager cacheManager = new RedisCacheManager(redisTemplate);
        return cacheManager;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(factory);
        return stringRedisTemplate;
    }

    /**
     * redis消息监听器容器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory
     * @param stringRedisTemplate
     * @return
     */
    @Bean //相当于xml中的bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            StringRedisTemplate stringRedisTemplate,
                                            List<Topic> listTopic,
                                            ThreadPoolTaskScheduler threadPoolTaskScheduler,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        List<Topic> topics = new ArrayList<>();
        topics.add(new ChannelTopic("testTopic1"));
        topics.add(new ChannelTopic("testTopic2"));
        //用监听器处理
        container.addMessageListener(new RedisMessageListener(stringRedisTemplate),listTopic);
        //用反射处理
        container.addMessageListener(listenerAdapter, new ChannelTopic("chat"));


        container.setTaskExecutor(threadPoolTaskScheduler);

        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter(RedisReceiver redisReceiver) {
        return new MessageListenerAdapter(redisReceiver, "receiveMessage");
    }

    /**
     * 消息处理器
     * @return
     */
    @Bean
    RedisReceiver receiver() {
        return new RedisReceiver();
    }


    /**
     * 订阅主题集合
     * @return
     */
    @Bean
    List<Topic> listTopic() {
        List<Topic> topics = new ArrayList<>();
        topics.add(new ChannelTopic("testTopic1"));
        topics.add(new ChannelTopic("testTopic2"));
        return topics;
    }

    /**
     * 定时任务
     * @return
     */
    @Bean
    ThreadPoolTaskScheduler threadPoolTaskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.initialize();

        return taskScheduler;
    }
}
