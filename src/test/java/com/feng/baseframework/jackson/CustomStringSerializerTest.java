package com.feng.baseframework.jackson;

import com.feng.baseframework.model.Student;
import com.feng.baseframework.util.JacksonUtil;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;

public class CustomStringSerializerTest {

	@Test
	public void serialize() {
		Student student = new Student();
		student.setName("test");
		student.setHobby("");

		String json = JacksonUtil.bean2Json(student);
		Assert.state(json.indexOf("\"hobby\":null") > -1);
	}
}