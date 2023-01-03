package com.feng.baseframework.jvmload;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.service.UserService;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @ProjectName: baseframework
 * @Description: 测试ServiceLoader
 * @Author: lanhaifeng
 * @CreateDate: 2019/3/16 23:00
 * @UpdateUser:
 * @UpdateDate: 2019/3/16 23:00
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ServiceLoaderTest extends MockitoBaseTest {

    @Test
    public void loadNotServiceConfigFile(){
        ServiceLoader<UserService> serviceLoader = ServiceLoader.load(UserService.class);
        Iterator<UserService> iterator = serviceLoader.iterator();
        Boolean result = false;
        while (iterator.hasNext()){
            Assert.assertNotNull("没有找到UserService实现类", iterator.next().getClass());
            result = true;
        }
        Assert.assertTrue("没有找到UserService实现类", result);
    }
}
