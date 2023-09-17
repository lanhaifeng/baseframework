package com.feng.baseframework.util;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.mapper.CommonMapper;
import com.feng.baseframework.mapper.OperLogMapper;
import com.feng.baseframework.model.OperLog;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class BeanUtilTest {

	private String packages;

	@Before
	public void setUp() throws Exception {
		packages = "com.feng.baseframework.controller";
	}

	@Test
	public void getAnnotationInstances() {
		List<Object> datas = BeanUtil.getAnnotationInstances(packages, ClassLevelAdviceTag.class);

		Assert.state(datas.size() > 0);
	}

	@Test
	public void getSuperClassGenericType() {
		Class<?> modelClass = BeanUtil.getSuperClassGenericType(OperLogMapper.class, CommonMapper.class, 0);
		Assertions.assertEquals(modelClass, OperLog.class);
	}


	@Test
	public void toCamel() {
		String source = "user_name";
		Assertions.assertEquals("userName", BeanUtil.toCamel(source, "_"));
	}

	@Test
	public void camelToUnderLine() {
		String source = "userName";
		Assertions.assertEquals("user_name", BeanUtil.camelToUnderLine(source));
	}
}