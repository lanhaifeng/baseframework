package com.feng.baseframework.jdk8.lambda;

import com.feng.baseframework.model.Student;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;
import lombok.Setter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ProjectName: baseframework
 * @Description: 集合的stream操作
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:53
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:53
 * @UpdateRemark:
 * @Version: 1.0
 */
public class StreamTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
        List<com.feng.baseframework.jdk8.lambda.StreamTest.TestList> datas = new ArrayList<>();
        com.feng.baseframework.jdk8.lambda.StreamTest.TestList l1 = new com.feng.baseframework.jdk8.lambda.StreamTest.TestList();
        l1.setData(Arrays.asList("a1", "b1", "c1"));

        datas.add(l1);

        l1 = new com.feng.baseframework.jdk8.lambda.StreamTest.TestList();
        l1.setData(Arrays.asList("d1", "e1", "f1"));

        datas.add(l1);

        List<String> strs = new ArrayList<>();
        datas.stream().map(com.feng.baseframework.jdk8.lambda.StreamTest.TestList::getData).forEach(strs::addAll);
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

    /**
     * 使用流的方式实现，集合的subList方法，子集合与父集合无必然关系，当然，子集合中的元素引用与父集合相同元素的引用是同一个
     * 原生的subList方法返回的是父集合的视图，当修改子集合的结构，父集合的结构也会发生变化；
     * 子集合截取后，当父集合的结构发生变化，再使用子集合系统会抛出java.util.ConcurrentModificationException异常
     */
    @Test
    public void testSubList(){
        List<Object> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

        List<Object> list1 = list.stream().filter((t)->t!=null).limit(2).collect(Collectors.toList());
        List<Object> list2 = list.subList(0,2);

        System.out.println(list1); //[1, 2]
        System.out.println(list2); //[1, 2]

        list1.remove(0);
        System.out.println(list1); //[2]
        System.out.println(list); //[1, 2, 3, 4]

        list2.remove(0);
        System.out.println(list2); //[2]
        System.out.println(list); //[2, 3, 4]

        list.add("5");
        System.out.println(list); //[2, 3, 4, 5]
        System.out.println(list1); //[2]
        expectedException.expect(ConcurrentModificationException.class);
        System.out.println(list2); //java.util.ConcurrentModificationException
    }

    @Test
    public void testSorted() {
        List<Student> students = new ArrayList<>();
        Student student = null;
        for (int i = 2; i < 10; i++) {
            student = new Student();
            student.setId(i);
            students.add(student);
        }

        List<Integer> ids =students.stream().sorted(Comparator.comparing(Student::getId).reversed()).map(s->s.getId()).collect(Collectors.toList());
        System.out.println(ids);
        ids =students.stream().map(s->s.getId()).collect(Collectors.toList());

        System.out.println(ids);
    }

    //测试lamblambda执行是否另起线程
    @Test
    public void testLambdaExecute() {
        List<Integer> nums = new ArrayList<>();
        nums.add(1);
        nums.add(2);
        nums.add(3);

        int d=0;
        ThreadInfo mainThreadInfo = new ThreadInfo(Thread.currentThread());
        ThreadInfo lambda1ThreadInfo = new ThreadInfo();
        ThreadInfo lambda2ThreadInfo = new ThreadInfo();
        BinaryOperator<Integer> addFunction = (a,b)->{
            lambda1ThreadInfo.build(Thread.currentThread());
            return a+b+d;
        };

        addFunction.apply(1,2);

        CountDownLatch cdl = new CountDownLatch(1);

        new Thread(()->{
            lambda2ThreadInfo.build(Thread.currentThread());
            cdl.countDown();
        }).start();
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Assert.state(mainThreadInfo.equals(lambda1ThreadInfo), "期待普通lambda表达式在主线程执行");
        Assert.state(!mainThreadInfo.equals(lambda2ThreadInfo), "期待普通线程lambda表达式在另一个线程执行");
    }

    @Test
    public void teseStreamCreate(){
        List<String> datas = new ArrayList<>();

        //Stream.builder()
        Stream<String> stream = Stream.<String>builder().add("a").build();
        System.out.println(stream.reduce(String::concat).get());

        //List.stream()
        datas.add("test");
        stream = datas.stream();

        //Stream.empty()
        //如果创建空流，要使用empty（）方法，避免为没有元素的流返回Null.
        stream = datas == null ? Stream.empty() : datas.stream();
        System.out.println(stream.reduce(String::concat).get());

        //Stream.of()
        stream = Stream.of("a");
        System.out.println(stream.reduce(String::concat).get());

        //Stream.of()
        stream = Stream.of("a", "b", "c");
        System.out.println(stream.reduce(String::concat).get());

        //Stream.iterate()
        stream = Stream.iterate("a", a -> a + "1").limit(4);
        System.out.println(stream.reduce(String::concat).get());

        //Stream.generate()
        stream = Stream.generate(()->"a").limit(3);
        System.out.println(stream.reduce(String::concat).get());

        //Stream.concat()
        stream = Stream.concat(Stream.of("b"), Stream.generate(()->String.valueOf(new Random().nextInt()))).limit(7);
        System.out.println(stream.reduce(String::concat).get());
    }

    @Test
    public void teseStreamOf() {
        //字符串拼接
        String concatStr = Stream.of("a", "b", "c").reduce("-", String::concat);
        Assert.state("-abc".equals(concatStr));

        concatStr = Stream.of("a", "b", "c").reduce(String::concat).get();
        Assert.state("abc".equals(concatStr));

        //求最小值
        double minVal = Stream.of(-1.5, 1.0, -9.9, 2.0).reduce(Double::min).get();
        Assert.state(minVal == -9.9);

        minVal = Stream.of(-1.5, 1.0, -9.9, 2.0).reduce(-12.1, Double::min);
        Assert.state(minVal == -12.1);

        //求最大值
        int sum = Stream.of(1,2,3,4).reduce(Integer::sum).get();
        Assert.state(sum == 10);

        sum = Stream.of(1,2,3,4).reduce(7, Integer::sum);
        Assert.state(sum == 17);
    }
}

@Setter
@Getter
class ThreadInfo{

    public ThreadInfo() {
    }

    public ThreadInfo(Thread thread) {
        this.threadId = thread.getId();
        this.threadName = thread.getName();
        this.threadStr = thread.toString();
    }

    public void build(Thread thread){
        this.threadId = thread.getId();
        this.threadName = thread.getName();
        this.threadStr = thread.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(Objects.isNull(obj)) return false;
        boolean result = false;
        ThreadInfo target = (ThreadInfo)obj;
        if((getThreadId() == null && target.getThreadId() != null) || (getThreadId() != null && target.getThreadId() == null )) return false;
        if((getThreadName() == null && target.getThreadName() != null) || (getThreadName() != null && target.getThreadName() == null)) return false;
        if((getThreadStr() == null && target.getThreadStr() != null) || (getThreadStr() != null && target.getThreadStr() == null)) return false;
        if(getThreadId().equals(target.getThreadId()) && getThreadName().equals(target.getThreadName()) && getThreadStr().equals(target.getThreadStr())) result = true;
        return result;
    }

    private Long threadId;
    private String threadName;
    private String threadStr;
}