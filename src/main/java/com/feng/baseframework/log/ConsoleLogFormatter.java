package com.feng.baseframework.log;


import com.feng.baseframework.constant.LogType;
import com.feng.baseframework.util.DateUtil;
import com.feng.baseframework.util.RuntimeInfoUtil;
import org.apache.commons.lang.time.DateFormatUtils;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * baseframework
 * 2020/9/4 11:24
 * log格式化
 *
 * @author lanhaifeng
 * @since
 **/
public class ConsoleLogFormatter extends Formatter {

	public static String LOG_FORMATE = "WARN [PraseThread.keepAlive] 2020-08-10 17:10:09,700 (PraseThread.java:69) - ";

	public ConsoleLogFormatter() {
		super();
	}

	@Override
	public String format(LogRecord record) {
		String lineSeparator = System.getProperty("line.separator");
		String spaceSeparator = " ";

		StringBuilder sb = new StringBuilder();
		sb.append(record.getLevel().getName()).append(spaceSeparator)
				.append("[").append(Thread.currentThread().getName()).append("]").append(spaceSeparator)
				.append(DateFormatUtils.format(record.getMillis(), DateUtil.STAMP_FORMATE)).append(spaceSeparator)
				.append(RuntimeInfoUtil.getLogStackTraceElement(LogType.JUL)).append(spaceSeparator).append("-").append(spaceSeparator);
		sb.append(record.getMessage());
		sb.append(lineSeparator);

		return sb.toString();
	}
}
