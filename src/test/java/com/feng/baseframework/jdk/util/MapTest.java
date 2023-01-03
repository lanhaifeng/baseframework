package com.feng.baseframework.jdk.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * baseframework
 * 2021/4/28 16:39
 * 测试类
 *
 * @author lanhaifeng
 * @since
 **/
public class MapTest {


	@Test
	public void putTest() {
		HashMap<String, String> map = new HashMap<>();
		System.out.println(map.put("test", "test"));
	}
}
