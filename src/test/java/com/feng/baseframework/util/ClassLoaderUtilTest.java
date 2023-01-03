package com.feng.baseframework.util;

import com.feng.baseframework.common.MockitoBaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

public class ClassLoaderUtilTest extends MockitoBaseTest {

	private String classFilePath1;
	private String classFilePath2;
	private String classFilePath3;

	private String outDir;
	private String jarPath;

	@Before
	public void setUp() throws Exception {
		classFilePath1 = System.getProperty("user.dir") + File.separator + "src/test/resources/compile\\JavaCompilerTest1.java";
		classFilePath2 = System.getProperty("user.dir") + File.separator + "src/test/resources/compile\\JavaCompilerTest2.java";
		classFilePath3 = System.getProperty("user.dir") + File.separator + "src/test/resources/compile\\JavaCompilerTest3.java";
		jarPath = System.getProperty("user.dir") + File.separator + "src/test/resources/compile/memory-calculation.jar";
		outDir = "C:\\Users\\feng\\Desktop\\test";

		JavaCompilerUtil.compileByFiles(new String[]{classFilePath2, classFilePath1, classFilePath3}, outDir);
	}

	@Test
	public void loadClassByPath() {
		String fullClassName = "com.feng.baseframework.util1.JavaCompilerTest1";
		Class cls = ClassLoaderUtil.loadClassByPath(fullClassName, outDir);
		Assert.notNull(cls);
		Assert.state(cls.getName().equals(fullClassName));
	}

	@Test
	public void loadDirClassByPath() {
		Collection<Class<?>> classes = ClassLoaderUtil.loadDirClassByPath(outDir, null, null);

		Assert.notNull(classes);
		Assert.state(classes.size() == 3);
	}

	@Test
	public void loadJarClassByPath() {
		Collection<Class<?>> classes = ClassLoaderUtil.loadJarClassByPath(jarPath);
		Assert.notNull(classes);
		Assert.state(classes.size() > 0);
	}
}