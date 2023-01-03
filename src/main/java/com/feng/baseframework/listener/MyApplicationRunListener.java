package com.feng.baseframework.listener;

import com.feng.baseframework.autoconfig.SystemPropertyInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * baseframework
 * 2019/8/8 15:43
 * 自定义spring启动监听器
 *
 * @author lanhaifeng
 * @since
 **/
public class MyApplicationRunListener implements SpringApplicationRunListener {

	private static Logger logger = LoggerFactory.getLogger(MyApplicationRunListener.class);

	public MyApplicationRunListener(SpringApplication application, String[]  args){
		logger.info("MyApplicationRunListener constructor");
	}

	@Override
	public void starting() {
		logger.info("application starting");
		SystemPropertyInit.initSystemProperty();
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
