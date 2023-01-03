package com.feng.baseframework.task;

import com.feng.baseframework.util.ValidateUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.scheduling.support.CronSequenceGenerator;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * baseframework
 * 2020/10/14 15:16
 * 定时任务实体类
 *
 * @author lanhaifeng
 * @since
 **/
public class CustomTimeTask extends TimerTask {

	private static final long serialVersionUID = 1954597707267521857L;
	private CronSequenceGenerator sequenceGenerator;

	@NotNull(message = "主键为空")
	private Integer id;
	@NotEmpty(message = "任务名为空")
	private String name;
	@NotEmpty(message = "分组名为空")
	private String groupName;
	@NotEmpty(message = "任务类名为空")
	private String beanClassName;
	@NotEmpty(message = "任务方法名为空")
	private String methodName;
	private boolean concurrent;
	@NotEmpty(message = "定时任务cron表达式为空")
	private String cronExpression;
//	private

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBeanClassName() {
		return beanClassName;
	}

	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public boolean isConcurrent() {
		return concurrent;
	}

	public void setConcurrent(boolean concurrent) {
		this.concurrent = concurrent;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Date nextExecutionTime() {
		return null;
	}

	public void init(){
		this.sequenceGenerator = new CronSequenceGenerator(cronExpression);
	}

	public boolean validate(){
		List<String> messageList = ValidateUtils.validateMessages(this);
		return messageList != null && messageList.size() > 0;
	}

	@Override
	public void run() {

	}
}
