package com.feng.baseframework.jdk8.lambda;

import com.feng.baseframework.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 测试集合的stream
 *
 * @author lanhaifeng
 * @version v1.1.0
 * @apiNote 时间:2022/5/9 20:41创建:StreamTest
 * @since v1.1.0
 */
public class StreamTest {

    static class TestList {
        private List<String> data;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    @Test
    public void join() {
        List<TestList> datas = new ArrayList<>();
        TestList l1 = new TestList();
        l1.setData(Arrays.asList("a1", "b1", "c1"));

        datas.add(l1);

        l1 = new TestList();
        l1.setData(Arrays.asList("d1", "e1", "f1"));

        datas.add(l1);

        List<String> strs = new ArrayList<>();
        datas.stream().map(TestList::getData).forEach(strs::addAll);
        System.out.println(String.join(",", strs));
    }

    @Test
    public void max() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1, "test1", 100, null));
        students.add(new Student(3, "test3", 300, null));
        students.add(new Student(1, "test2", 200, null));
        System.out.println(students.stream().map(Student::getAge).max(Integer::compareTo).get());
    }
}
