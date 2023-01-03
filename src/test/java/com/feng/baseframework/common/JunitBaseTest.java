package com.feng.baseframework.common;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @ProjectName: svc-search-biz
 * @description: junit测试基本类
 * @author: lanhaifeng
 * @create: 2018-05-22 14:22
 * @UpdateUser:
 * @UpdateDate: 2018/5/22 14:22
 * @UpdateRemark:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@Category(SlowTests.class)
public class JunitBaseTest {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    @Ignore
    public void testNull(){

    }
}
