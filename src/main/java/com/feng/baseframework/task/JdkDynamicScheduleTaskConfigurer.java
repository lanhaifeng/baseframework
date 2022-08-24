package com.feng.baseframework.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 2020/11/26 17:15
 * jdk动态定时任务
 *
 *
 * @author lanhaifeng
 * @since
 **/
public class JdkDynamicScheduleTaskConfigurer implements DynamicScheduleTask {

	private static Logger logger = LoggerFactory.getLogger(JdkDynamicScheduleTaskConfigurer.class);

	private static Timer timer = new Timer();

	private List<TimerTask> tasks = new ArrayList<>();
	private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 10, TimeUnit.SECONDS,
			new ArrayBlockingQueue<Runnable>(5), new RejectedExecutionHandler(){
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			logger.error("Task rejected.");
		}
	});

	@Override
	public List<CustomTimeTask> findScheduleTasks() {
		return null;
	}

	@Override
	public void loadScheduleTasks() {

	}

	@Override
	public void addScheduleTask(CustomTimeTask timeTask) {
//		timer.schedule(timeTask, timeTask.get);
	}

	@Override
	public void updateScheduleTask(CustomTimeTask timeTask) {

	}

	@Override
	public void removeScheduleTask(CustomTimeTask timeTask) {

	}
}
