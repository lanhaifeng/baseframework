package com.feng.baseframework.spring;

import com.feng.baseframework.model.KeyStoreParam;
import org.junit.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.env.PropertiesPropertySourceLoader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

/**
 * baseframework
 * 2021/4/28 9:54
 * 绑定属性到bean
 *
 * @author lanhaifeng
 * @since
 **/
public class RelaxedDataBinderTest {

	@Test
	public void binderTest1() throws IOException {
		KeyStoreParam keyStoreParam = new KeyStoreParam();
		keyStoreParam.setCity("city");
		keyStoreParam.setFilePath("file");

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
		PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

		new Binder(ConfigurationPropertySources.from(
				propertiesPropertySourceLoader.load("dev", resource)))
				.bind("base.test", Bindable.ofInstance(keyStoreParam));

		System.out.println(keyStoreParam);
	}

	@Test
	public void binderTest2() throws IOException {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
		PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

		BindResult<Teacher> bindResult = new Binder(ConfigurationPropertySources.from(
				propertiesPropertySourceLoader.load("dev", resource)))
				.bind("teacher.test", Teacher.class);
		Teacher teacher = bindResult.get();

		System.out.println(teacher);
	}

	public static class Teacher {
		private String userName;
		private Student student;

		@Override
		public String toString() {
			return "Teacher{" +
					"userName='" + userName + '\'' +
					", student=" + student +
					'}';
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public Student getStudent() {
			return student;
		}

		public void setStudent(Student student) {
			this.student = student;
		}
	}

	public static class Student {
		private String userName;

		@Override
		public String toString() {
			return "Student{" +
					"userName='" + userName + '\'' +
					'}';
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
	}
}
