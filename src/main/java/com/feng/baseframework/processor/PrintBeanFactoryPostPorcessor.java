package com.feng.baseframework.processor;

import com.feng.baseframework.annotation.CustomOnProfileCondition;
import com.feng.baseframework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * baseframework
 * 2020/7/2 17:31
 * spring post processor学习
 *
 * @author lanhaifeng
 * @since
 **/
@Component
//@CustomOnProfileCondition("dev")等同于@ConditionalOnProperty(prefix = "spring.profiles", name = "active", havingValue = "dev")
@CustomOnProfileCondition("dev")
public class PrintBeanFactoryPostPorcessor implements BeanFactoryPostProcessor {

	private Logger logger = LoggerFactory.getLogger(PrintBeanFactoryPostPorcessor.class);

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		Iterator<String> beanNames = configurableListableBeanFactory.getBeanNamesIterator();
		while (beanNames.hasNext()){
			String beanName = beanNames.next();

			StringUtil.consoleDebugLog(logger, "beanName：", beanName, true);

			if(configurableListableBeanFactory.containsBeanDefinition(beanName)){
				BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(beanName);
				StringUtil.consoleDebugLog(logger, "beanDefinition：", beanDefinition.getClass().toString(), true);

				if(beanDefinition.getBeanClassName() != null){
					StringUtil.consoleDebugLog(logger, "beanClassName：", beanDefinition.getBeanClassName(), true);
				}else {
					StringUtil.consoleDebugLog(logger, "factoryBeanName：",
							beanDefinition.getFactoryBeanName() + ",factoryMethodName："
							+ beanDefinition.getFactoryMethodName(), true);

				}
				StringUtil.consoleDebugLog(logger, "", "-------------------------", true);
			}
		}
	}

}
