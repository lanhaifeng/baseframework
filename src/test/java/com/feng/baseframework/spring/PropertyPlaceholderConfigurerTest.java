package com.feng.baseframework.spring;

import org.junit.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * baseframework
 * 2021/4/22 17:23
 * 测试PropertyPlaceholderConfigurer
 *
 * @author lanhaifeng
 * @since
 **/
public class PropertyPlaceholderConfigurerTest {

	@Test
	public void testResourceLoader() throws IOException {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
		Properties p = new Properties();
		p.load(resource.getInputStream());
		System.out.println(resource.getFilename());
		System.out.println(p.toString());
	}
}
