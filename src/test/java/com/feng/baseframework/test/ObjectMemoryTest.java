package com.feng.baseframework.test;

import com.feng.baseframework.model.Student;
import com.feng.baseframework.model.Teacher;
import com.feng.baseframework.util.MemoryUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

/**
 * baseframework
 * 2019/2/23 17:27
 * 占用内存测试类
 * 
 * 1.运行报错：java.lang.reflect.InaccessibleObjectException:
 * Unable to make field private final byte[] java.lang.String.value accessible:
 * module java.base does not "opens java.lang" to unnamed module @2096442d
 * 添加参数：--add-opens java.base/java.lang=ALL-UNNAMED
 * 2.找不到包
 * 手动在dependencies添加jar包
 *
 * @author lanhaifeng
 * @since
 **/
public class ObjectMemoryTest {

	private Teacher teacher;
	private Student stu;

	@Before
	public void init(){
		teacher = new Teacher();
		stu = new Student(1, "tom", 11, teacher);
	}

	@Test
	public void testMemory1(){
		Assert.assertTrue("期待对象堆中内存为32", MemoryUtil.getObjecShallowSizeByInstrumentation(stu) == 32);
	}

	@Test
	public void testMemory2(){
		//计算指定对象及其引用树上的所有对象的综合大小，单位字节
		Assert.assertTrue("期待对象堆中内存为144", MemoryUtil.getObjecSizeByRamUsageEstimator(stu) == 144);
		//计算指定对象本身在堆空间的大小，单位字节
		Assert.assertTrue("期待对象堆中内存为32", MemoryUtil.getObjecShallowSizeByRamUsageEstimator(stu) == 32);
		//计算指定对象及其引用树上的所有对象的综合大小，返回可读的结果，如：2KB
		Assert.assertTrue("期待对象及其引用树上的所有对象的综合大小为144 bytes", MemoryUtil.getObjecHumanSizeByRamUsageEstimator(stu).equals("144 bytes"));
	}

	@Test
	public void testMemory3(){
		/**
		 * 引入工具包，在高版本jdk中已经移除ObjectSizeCalculator
		 * <dependency>
		 *     <groupId>org.openjdk.jol</groupId>
		 *     <artifactId>jol-core</artifactId>
		 *     <version>0.13</version>
		 * </dependency>
		 */
		ClassLayout classLayout = ClassLayout.parseInstance(stu);
		GraphLayout graphLayout = GraphLayout.parseInstance(stu);
		System.out.println(graphLayout.totalSize());
		System.out.println(MemoryUtil.getObjecSizeByRamUsageEstimator(stu));
		System.out.println(MemoryUtil.getObjecSizeByRamUsageEstimator(stu));

		//查看对象内部信息
		System.out.println(classLayout.toPrintable());

		//查看对象外部信息
		System.out.println(graphLayout.toPrintable());

		//获取对象总大小
		System.out.println("size : " + graphLayout.totalSize());

		Assert.assertEquals("期待对象引用大小为4", 4, classLayout.headerSize());
		Assert.assertEquals("期待对象堆中内存为144", 144, graphLayout.totalSize());
	}

	@Test
	public void testMemory4(){
		String[] strs = new String[]{"sda","sda","sda","sda"};
		Assert.assertTrue("期待对象堆中内存为32", MemoryUtil.getObjecShallowSizeByRamUsageEstimator(strs) == 32);
	}

}
