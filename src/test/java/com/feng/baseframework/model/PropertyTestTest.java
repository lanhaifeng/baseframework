package com.feng.baseframework.model;

import com.feng.baseframework.common.JunitBaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@ActiveProfiles("dev,dev1")
public class PropertyTestTest extends JunitBaseTest{

	@Autowired
	private PropertyTest propertyTest;

	@Test
	public void testPropertyTest() {
		Assert.state(10 == propertyTest.getHelloWorldBytes(),"期待为：10,real value:" + propertyTest.getHelloWorldBytes());
		Assert.state("zh".equals(propertyTest.getLanguage()),"期待为：zh,real value:" + propertyTest.getLanguage());
		Assert.state("127.0.0.1".equals(propertyTest.getSpellDefault()),"期待为：127.0.0.1,real value:" + propertyTest.getSpellDefault());
		Assert.state("[server1, server2, server3]".equals(propertyTest.getServers().toString()),"期待为：[server1, server2, server3],real value:" + propertyTest.getServers().toString());
		Assert.state("NO".equals(propertyTest.getFpNoHostCheck()),"期待为：NO,real value:" + propertyTest.getFpNoHostCheck());
		Assert.state("staticName".equals(propertyTest.getStaticName()),"期待为：staticName,real value:" + propertyTest.getStaticName());

		Assert.state("music".equals(propertyTest.getStudentHobby()), "期待为：music,real value:" + propertyTest.getStudentHobby());
		Assert.state("test".equals(propertyTest.getStudentName()), "期待为：test,real value:" + propertyTest.getStudentName());

		Assert.state("person".equals(propertyTest.getStudentType()), "期待为：person,real value:" + propertyTest.getStudentType());
		Assert.state("PERSON".equals(propertyTest.getStudentTypeUpperCase()), "期待为：PERSON,real value:" + propertyTest.getStudentTypeUpperCase());

		Optional.ofNullable(System.getenv()).ifPresent(map ->
				map.forEach((key, value) ->
						System.out.println("env_key:" + key + ",value:" + value)
				)
		);

		Optional.ofNullable(System.getProperties()).ifPresent(map ->
				map.forEach((key, value) ->
						System.out.println("property_key:" + key + ",value:" + value)
				)
		);
	}
}