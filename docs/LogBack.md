#### 换配置文件路径
1.springboot的application.properties
```
logging.config=classpath:log/logback.xml
```
原理：实际上是springboot在org.springframework.boot.logging.LoggingApplicationListener#initialize()#initializeSystem中重新初始化日志类


2.System设置property  
```
public class MyApplicationRunListener implements SpringApplicationRunListener {

	private static Logger logger = LoggerFactory.getLogger(MyApplicationRunListener.class);

	public MyApplicationRunListener(SpringApplication application, String[]  args){
		logger.info("MyApplicationRunListener constructor");
	}

	@Override
	public void starting() {
		logger.info("application starting");
		System.setProperty("logging.config", "classpath:log/logback.xml");
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {
		logger.info("application environmentPrepared");
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
		logger.info("application contextPrepared");
	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
		logger.info("application contextLoaded");
	}

	@Override
	public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
		logger.info("application finished");
	}
}
```
原理：同上


3.web的context-param
```
    @Bean
    public EmbeddedServletContainerCustomizer webServerFactoryCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                configurableEmbeddedServletContainer.addInitializers(servletContextInitializer());
            }
        };
    }

	public ServletContextInitializer servletContextInitializer(){
        Map<String, String> contextParams = new HashMap<>();
        contextParams.put("logbackConfigLocation", "classpath:log/logback.xml");
        return new InitParameterConfiguringServletContextInitializer(contextParams);
    }
    
    //org.logback-extensions:logback-ext-spring:0.1.5
    @Bean
    public LogbackConfigListener logbackConfigListener(){
        return new LogbackConfigListener();
    }
```
原理：LogbackConfigListener中ServletContext.getInitParameter("logbackConfigLocation")重新初始化日志类

#### slf4j集成logback配置
1.导入依赖
```
<properties>
		<slf4j.version>1.7.20</slf4j.version>
		<logback.version>1.1.7</logback.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>${slf4j.version}</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>jcl-over-slf4j</artifactId>
        <version>${slf4j.version}</version>
    </dependency>

    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>${logback.version}</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-access</artifactId>
        <version>${logback.version}</version>
    </dependency>
    <dependency>
        <scope>compile</scope>
        <groupId>org.logback-extensions</groupId>
        <artifactId>logback-ext-spring</artifactId>
        <version>0.1.5</version>
    </dependency>
</dependencies>
```

2.指明配置文件路径application.properties
```
logging.config=classpath:log/logback.xml
```

3.添加日志配置文件logback.xml