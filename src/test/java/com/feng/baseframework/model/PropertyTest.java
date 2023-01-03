package com.feng.baseframework.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

/**
 * baseframework
 * 2019/10/28 15:58
 * 测试springboot属性占位
 *
 * @author lanhaifeng
 * @since
 **/
@Getter
@Component
@PropertySource("classpath:application-dev1.properties")
public class PropertyTest {

	//使用默认值
	@Value("${spellDefault.value:127.0.0.1}")
	private String spellDefault;
	//注入静态属性，在set方法上注入
	private static String staticName;
	//引用文字表达式属性
	@Value("#{'helloWorld'.bytes.length}")
	private Integer helloWorldBytes;
	//#{}内嵌套${}引用方法
	@Value("#{'${server.name}'.split(',')}")
	private List<String> servers;
	//引用系统属性
	@Value("#{systemProperties['user.language']}")
	private String language;
	//引用系统环境变量
	@Value("#{systemEnvironment['FP_NO_HOST_CHECK']}")
	private String fpNoHostCheck;
	//引用bean属性
	@Value("#{spELTestStudent.hobby}")
	private String studentHobby;
	//引用bean方法
	@Value("#{spELTestStudent.getName()}")
	private String studentName;
	//引用class静态属性，如果属性存在get方法，会调用get方法
	@Value("#{T(com.feng.baseframework.model.Student).type}")
	private String studentType;
	//引用class静态方法
	@Value("#{T(com.feng.baseframework.model.Student).getTestType()}")
	private String studentTypeUpperCase;


	@Value("${static.name}")
	public void setStaticName(String staticName) {
		PropertyTest.staticName = staticName;
	}

	public String getStaticName() {
		return staticName;
	}
}
