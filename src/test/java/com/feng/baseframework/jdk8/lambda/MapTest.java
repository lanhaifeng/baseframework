package com.feng.baseframework.jdk8.lambda;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map的lambda测试
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/9/8 17:12创建:MapTest
 * @since 1.0
 */
public class MapTest {

    @Test
    public void computeIfAbsentTest() {
        Map<String, List<String>> map = new HashMap<>();
        map.computeIfAbsent("test", k -> Lists.newArrayList()).add("test1");

        map.computeIfAbsent("test", k -> Lists.newArrayList()).add("test2");

        Assert.assertEquals("测试computeIfAbsent失败", map.get("test").get(0), "test1");
        Assert.assertEquals("测试computeIfAbsent失败", map.get("test").get(1), "test2");
    }
}
