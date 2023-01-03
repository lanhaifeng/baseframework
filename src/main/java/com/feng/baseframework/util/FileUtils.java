package com.feng.baseframework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Objects;

/**
 * baseframework
 * 2018/9/30 16:48
 * 文件操作工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 将文本文件中的内容读入到buffer中
	 * @param buffer buffer
	 * @param filePath 文件路径
	 * @throws IOException 异常
	 * @author cn.outofmemory
	 * @date 2013-1-7
	 */
	public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		readToBuffer(buffer, is);
	}

	/**
	 * 将文本文件中的内容读入到buffer中
	 * @param buffer buffer
	 * @param is 文件流
	 * @throws IOException 异常
	 * @author cn.outofmemory
	 * @date 2013-1-7
	 */
	public static void readToBuffer(StringBuffer buffer, InputStream is) throws IOException {
		//用来保存每行读取的内容
		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		//读取第一行
		line = reader.readLine();
		//如果line为空说明读完了
		while (line != null) {
			//将读到的内容添加到buffer中
			buffer.append(line);
			//添加换行符
			buffer.append("\n");
			//读取下一行
			line = reader.readLine();
		}
		reader.close();
		is.close();
	}

	/**
	 * 读取文本文件内容
	 * @param filePath 文件所在路径
	 * @return 文本内容
	 * @throws IOException 异常
	 * @author cn.outofmemory
	 * @date 2013-1-7
	 */
	public static String readFile(String filePath) throws IOException {
		StringBuffer sb = new StringBuffer();
		FileUtils.readToBuffer(sb, filePath);
		return sb.toString();
	}

	/**
	 * 2018/9/30 17:23
	 * 获取项目编译路径
	 *
	 * @author lanhaifeng
	 * @return
	 */
	public static String getWebRootPath() {
		try {
			return ResourceUtils.getFile("classpath:").getPath();
		} catch (FileNotFoundException e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return null;
		}
	}

	/**
	 * 2020/10/29 15:19
	 * 获取工程目录
	 * 1.System.getProperty("user.dir");
	 * 2.File directory = new File("");
	 * 	 directory.getCanonicalPath();
	 *
	 * @param
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String getProjectPath(){
		return System.getProperty("user.dir");
	}

	/**
	 * 2018/9/30 17:23
	 * 根据相对路径获取文件
	 *
	 * @param relativePath
	 * @author lanhaifeng
	 * @return
	 */
	public static File getFileByRelativePath(String relativePath) {
		try {
			if(relativePath != null && (!relativePath.startsWith("/") || !relativePath.startsWith("\\"))){
				relativePath = File.separator + relativePath;
			}
			String path = getWebRootPath() + relativePath;
			int index = path.lastIndexOf("/") == -1 ? path.lastIndexOf("\\") : path.lastIndexOf("/");

			File dir = new File(path.substring(0,index));
			if(!dir.exists()){
				dir.mkdirs();
			}
			File pathFile = new File(path);
			if(!pathFile.exists()){
				pathFile.createNewFile();
			}
			return pathFile;
		} catch (FileNotFoundException e1) {
			logger.error(ExceptionUtil.getStackTrace(e1));
			return null;
		}catch (IOException e2) {
			logger.error(ExceptionUtil.getStackTrace(e2));
			return null;
		}
	}

	/**
	 * 2019/12/5 10:25
	 * 根据路径获取文件
	 *
	 * @param path
	 * @author lanhaifeng
	 * @return java.io.File
	 */
	public static File getFile(String path) {
		try {
			if (StringUtils.isBlank(path)) return null;
			if (path.startsWith("classpath:")) {
				return ResourceUtils.getFile(path);
			}
			if (path.startsWith(File.separator)) return new File(path);
			if (!path.startsWith("/") || !path.startsWith("\\")) return new File(path);
			return null;
		} catch (Exception e) {
			logger.error("获取文件失败，错误：" + ExceptionUtils.getFullStackTrace(e));
			return null;
		}
	}

	/**
	 * 2019/12/4 17:36
	 * 文件反序列化对象
	 *
	 * @param filePath		路径
	 * @author lanhaifeng
	 * @return T
	 */
	public static <T>T readObject(String filePath) throws IOException, ClassNotFoundException {
		Assert.state(StringUtils.isNotBlank(filePath), "filePath参数不能为空");
		ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(filePath));
		return (T)objIn.readObject();
	}

	/**
	 * 2019/12/4 17:39
	 * 将对象序列化为文件
	 *
	 * @param filePath
	 * @param t
	 * @author lanhaifeng
	 * @return void
	 */
	public static <T> void saveObjFile(String filePath, T t) throws IOException {
		Objects.requireNonNull(t);
		Assert.state(StringUtils.isNotBlank(filePath), "filePath参数不能为空");
		ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(filePath));
		objOut.writeObject(t);
	}

	public static String getFileName(String fullName) {
		if (StringUtils.isBlank(fullName)) {
			return "";
		} else {
			int index = -1;
			int target;
			if ((target = fullName.lastIndexOf("/")) != -1) {
				index = target;
			}

			if ((target = fullName.lastIndexOf("\\")) != -1 && target > index) {
				index = target;
			}

			return fullName.substring(index + 1);
		}
	}

	/**
	 * 描述: 获取资源流对象<br/>
	 * 时间:17:29 2022/3/1 <br/>
	 *
	 * @param filePath
	 * @author lanhaifeng
	 * @return java.io.InputStream
	 */
	public static InputStream getResource(String filePath) {
		return FileUtils.class.getResourceAsStream(filePath);
	}


	public static void main(String[] args) throws IOException {
		System.out.println(getWebRootPath());
		System.out.println(ResourceUtils.getFile("classpath:").getPath());
		File file = ResourceUtils.getFile("classpath:ruleTemplate/Tp.drl");
		System.out.println("sdfsd"+new ClassPathResource("classpath:").getPath());

		if(file != null && file.exists() && file.isFile()){
			System.out.println(file.getPath());
			System.out.println(readFile(file.getPath()));
		}
	}
}
