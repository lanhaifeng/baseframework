package com.feng.baseframework.spring;

import com.feng.baseframework.model.KeyStoreParam;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

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
		RelaxedDataBinder binder = new RelaxedDataBinder(keyStoreParam, "base.test");

		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		PropertyValues pvs = new MutablePropertyValues(properties);
		binder.bind(pvs);

		System.out.println(keyStoreParam);
	}

	@Test
	public void binderTest2() throws IOException {
		Teacher teacher = new Teacher();

		RelaxedDataBinder binder = new RelaxedDataBinder(teacher, "teacher.test");
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
		Properties properties = new Properties();
		properties.load(resource.getInputStream());
		PropertyValues pvs = new MutablePropertyValues(properties);
		binder.bind(pvs);

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
