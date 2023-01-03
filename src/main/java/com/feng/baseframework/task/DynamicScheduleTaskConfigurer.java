package com.feng.baseframework.task;

import com.feng.baseframework.util.SpringUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * baseframework
 * 2020/10/14 15:34
 * 动态定时任务配置类
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class DynamicScheduleTaskConfigurer implements SchedulingConfigurer, DynamicScheduleTask {

	private static Logger log = LoggerFactory.getLogger(DynamicScheduleTaskConfigurer.class);
	private TaskScheduler taskScheduler;
	private Set<ScheduledFuture<?>> scheduledFutures = null;
	private Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
		this.taskScheduler = scheduledTaskRegistrar.getScheduler();
		try {
			scheduledFutures = (Set<ScheduledFuture<?>>)FieldUtils.readField(
					FieldUtils.getField(scheduledTaskRegistrar.getClass(), "scheduledTasks", true),
					scheduledTaskRegistrar);
		} catch (IllegalAccessException e) {
			scheduledFutures = new HashSet<>();
		}
	}

	private CronTask schedule(CustomTimeTask timeTask){
		if(timeTask == null || !timeTask.validate()) return null;
		CronTask cronTask = new CronTask(new Runnable() {
			@Override
			public void run() {
				try{
					Class cls = Class.forName(timeTask.getBeanClassName());
					Method method = MethodUtils.getAccessibleMethod(cls, timeTask.getMethodName());
					Object target = SpringUtil.getBeanByType(cls);
					if(target == null){
						target = cls.newInstance();
					}
					method.invoke(target, method);
				}catch(Exception e){
				    log.error("执行任务失败，错误：" + ExceptionUtils.getFullStackTrace(e));
				}
			}
		}, timeTask.getCronExpression());

		return cronTask;
	}

	@Override
	public void loadScheduleTasks() {
		findScheduleTasks().forEach(timeTask -> {
			CronTask cronTask = schedule(timeTask);
			Optional.of(
					taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger())
			).ifPresent(scheduledFuture -> {
				scheduledFutures.add(scheduledFuture);
				taskFutures.put(String.valueOf(timeTask.getId()), scheduledFuture);
			});
		});

	}

	@Override
	public List<CustomTimeTask> findScheduleTasks() {
		List<CustomTimeTask> timeTasks = new ArrayList<>();
		//可以从数据库中加载
		//也可以自定义
		customTimeTask(timeTasks);
		return timeTasks;
	}

	private void customTimeTask(List<CustomTimeTask> timeTasks){
		if(timeTasks == null) timeTasks = new ArrayList<>();
		CustomTimeTask timeTask = new CustomTimeTask();
		timeTasks.add(timeTask);
	}

	@Override
	public void addScheduleTask(CustomTimeTask timeTask) {
		Optional.of(schedule(timeTask)).ifPresent(cronTask -> Optional.of(
				taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger())
				).ifPresent(scheduledFuture -> {
					scheduledFutures.add(scheduledFuture);
					taskFutures.put(String.valueOf(timeTask.getId()), scheduledFuture);
				})
		);
	}

	@Override
	public void updateScheduleTask(CustomTimeTask timeTask) {
		removeScheduleTask(timeTask);
		addScheduleTask(timeTask);
	}

	@Override
	public void removeScheduleTask(CustomTimeTask timeTask) {
		ScheduledFuture<?> scheduledFuture = taskFutures.get(String.valueOf(timeTask.getId()));
		Optional.of(scheduledFuture).ifPresent(task -> {
			task.cancel(true);
			taskFutures.remove(String.valueOf(timeTask.getId()));
			scheduledFutures.remove(task);
		});
	}
}
