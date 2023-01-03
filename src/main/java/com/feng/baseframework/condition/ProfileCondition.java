package com.feng.baseframework.condition;

import com.feng.baseframework.annotation.CustomOnProfileCondition;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * capaa-web2
 * 2020/7/2 17:39
 * 项目模式条件
 *
 * @author lanhaifeng
 * @since
 **/
public class ProfileCondition implements Condition {
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(CustomOnProfileCondition.class.getName());
		String profile = (String) annotationAttributes.get("value");
		String[] profiles = (String[]) annotationAttributes.get("profiles");
		if (0 == profiles.length && StringUtils.isBlank(profile)) {
			return false;
		}
		List<String> realProfiles = Arrays.asList(conditionContext.getEnvironment().getActiveProfiles());
		// 有一个匹配上就ok
		if(StringUtils.isNotBlank(profile) && realProfiles.contains(profile)){
			return true;
		}
		for (String expectProfile : profiles) {
			if (realProfiles.contains(expectProfile)) {
				return true;
			}
		}
		return false;
	}
}
