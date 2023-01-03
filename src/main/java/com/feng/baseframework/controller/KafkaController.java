package com.feng.baseframework.controller;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * baseframework
 * 2021/1/7 10:59
 * kafka测试controller
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@RestController
@RequestMapping(value = "kafka")
@Profile("kafka")
public class KafkaController {

	private static Logger logger = LoggerFactory.getLogger(KafkaController.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@RequestMapping(value = "/{topic}/message", method = {RequestMethod.GET, RequestMethod.POST})
	public void sendKafkaMessage(@PathVariable String topic, String message) {
		kafkaTemplate.send(topic, message);
	}

	@KafkaListener(group = "${spring.kafka.consumer.group-id}", topics="#{'${spring.kafka.consumer.topic}'.split(',')}")
	public void listen(ConsumerRecord<String, String> msg){
		logger.info(msg.toString());
	}
}
