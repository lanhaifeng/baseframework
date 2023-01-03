package com.feng.baseframework.jdk8.util;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.model.Student;
import com.feng.baseframework.model.Teacher;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Optional;

/**
 * @ProjectName: baseframework
 * @Description: 工具类Optional测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:50
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:50
 * @UpdateRemark:
 * @Version: 1.0
 */
public class OptionalTest extends MockitoBaseTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	//转换值
	@Test
	public void testMap(){
		Student student = null;
		Assert.assertFalse("expect student id is null",Optional.ofNullable(student).map(e -> e.getId()).orElse(0) > 0);

		student = new Student();
		Assert.assertFalse("expect student id is null",Optional.ofNullable(student).map(e -> e.getId()).orElse(0) > 0);

		student.setId(1);
		Assert.assertTrue("expect student id is not null",Optional.ofNullable(student).map(e -> e.getId()).orElse(0) > 0);
	}

	//与map的区别，在于flatMap返回的是被Optional包装的期待值，map返回则是期待的值
	@Test
	public void testFlatMap() {
		Student student = new Student();
		student.setAge(1);
		Assert.assertTrue("student age is not null",Optional.ofNullable(student).flatMap(e -> e.getAgeTest()).orElse(0) > 0);
	}

	@Test
	public void testGet(){
		Integer id = null;
		id = Optional.ofNullable(id).orElse(1);
		Assert.assertTrue("Optional赋值失败",id == 1);
		id = 2;
		id = Optional.ofNullable(id).orElse(1);
		Assert.assertTrue("Optional赋值失败",id == 2);
	}

	@Test
	public void testOrElseGet1() {
		expectedException.expect(NullPointerException.class);

		Student student = null;
		//orElseGet需要接收一个非空对象
		Student result = Optional.ofNullable(student).orElseGet(null);
		Optional.ofNullable(result).ifPresent(r-> Assert.assertTrue("student is not null", r == null));
	}

	//orElseGet
	@Test
	public void testOrElseGet2() {
		Student student1 = null;
		Student student2 = null;
		//orElseGet需要接收一个非空对象
		Student result = Optional.ofNullable(student1).orElseGet(()->Optional.ofNullable(student2).orElseGet(()->new Student("test")));
		Assert.assertTrue("student is not null", "test".equals(result.getName()));
	}

	//orElse对象不为空时也会执行
	//orElseGet对象不为空时不会执行，尽量使用该方法
	@Test
	public void testOrElseGet3() {
		Student result = new Student("test");
		Optional.ofNullable("test").orElseGet(()->{String str = "test1"; result.setName(str);return str;});
		Assert.assertTrue("student name is change, so orElseGet execute when result is not null", !"test1".equals(result.getName()));

		Optional.ofNullable("test").orElse(result.nameToUpperCase());
		Assert.assertTrue("student name is not change, so orElse not execute when result is not null", "test".toUpperCase().equals(result.getName()));
	}

	//过滤值
	@Test
	public void testFilter() {
		Student student = new Student("test");
		Optional<Student> result = Optional.ofNullable(student).filter(s -> StringUtils.isNotBlank(s.getName()));
		Assert.assertTrue("student name is not null, so return source object", student.getName().equals(result.get().getName()));

		result = Optional.ofNullable(student).filter(s -> StringUtils.isNotBlank(s.getHobby()));
		Assert.assertFalse("student hobby is null, so return null", result.isPresent());
	}

	//链式调用
	@Test
	public void chainCallTest() {
		Student student = new Student("test");

		Assert.assertFalse("expect tearch name is null", Optional.ofNullable(student).flatMap(s->Optional.ofNullable(s.getTeacher())).map(t->t.getName()).isPresent());

		Teacher teacher = new Teacher(1l, "teacher", 40);
		student.setTeacher(teacher);
		Assert.assertTrue("expect chain call get teacher name", teacher.getName().equals(Optional.ofNullable(student).flatMap(s->Optional.ofNullable(s.getTeacher())).map(t->t.getName()).orElse("")));
		Assert.assertTrue("expect chain call get teacher name", teacher.getName().equals(Optional.ofNullable(student).map(s->s.getTeacher()).map(t->t.getName()).orElse("")));
		Assert.assertTrue("expect chain call get teacher name", teacher.getName().equals(Optional.ofNullable(student).map(Student::getTeacher).map(Teacher::getName).orElse("")));
	}
}
