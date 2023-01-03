package com.feng.memory.util;

import java.lang.instrument.Instrumentation;

/**
 * baseframework
 * 2019/2/23 17:09
 * 计算内存工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class MemoryCalculationTool {

	private static Instrumentation instrumentation;

	public static void premain(String args, Instrumentation inst) {
		instrumentation = inst;
	}

	public static long getObjectSize(Object o) {
		return instrumentation.getObjectSize(o);
	}
}
