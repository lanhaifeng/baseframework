package com.feng.baseframework.util;

import com.feng.baseframework.common.MockitoBaseTest;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JavaCompilerUtilTest extends MockitoBaseTest {

	private String className;
	private String methodName;
	private StringBuffer javaCodes;

	private String classFilePath1;
	private String classFilePath2;
	private String packageName;
	private String suffix1;
	private String suffix2;

	private String outDir;

	@Before
	public void setUp() throws Exception {
		packageName = "com.feng.baseframework.util";
		className = "JavaCompilerTest";
		suffix1 = "1";
		suffix2 = "2";

		javaCodes = new StringBuffer();
		javaCodes.append("package ").append(packageName).append(";");
		javaCodes.append("public class JavaCompilerTest{");
		javaCodes.append("  public static String test(String[] args){");
		javaCodes.append("    return \"hello test compile\";");
		javaCodes.append("  }");
		javaCodes.append("}");

		methodName = "test";

		classFilePath1 = System.getProperty("user.dir") + File.separator + "src/test/resources/compile\\JavaCompilerTest1.java";
		classFilePath2 = System.getProperty("user.dir") + File.separator + "src/test/resources/compile\\JavaCompilerTest2.java";

		outDir = "C:\\Users\\feng\\Desktop\\test";
	}

	@Test
	public void compile1() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		className = packageName + "." + className;
		Class cls = JavaCompilerUtil.compileByStr(className, javaCodes.toString(), outDir);
		Assert.assertNotNull(cls);
		Assert.assertTrue(className.equals(cls.getName()));

		Object obj = cls.newInstance();
		Method method = cls.getMethod(methodName, String[].class);
		Object invokeResult = method.invoke(obj, new Object[] { new String[] {} });

		Assert.assertNotNull(invokeResult);
		Assert.assertTrue("hello test compile".equals(invokeResult));

		String clsFilePaht = cls.getResource("").getPath();
		Assert.assertTrue(StringUtils.isNotBlank(clsFilePaht)
				&& clsFilePaht.contains("com/feng/baseframework/util"));
	}

	@Test
	public void compile2() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Class cls = JavaCompilerUtil.compileByFile(classFilePath1, packageName + suffix1, outDir);

		Assert.assertNotNull(cls);
		Assert.assertTrue((packageName + suffix1 + "." + className + suffix1).equals(cls.getName()));

		Object obj = cls.newInstance();
		Method method = cls.getMethod(methodName, String[].class);
		Object invokeResult = method.invoke(obj, new Object[] { new String[] {} });

		Assert.assertNotNull(invokeResult);
		Assert.assertTrue("hello test compile".equals(invokeResult));

		String clsFilePaht = cls.getResource("").getPath();
		Assert.assertTrue(StringUtils.isNotBlank(clsFilePaht)
				&& clsFilePaht.contains("com/feng/baseframework/util"));
	}

	@Test
	public void compile3() throws ClassNotFoundException, MalformedURLException {
		JavaCompilerUtil.compileByFiles(new String[]{classFilePath2, classFilePath1}, outDir);
		if(!outDir.endsWith("/") && !outDir.endsWith("\\")){
			outDir += File.separator;
		}

		URL[] urls = new URL[]{new URL("file:" + outDir)};
		URLClassLoader classLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

		Class cls1 = classLoader.loadClass(packageName + suffix1 + "." + className + suffix1);
		Class cls2 = classLoader.loadClass(packageName + suffix2 + "." + className + suffix2);

		Assert.assertNotNull(cls1);
		Assert.assertNotNull(cls2);
	}
}