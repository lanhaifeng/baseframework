package com.feng.baseframework.autoconfig;

/**
 * baseframework
 * 2019/8/8 23:22
 * 初始化系统属性
 *
 * @author lanhaifeng
 * @since
 **/
public class SystemPropertyInit {

	public static void initSystemProperty(){
		System.setProperty("jasypt.encryptor.password", "feng");
        /**
         * 配置logback配置文件路径
         * 实际上是springboot在org.springframework.boot.logging.LoggingApplicationListener#initialize()#initializeSystem中重新初始化日志类
         * 1.在application.properties中添加一行配置：logging.config=classpath:log/logback.xml
         * 2.添加如下一行代码:
         *   System.setProperty("logging.config", "classpath:log/logback.xml");
         */
		System.setProperty("logging.config", "classpath:log/logback.xml");
	}
}
