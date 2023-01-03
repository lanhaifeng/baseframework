package com.feng.baseframework.jdk8.concurrent;

import com.feng.baseframework.model.Role;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2020/12/7 15:24
 * 容器测试
 *
 * @author lanhaifeng
 * @since
 **/
public class CollectionTest {

	@Test
	public void testIfRemove() {
		List<Role> listObj = new ArrayList<>();
		listObj.add(new Role(1, "test", "test"));
		listObj.add(new Role(null, "test", "test"));
		listObj.add(new Role(1, "test", "test"));
		listObj.add(new Role(null, "test", "test"));


		List<String> listStr = new ArrayList<>();
		listStr.add(null);
		listStr.add("test");
		listStr.add("");
		listStr.add("test1");

		Assert.state(listObj.size() == 4);
		listObj.removeIf(role -> Objects.isNull(role.getId()));

		Assert.state(listObj.size() == 2);

		Assert.state(listStr.size() == 4);
		listStr.removeIf(StringUtils::isBlank);

		Assert.state(listStr.size() == 2);
	}
}
