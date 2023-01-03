package com.feng.baseframework.autoconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * baseframework
 * 2021/1/7 10:21
 * kafka配置类
 *
 * @author lanhaifeng
 * @since
 **/
@Configuration
@EnableKafka
@Profile("kafka")
public class KafkaConfiguration {
}
