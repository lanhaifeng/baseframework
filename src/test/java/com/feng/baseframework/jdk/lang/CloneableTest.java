package com.feng.baseframework.jdk.lang;

import com.feng.baseframework.model.Student;
import com.feng.baseframework.model.Teacher;
import com.feng.baseframework.util.BeanUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: 测试Cloneable接口
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/23 20:24
 * @UpdateUser:
 * @UpdateDate: 2018/10/23 20:24
 * @UpdateRemark:
 * @Version: 1.0
 */
public class CloneableTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testDefaultClone() throws CloneNotSupportedException {
        Teacher t1 = new Teacher(1l,"t1",50);
        Student s1 = new Student(1,"s1",10, t1);
        Student s2 = s1.clone();
        s2.getTeacher().setName("t2");
        System.out.println("Student is " + (s1.getTeacher() == s2.getTeacher() ? "shallow clone" : "deep clone"));
        System.out.println("Student is " + (s1.getTeacher().equals(s2.getTeacher()) ? "equals clone" : "not equals clone"));

        Student s3 = new Student(3,"s3",13, null);
        ArrayList<Student> students = new ArrayList<>();
        students.add(s3);

        ArrayList<Student> studentList = (ArrayList<Student>)students.clone();
        studentList.get(0).setName("s4");
        System.out.println(studentList.get(0).getName());
        System.out.println(students.get(0).getName());
        System.out.println("ArrayList is " + (studentList == students ? "shallow clone" : "deep clone"));
        System.out.println("ArrayList is " + (studentList.equals(students) ? "equals clone" : "not equals clone"));

        Teacher t2 = new Teacher(1l,"t1",50, students);
        Teacher t3 = t2.clone();

        System.out.println("Teacher is " + (t2.getStudents() == t3.getStudents() || t2.getStudents().get(0) == t3.getStudents().get(0) ? "shallow clone" : "deep clone"));
        System.out.println("Teacher is " + (t2.getStudents().equals(t3.getStudents()) ? "equals clone" : "not equals clone"));
    }

    @Test
    public void testBeanUtilClone(){
        Teacher t1 = new Teacher(1l,"t1",50);
        Student s1 = new Student(1,"s1",10, t1);
        Student s4 = BeanUtil.deepCloneObject(s1);
        System.out.println("BeanUtil method is " + (s1.getTeacher() == s4.getTeacher() ? "shallow clone" : "deep clone"));
        System.out.println("BeanUtil method is " + (s1.getTeacher().equals(s4.getTeacher()) ? "equals clone" : "not equals clone"));

        Student s3 = new Student(3,"s3",13, null);
        List<Student> students = new ArrayList<>();
        students.add(s3);
        Teacher t2 = new Teacher(1l,"t1",50, students);
        Teacher t4 = BeanUtil.deepCloneObject(t2);
        System.out.println("BeanUtil method is " + (t2.getStudents() == t4.getStudents() || t2.getStudents().get(0) == t4.getStudents().get(0) ? "shallow clone" : "deep clone"));
        System.out.println("BeanUtil method is " + (t2.getStudents().equals(t4.getStudents()) ? "equals clone" : "not equals clone"));
    }

}
