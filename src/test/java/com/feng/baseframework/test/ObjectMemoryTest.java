package com.feng.baseframework.test;

import com.feng.baseframework.model.Student;
import com.feng.baseframework.model.Teacher;
import com.feng.baseframework.util.ClassIntrospector;
import com.feng.baseframework.util.MemoryUtil;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * baseframework
 * 2019/2/23 17:27
 * 占用内存测试类
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
		ObjectSizeCalculator.MemoryLayoutSpecification memoryLayoutSpecification = ObjectSizeCalculator.getEffectiveMemoryLayoutSpecification();

		System.out.println(memoryLayoutSpecification.getObjectHeaderSize());
		System.out.println(memoryLayoutSpecification.getArrayHeaderSize());
		System.out.println(memoryLayoutSpecification.getReferenceSize());
		System.out.println(ClassIntrospector.getObjectRefSize());
		System.out.println(ObjectSizeCalculator.getObjectSize(stu));
		System.out.println(MemoryUtil.getObjecSizeByRamUsageEstimator(stu));
		System.out.println(MemoryUtil.getObjecSizeByRamUsageEstimator(stu));

		Assert.assertTrue("期待对象引用大小为4", ClassIntrospector.getObjectRefSize() == 4);
		Assert.assertTrue("期待对象堆中内存为144", ObjectSizeCalculator.getObjectSize(stu) == 144);



		Assert.assertTrue("期待数组头大小为16", memoryLayoutSpecification.getArrayHeaderSize() == 16);
		Assert.assertTrue("期待对象头大小为12", memoryLayoutSpecification.getObjectHeaderSize() == 12);
		Assert.assertTrue("期待对象填充大小为8", memoryLayoutSpecification.getObjectPadding() == 8);
		Assert.assertTrue("期待对象引用大小为4", memoryLayoutSpecification.getReferenceSize() == 4);
		Assert.assertTrue("期待父属性填充大小为4", memoryLayoutSpecification.getSuperclassFieldPadding() == 4);
	}

	@Test
	public void testMemory4(){
		String[] strs = new String[]{"sda","sda","sda","sda"};
		Assert.assertTrue("期待对象堆中内存为32", MemoryUtil.getObjecShallowSizeByRamUsageEstimator(strs) == 32);
	}

}
