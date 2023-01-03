1. 动态传入多个topics  
     配置类中配置
    ```java
        @Configuration
        public class KafkaTopicConfig implements InitializingBean {
        
            @Override
            public void afterPropertiesSet() {
            //获取topic
                String topics = Sets.newHashSet(KafkaTopicEnum.values()).stream()
                        .map(KafkaTopicEnum::getTopic).collect(Collectors.joining(","));
             //系统写入
                System.setProperty("topics", topics);
            }
        }
    ```
    使用
    ```java
        @KafkaListener(topics = "#{'${topics}'.split(',')}")
        public void listen(ConsumerRecord<String, String> data) {}
    ```
    
    配置文件中配置
    ```properties
       application.system-log-topic=queue1,queue2
    ```
    使用
    ```java
    @KafkaListener(groupId = "${spring.kafka-group}" , topics = "#{'${application.system-log-topic}'.split(',')}")
    public void listen(ConsumerRecord<String, String> data) {}
    ```
    
2.多个注解
    @KafkaListeners
    ```
        @KafkaListeners({@KafkaListener(topics="topic1"), @KafkaListener(topics="topic2")})
        public void listen(ConsumerRecord<Integer, String> msg) {}
    ```
    @KafkaListener
    ```
        @KafkaListener(topics="topic1")
        @KafkaListener(topics="topic2")
        public void listen(ConsumerRecord<Integer, String> msg) {}
    ```
    

3. 同一分组配置多个消费者
    [kafkaConsumer](http://www.hainiubl.com/topics/36305)  
    [@KafkaListener](https://www.pianshen.com/article/58921419756/)
    ConcurrentKafkaListenerContainerFactory#setConcurrency(3)
    [@KafkaListners](https://www.cnblogs.com/gxyandwmm/p/11849786.html)
    
4. springboot整合kafka
    [消费者手动](https://blog.csdn.net/allensandy/article/details/89633232)
    [集成kafka](https://blog.csdn.net/yuanlong122716/article/details/105160545/)
    4.2 手动配置
    配置consumer属性
    ```properties
       # [kafka consumer]
       spring.kafka-consumer-bootstrap-servers=127.0.0.1:9092
       
       # 如果为true，消费者的偏移量将在后台定期提交
       spring.kafka-consumer-enable-auto-commit=true
       
       #如何设置为自动提交（enable.auto.commit=true），这里设置自动提交周期，单位毫秒
       spring.kafka-consumer-auto-commit-interval=1000
       
       #offset提交消费策略
       spring.kafka-consumer-auto-offset-reset=latest
       
       #order-beta 消费者群组ID，发布-订阅模式，即如果一个生产者，多个消费者都要消费，那么需要定义自己的群组，同一群组内的消费者只有一个能消费到消息
       spring.kafka-group=systemLogGroup
       
       #在使用Kafka的组管理时，用于检测消费者故障的超时，单位毫秒
       spring.kafka-consumer-session-timeout=30000
       
       spring.kafka-consumer-key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
       spring.kafka-consumer-value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
    ```
    配置类
    ```java
    @Configuration
    @EnableKafka
    @Profile("kafka")
    public class KafkaConfiguration {
       @Bean
       	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaListenerContainerFactory() {
       		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
       		factory.setConsumerFactory(consumerFactory());
       		factory.setConcurrency(3);
       		factory.getContainerProperties().setPollTimeout(3000);
       		return factory;
       	}
       
       	@Bean
       	public ConsumerFactory<Integer, String> consumerFactory() {
       		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
       	}
       
       	@Bean
       	public Map<String, Object> consumerConfigs() {
       		Map<String, Object> props = new HashMap<>();
       		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumerServers());
       		props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getKafkaGroup());
       		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getConsumerEnableAutoCommit());
       		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getConsumerAutoCommitInterval());
       		props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaProperties.getConsumerSessionTimeout());
       		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumerAutoOffsetReset());
       		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumerKeyDeserializer());
       		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumerKeyDeserializer());
       		return props;
       	}
       
       	@KafkaListener(groupId = "${spring.kafka-group}" , topics = "#{'${application.system-log-topic}'.split(',')}")
       	public void receiveSystemLog(ConsumerRecord<String, String> data){
       		if(logger.isDebugEnabled()){
       			logger.debug("消费者线程：{} [ 消息 来自kafkatopic：{} ,分区：{} ,委托时间：{} 消息内容如下：{} ]",
       					Thread.currentThread().getName(), data.topic(), data.partition(), data.timestamp(), data.value());
       		}
       		systemOperationLogService.receiveSystemLogMsg(data.value());
       	}
    }
    ```
    
    配置server属性
    ```properties
        log.client.kafka-producer-bootstrap-servers=127.0.0.1:9092
        log.client.producer-client-id=soc
    ```
    配置类
    ```java
        public Map<String, Object> producerConfigs() {
        
        		Map<String, Object> props = new HashMap<String, Object>();
        		// 集群的服务器地址
        		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerProperties.getKafkaProducerBootstrapServers());
        		//  消息缓存
        		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 40960);
        		// 生产者空间不足时，send()被阻塞的时间，默认60s
        		props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 6000);
        		// 生产者重试次数
        		props.put(ProducerConfig.RETRIES_CONFIG, 0);
        		// 指定ProducerBatch（消息累加器中BufferPool中的）可复用大小
        		props.put(ProducerConfig.BATCH_SIZE_CONFIG,  4096);
        		// 生产者会在ProducerBatch被填满或者等待超过LINGER_MS_CONFIG时发送
        		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        		// key 和 value 的序列化
        		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        				"org.apache.kafka.common.serialization.StringSerializer");
        		// 客户端id
        		props.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerProperties.getProducerClientId());
        
        		return props;
        	}
        
        	@Bean("logClientProducerFactory")
        	@ConditionalOnMissingBean(name = "logClientTopicContainerFactory")
        	public ProducerFactory<String, String> producerFactory() {
        		return new DefaultKafkaProducerFactory<>(producerConfigs());
        	}
        
        	@Bean("logClientKafkaTemplate")
        	@ConditionalOnMissingBean(name = "logClientTopicContainerFactory")
        	public KafkaTemplate<String, String> kafkaTemplate() {
        		return new KafkaTemplate<>(producerFactory());
        	}
    ```
    
    4.2 使用注解EnableKafka.class
    启用kafka
    ```java
       @Configuration
       @EnableKafka
       public class KafkaAutoConfiguration {}
    ```
    配置kafka
    ```properties
       #kafka
       # 指定kafka 代理地址，可以多个
       spring.kafka.bootstrap-servers=192.168.59.130:9092,192.168.59.131:9092,192.168.59.132:9092
       # 指定默认消费者group id
       spring.kafka.consumer.group-id=myGroup
       # 指定默认topic id
       spring.kafka.template.default-topic= my-replicated-topic
       # 指定listener 容器中的线程数，用于提高并发量
       spring.kafka.listener.concurrency= 3
       # 每次批量发送消息的数量
       spring.kafka.producer.batch-size= 1000
    ```