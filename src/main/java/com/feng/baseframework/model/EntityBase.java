package com.feng.baseframework.model;

import com.feng.baseframework.util.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * license-spring-boot
 * 2019/4/2 9:45
 *
 * @author lanhaifeng
 * @since
 **/
@Slf4j
public abstract class EntityBase implements Serializable {

	/**
	 * 当集合为空，返回true
	 * @param messageList
	 * @return
	 */
	private boolean emptyList(List<String> messageList) {
		Predicate<Collection> empty = collection -> collection == null || collection.isEmpty();
		return empty.test(messageList);
	}

	/**
	 * 返回验证bean失败的前置描述
	 * @return
	 */
	public abstract String preErrorDesc();

	public abstract List<String> customValidate(Class groupCls);

	/**
	 * 验证当前实例是否合法
	 * @return
	 */
	public void validate(){
		List<String> messageList = ValidateUtils.validateMessages(this);
		List<String> customMessageList = customValidate(null);
		if(!emptyList(customMessageList)){
			messageList.addAll(customMessageList);
		}

		if(!emptyList(messageList)){
			log.error(String.format("[%s] bean validate fail:", preErrorDesc()) + StringUtils.join(messageList, ","));
			throw new IllegalArgumentException("bean validate fail");
		}
	}

	/**
	 * 验证当前实例是否合法
	 * @param groupCls 分组
	 * @return
	 */
	public void validate(Class groupCls){
		List<String> messageList = ValidateUtils.validateMessages(this, groupCls);
		List<String> customMessageList = customValidate(groupCls);
		if(!emptyList(messageList) && !emptyList(customMessageList)){
			messageList.addAll(customMessageList);
		}
		if(!emptyList(messageList)){
			log.error(String.format("{} bean validate fail:", preErrorDesc()) + StringUtils.join(messageList, ","));
			throw new IllegalArgumentException("bean validate fail");
		}
	}
}
