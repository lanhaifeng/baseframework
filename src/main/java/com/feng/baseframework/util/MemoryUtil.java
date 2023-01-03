package com.feng.baseframework.util;

import com.feng.memory.util.MemoryCalculationTool;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.util.RamUsageEstimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * baseframework
 * 2019/2/21 16:34
 * 输入内存信息工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class MemoryUtil {

	private static final Logger logger = LoggerFactory.getLogger(MemoryUtil.class);

	public static void consoleMemoryInfo(String message){
		MemoryMXBean memorymbean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapUsage = memorymbean.getHeapMemoryUsage();
		MemoryUsage nonHeapUsage = memorymbean.getNonHeapMemoryUsage();
		if(StringUtils.isNotBlank(message)){
			logger.info(message + ",Memory info console start!");
		}else {
			logger.info("Memory info console start!");
		}

		//heap
		logger.info("init heap:" + heapUsage.getInit()/1024/1024 + "MB");
		logger.info("max heap:" + heapUsage.getMax()/1024/1024 + "MB");
		logger.info("used heap:" + heapUsage.getUsed()/1024/1024 + "MB");
		logger.info("heap memory usage:" + heapUsage.toString());

		//non-heap
		logger.info("init non-heap:" + nonHeapUsage.getInit()/1024/1024 + "MB");
		logger.info("max non-heap:" + nonHeapUsage.getMax()/1024/1024 + "MB");
		logger.info("used non-heap:" + nonHeapUsage.getUsed()/1024/1024 + "MB");
		logger.info("non-heap memory usage:" +nonHeapUsage.toString());

		if(StringUtils.isNotBlank(message)){
			logger.info(message + ",Memory info console end!");
		}else {
			logger.info("Memory info console end!");
		}
	}

	public static void consoleMemoryInfo(){
		consoleMemoryInfo(null);
	}

	//计算指定对象本身在堆空间的大小，单位字节
	public static long getObjecShallowSizeByInstrumentation(Object obj){
		return MemoryCalculationTool.getObjectSize(obj);
	}

	//计算指定对象本身在堆空间的大小，单位字节
	public static long getObjecShallowSizeByRamUsageEstimator(Object obj){
		return RamUsageEstimator.shallowSizeOf(obj);
	}

	//计算指定对象及其引用树上的所有对象的综合大小，单位字节
	public static long getObjecSizeByRamUsageEstimator(Object obj){
		return RamUsageEstimator.sizeOf(obj);
	}

	//计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
	public static String getObjecHumanSizeByRamUsageEstimator(Object obj){
		return RamUsageEstimator.humanSizeOf(obj);
	}

	//计算指定对象本身在堆空间的大小，单位字节
	public static long getObjecShallowSizeByUnsafe(Object obj){
		return RamUsageEstimator.shallowSizeOf(obj);
	}
}
