package com.feng.baseframework.proxy;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.interceptor.MyMethodInterceptor;
import com.feng.baseframework.service.UserService;
import com.feng.baseframework.service.impl.UserServiceImpl;
import com.feng.baseframework.util.FileUtils;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DynamicCGLIBProxyTest extends MockitoBaseTest {

	private String outPath;

	@Before
	public void setUp() throws Exception {
		outPath = "E:\\data";
		//在指定目录下生成动态代理类
		System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, outPath);
	}

	@Test
	public void proxyTest() {
		//创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
		Enhancer enhancer = new Enhancer();
		//设置目标类的字节码文件
		enhancer.setSuperclass(UserServiceImpl.class);
		//设置回调函数
		enhancer.setCallback(new CGLIBMethodInterceptor());

		//这里的create方法就是正式创建代理类
		UserService userService = (UserService)enhancer.create();
		assertTrue(userService != null);
		String fullName = userService.getClass().getName();
		String path = outPath + File.separator + fullName.replaceAll("\\.", "\\\\");

		System.out.println(fullName);
		System.out.println(path);

		userService.deleteUser(1);
	}

	@Test
	public void proxyTest2() {
		List<Callback> callbacks = new ArrayList<>();
		callbacks.add(new CGLIBMethodInterceptor());
		UserService userService = DynamicCGLIBProxy.getProxyObject(UserServiceImpl.class, callbacks);
		assertTrue(userService != null);
		String fullName = userService.getClass().getName();
		String path = outPath + File.separator + fullName.replaceAll("\\.", "\\\\");

		System.out.println(fullName);
		System.out.println(path);

		userService.deleteUser(1);
	}
}