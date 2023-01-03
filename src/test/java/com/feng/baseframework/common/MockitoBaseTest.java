package com.feng.baseframework.common;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @ProjectName:    svc-search-biz
 * @Description:    单元测试基础类，用于继承
 * @Author:         兰海峰
 * @CreateDate:     2018/4/28 17:20
 * @UpdateUser:
 * @UpdateDate:     2018/4/28 17:20
 * @UpdateRemark:
 */

@RunWith(MockitoJUnitRunner.class)
@Category(FastTests.class)
public class MockitoBaseTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void mockitoTest(){
    }

}
