package com.feng.baseframework.autoconfig;

import com.feng.baseframework.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * baseframework
 * 2019/11/4 15:04
 * spring EL表达式测试配置类
 *
 * @author lanhaifeng
 * @since
 **/
@Configuration
public class SpELTestConfig {

	@Bean("spELTestStudent")
	public Student student(){
		Student student = new Student();
		student.setName("test");
		student.setHobby("music");
		return student;
	}
}
