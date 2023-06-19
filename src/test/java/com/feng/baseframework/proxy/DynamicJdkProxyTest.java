package com.feng.baseframework.proxy;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.service.UserService;
import com.feng.baseframework.service.impl.UserServiceImpl;
import com.feng.baseframework.util.FileUtils;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DynamicJdkProxyTest extends MockitoBaseTest{

    @Test
    public void testJdkProxy(){
        DynamicJdkProxy proxy = new DynamicJdkProxy(new UserServiceImpl());
        UserService userService = proxy.getProxyObject();
        assertTrue(userService != null);
        String fullName = userService.getClass().getName();
        String path = FileUtils.getProjectPath() + File.separator + fullName.replaceAll("\\.", "\\\\");

        System.out.println(fullName);
        System.out.println(path);

        userService.deleteUser(1);
    }
}