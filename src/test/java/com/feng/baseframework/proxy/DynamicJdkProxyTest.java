package com.feng.baseframework.proxy;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.service.UserService;
import com.feng.baseframework.service.impl.UserServiceImpl;
import com.feng.baseframework.util.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.ProxyGenerator;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.assertTrue;

public class DynamicJdkProxyTest extends MockitoBaseTest{

    static Logger logger = LoggerFactory.getLogger(DynamicJdkProxyTest.class);

    static {
        /**
         * 该方法设置无效
         * 因为在ProxyGenerator中saveGeneratedFiles属性是静态属性，jvm容器先加载jre，然后再加载该类
         */
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        try {
            Field field = ProxyGenerator.class.getDeclaredField("saveGeneratedFiles");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true); //Field 的 modifiers 是私有的
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.setAccessible(true);
            field.setBoolean(null, true);
            assertTrue(field.getBoolean(null));
        } catch (NoSuchFieldException e1) {
            logger.error("设置输出动态代理class文件属性失败" + ExceptionUtils.getFullStackTrace(e1));
        } catch (IllegalAccessException e2) {
            logger.error("设置输出动态代理class文件属性失败" + ExceptionUtils.getFullStackTrace(e2));
        }
    }

    @Test
    public void testJdkProxy(){
        /**
         * 该方法设置无效
         * 因为在ProxyGenerator中saveGeneratedFiles属性是静态属性，在类加载时已经完成初始化(通过System.getProperty取值)
         */
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "false");
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