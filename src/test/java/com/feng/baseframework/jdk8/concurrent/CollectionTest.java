package com.feng.baseframework.jdk8.concurrent;

import com.feng.baseframework.model.Role;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.*;

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

	@Test
	public void intersectionTest1() {
		List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		System.out.println("交集前集合值：" + list1 + "," + list2);

		boolean existIntersection  = list1.retainAll(list2);
		System.out.println("是否存在交集：" + existIntersection +
				",交集：" + list1 + ",交集后集合值：" + list1 + "," + list2);

		list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		System.out.println("交集前集合值：" + list1 + "," + list2);

		List<Integer> list3 = new ArrayList<>(list1);
		existIntersection  = list3.retainAll(list2);
		System.out.println("是否存在交集：" + existIntersection +
				",交集：" + list3 + ",交集后集合值：" + list1 + "," + list2);

		list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		list3 = new ArrayList<>(list1);
		System.out.println("交集前集合值：" + list1 + "," + list2);

		existIntersection  = list3.retainAll(list2);
		System.out.println("是否存在交集：" + existIntersection +
				",交集：" + list3 + ",交集后集合值：" + list1 + "," + list2);
	}

	@Test
	public void intersectionTest2() {
		List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		List<Integer> list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		Collection<Integer> intersection = CollectionUtils.intersection(list1, list2);
		System.out.println("交集：" + intersection);

		list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		list2 = new ArrayList<>(Arrays.asList(1, 2, 3, 6, 7, 8, 9));
		intersection = CollectionUtils.intersection(list1, list2);
		System.out.println("交集：" + intersection);

		list1 = new ArrayList<>();
		list2 = new ArrayList<>();
		intersection = CollectionUtils.intersection(list1, list2);
		System.out.println("交集：" + intersection);
	}
}
