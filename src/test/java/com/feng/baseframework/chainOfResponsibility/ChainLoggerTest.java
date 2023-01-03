package com.feng.baseframework.chainOfResponsibility;

import org.junit.Before;
import org.junit.Test;

public class ChainLoggerTest {

	private LoggerChain loggerChain;

	@Before
	public void setUp() throws Exception {
		loggerChain = new LoggerChain();
		loggerChain.setRootLogger(new ErrorLogger(AbstractLogger.ERROR))
				.setNextLogger(new FileLogger(AbstractLogger.DEBUG))
				.setNextLogger(new ConsoleLogger(AbstractLogger.INFO));
	}

	@Test
	public void logMessageTest() {
		loggerChain.logMessage(AbstractLogger.INFO, "This is an information.");

		loggerChain.logMessage(AbstractLogger.DEBUG,
				"This is a debug level information.");

		loggerChain.logMessage(AbstractLogger.ERROR,
				"This is an error information.");
	}
}