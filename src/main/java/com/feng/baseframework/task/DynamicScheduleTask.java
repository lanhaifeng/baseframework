package com.feng.baseframework.task;

import java.util.List;

/**
 * baseframework
 * 2020/10/14 15:35
 * 动态定时任务
 *
 * @author lanhaifeng
 * @since
 **/
public interface DynamicScheduleTask {

	List<CustomTimeTask> findScheduleTasks();

	void loadScheduleTasks();

	void addScheduleTask(CustomTimeTask timeTask);

	void updateScheduleTask(CustomTimeTask timeTask);

	void removeScheduleTask(CustomTimeTask timeTask);
}
