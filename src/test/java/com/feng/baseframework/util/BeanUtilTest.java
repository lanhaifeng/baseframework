package com.feng.baseframework.util;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;

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
}