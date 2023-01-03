package com.feng.baseframework.test;

import com.feng.baseframework.mapper.StudentMapper;
import com.feng.baseframework.model.Student;
import com.github.pagehelper.PageHelper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


/**
 * @ProjectName: testProject
 * @Description: StudentMapper单元测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/4/30 0:55
 * @UpdateUser:
 * @UpdateDate: 2018/4/30 0:55
 * @UpdateRemark:
 * @Version: 1.0
 */
@Transactional
public class StudentMapperTest extends BaseFrameworkApplicationTest {

    @Autowired
    private StudentMapper studentMapper;
    private Student student;

    @Test
    public void addStudentTest(){
        student = new Student();
        student.setName("tom");
        student.setAge(11);
        studentMapper.addStudent(student);
        Assert.assertNotNull("新增学生记录失败,主键为空",student.getId());
    }

    @Test
    public void getStudentByIdTest(){
        student = new Student();
        student.setName("jack");
        student.setAge(12);
        studentMapper.addStudent(student);
        Student temp = studentMapper.getStudentById(student.getId());
        Assert.assertEquals("根据id查询失败,姓名不相同",student.getName(),temp.getName());
    }

    @Test
    public void updateStudentTest(){
        student = new Student();
        student.setName("lucy");
        student.setAge(13);
        studentMapper.addStudent(student);
        Integer id = student.getId();
        student.setName("lucyTest");
        student.setAge(14);
        studentMapper.updateStudent(student);
        Student temp = studentMapper.getStudentById(id);
        Assert.assertEquals("姓名没有变化,更新失败",student.getName(),temp.getName());
        Assert.assertEquals("年龄没有变化,更新失败",student.getAgeTest(),temp.getAgeTest());
    }

    @Test
    public void getStudentsLikeNameTest(){
        PageHelper.startPage(1,5);
        List<Student> sudents = studentMapper.getStudentsLikeName("tom");
        Assert.assertNotNull("集合为空",sudents);
        Assert.assertTrue("名字不是以tom开头",sudents.get(0).getName().startsWith("tom"));
    }

    @Test
    public void deleteStudentsTest(){
        student = new Student();
        student.setName("lucy");
        student.setAge(13);
        studentMapper.addStudent(student);
        int id1 = student.getId();
        student.setId(null);
        studentMapper.addStudent(student);
        int id2 = student.getId();
        studentMapper.deleteStudents((List<Integer>) Arrays.asList(new Integer[]{id1,id2}));
    }
}
