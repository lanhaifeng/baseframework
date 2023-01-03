package com.feng.baseframework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * baseframework
 * 2020/7/29 15:51
 * java编译工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class JavaCompilerUtil {

	static Logger logger = LoggerFactory.getLogger(JavaCompilerUtil.class);

	/**
	 * 2020/7/29 16:49
	 * 将字符串编译成class文件并加载
	 *
	 * @param className	类完整名
	 * @param javaCodes	代码内容
	 * @param outDir	输出路径，默认放在源文件路径下
	 * @author lanhaifeng
	 * @return java.lang.Class<?>
	 */
	public static Class<?> compileByStr(String className, String javaCodes, String outDir){
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics,null, null);

		StrSrcJavaObject srcObject = new StrSrcJavaObject(className, javaCodes);
		Iterable<? extends JavaFileObject> fileObjects = Arrays.asList(srcObject);

		String flag = "-d";
		outDir = getOutputDir(outDir);
		Iterable<String> options = Arrays.asList(flag, outDir);

		JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, options, null, fileObjects);
		return task.call() ? ClassLoaderUtil.loadClassByPath(className, outDir) : null;
	}

	/**
	 * 2020/8/4 14:45
	 * 构建输出路径
	 *
	 * @param outDir
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	private static String getOutputDir(String outDir) {
		String currentDir = "";
		try {
			File classPath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
			currentDir = classPath.getAbsolutePath() + File.separator;
		} catch (URISyntaxException e) {
			logger.error("获取当前类路径失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}
		if(StringUtils.isNotBlank(outDir)){
			File outDirFile = new File(outDir);
			if(!outDirFile.exists()){
				outDirFile.mkdirs();
			}
			if(!outDirFile.isDirectory()){
				outDir = currentDir;
				outDirFile.delete();
			}
			//注意使用URLClassLoader加载class文件时，如果路径不是以分隔符结果，Windows环境下将加载失败，linux未测试
			if(!outDir.endsWith("/") && !outDir.endsWith("\\")){
				outDir += File.separator;
			}
		}
		return outDir;
	}

	/**
	 * 2020/8/4 9:58
	 * 批量编译java文件
	 *
	 * @param classFiles    .java文件路径集合
	 * @param outDir		编译输出目录
	 * @author lanhaifeng
	 * @return void
	 */
	public static void compileByFiles(String[] classFiles, String outDir){
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null,null,null);
		List<File> files = new ArrayList<>();
		for (String classFile : classFiles) {
			files.add(new File(classFile));
		}
		Iterable<? extends JavaFileObject> compilationUnits = manager.getJavaFileObjectsFromFiles(files);

		// 编译
		// 设置编译选项，配置class文件输出路径
		outDir = getOutputDir(outDir);
		Iterable<String> options = Arrays.asList("-d", outDir);
		JavaCompiler.CompilationTask task = javaCompiler.getTask(
				null, manager, null, options, null, compilationUnits);
		// 执行编译任务
		boolean result = task.call();
		if(!result) {
			logger.error("编译失败！");
			throw new RuntimeException("编译失败！");
		}
	}

	/**
	 * 2020/7/30 10:55
	 * 将.java文件编译成class文件并加载
	 *
	 * @param classFile
	 * @param packageName
	 * @author lanhaifeng
	 * @return java.lang.Class<?>
	 */
	public static Class<?> compileByFile(String classFile, String packageName, String outDir) {
		if(StringUtils.isBlank(classFile) || !classFile.endsWith(".java")) return null;
		outDir = getOutputDir(outDir);
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		String className = parseClassName(classFile, packageName);

		int result = compiler.run(null, null, null, "-d" , outDir, classFile);
		return result == 0 ? ClassLoaderUtil.loadClassByPath(className, outDir) : null;
	}

	/**
	 * 2020/8/4 16:30
	 * 根据文件名和包路径得到全类名
	 *
	 * @param classFile
	 * @param packageName
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String parseClassName(String classFile, String packageName) {
		int separatorIndex = classFile.lastIndexOf(File.separator);
		if(separatorIndex == -1) separatorIndex = classFile.lastIndexOf("\\");
		int endIndex = classFile.length() - 5;
		if(classFile.endsWith(".class")){
			endIndex = classFile.length() - 6;
		}
		String className = classFile.substring(separatorIndex + 1, endIndex);
		if(StringUtils.isNotBlank(packageName)){
			className = packageName + "." + className;
		}
		return className;
	}

	private static class StrSrcJavaObject extends SimpleJavaFileObject {
		private String content;
		public StrSrcJavaObject(String name, String content) {
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.content = content;
		}
		public CharSequence getCharContent(boolean ignoreEncodingErrors) {
			return content;
		}
	}
}
