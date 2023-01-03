package com.feng.baseframework.log;

import com.feng.baseframework.common.MockitoBaseTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;

/**
 * baseframework
 * 2020/9/4 11:02
 * 测试日志
 *
 * @author lanhaifeng
 * @since
 **/
public class TestLog extends MockitoBaseTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void testJul1() {
		java.util.logging.Logger julLogger = java.util.logging.Logger.getLogger(getClass().toString());
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new ConsoleLogFormatter());
		julLogger.addHandler(consoleHandler);
		julLogger.setUseParentHandlers(false);

        julLogger.info(julLogger.getClass().toGenericString());
		julLogger.log(Level.INFO, "test jul log");
		julLogger.log(Level.INFO, "test jul log");
	}

	//profile=jul
	@Test
	public void testJul2() {
        logger.info(logger.getClass().toGenericString());
		logger.info("test jul log");
		logger.info("test jul log");
	}

	//profile=jcl
	@Test
	public void testJcl1() {
        logger.info(logger.getClass().toGenericString());
		logger.info("test jcl log");
		logger.info("test jcl log");
	}

	//profile=jcl
	@Test
	public void testJcl2() {
        logger.info(logger.getClass().toGenericString());
		logger.info("test jcl log");
		logger.info("test jcl log");
	}

	//profile=log4j
	//test -P SlowTests -P log4j -Dtest=com.feng.baseframework.log.TestLog#testLog4j1
    //配置默认激活log4j，既<activeByDefault>true</activeByDefault>，否则没有对应依赖
	@Test
	public void testLog4j1() {
        /*org.apache.log4j.Logger log4jLogger = org.apache.log4j.Logger.getLogger(TestLog. class );
        log4jLogger.info(log4jLogger.getClass().toGenericString());
        log4jLogger.info("test log4j log");
        log4jLogger.info("test log4j log");*/
	}

	//profile=log4j
	@Test
	public void testLog4j2() {
        logger.info(logger.getClass().toGenericString());
		logger.info("test log4j log");
		logger.info("test log4j log");
	}

    //profile=simple
    @Test
    public void testSlf4j_SimpleLog1() {
	    logger.info(logger.getClass().toGenericString());
        logger.info("test slf4j-simpleLog log");
        logger.info("test slf4j-simpleLog log");
    }

    static {
        //ch.qos.logback.classic.util.ContextInitializer.CONFIG_FILE_PROPERTY
        System.setProperty("logback.configurationFile", "log/logback.xml");
    }

    //profile=logback
    //初始化过程(加载配置文件)在类org.slf4j.impl.StaticLoggerBinder中(logback-classic.jar)
    @Test
    public void testLogback1() {
        logger.info(logger.getClass().toGenericString());
        logger.info("test logback log");
        logger.info("test logback log");
    }

    @Test
    public void testNop1() {
        logger.info(logger.getClass().toGenericString());
        logger.info("test logback log");
        logger.info("test logback log");
    }
}
