package com.feng.baseframework.jdk8.lambda;

import com.feng.baseframework.model.Student;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @ProjectName: baseframework
 * @Description: 函数式接口Consumer测试
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:47
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:47
 * @UpdateRemark:
 * @Version: 1.0
 */
public class ConsumerTest {

    //接收T对象，不返回值，进行消费处理
    @Test
    public void testConsumer() {
        Consumer<Student> consumer = s1-> Optional.ofNullable(s1).ifPresent(s2->s2.setName("test"));
        consumer = consumer.andThen(s3->Optional.ofNullable(s3).ifPresent(s4->s4.setId(10)));
        Student student = new Student();
        consumer.accept(student);

        Assert.assertTrue("test".equals(student.getName()));
        Assert.assertTrue(10 == student.getId());
    }
}
