package com.feng.baseframework.jdk.util;

import com.feng.baseframework.common.MockitoBaseTest;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @ProjectName: baseframework
 * @Description: 测试hashset
 * @Author: lanhaifeng
 * @CreateDate: 2019/7/15 22:51
 * @UpdateUser:
 * @UpdateDate: 2019/7/15 22:51
 * @UpdateRemark:
 * @Version: 1.0
 */
public class HashSetTest extends MockitoBaseTest{

    @Test
    public void testHashcode(){
        Set<HashSetNode1> set1 = new HashSet<>();
        HashSetNode1 node1 = new HashSetNode1("test");
        HashSetNode1 node2 = new HashSetNode1("test");
        set1.add(node1);
        set1.add(node2);
        Assert.state(node1.equals(node2), "重写equals方法有误");
        Assert.state(set1.size() == 2, "未重写hashcode方法，hashset数量与期待不一致");

        Set<HashSetNode2> set2 = new HashSet<>();
        HashSetNode2 node3 = new HashSetNode2("test");
        HashSetNode2 node4 = new HashSetNode2("test");
        set2.add(node3);
        set2.add(node4);
        Assert.state(node3.equals(node4), "重写equals方法有误");
        Assert.state(set2.size() == 1, "重写hashcode方法，hashset数量与期待不一致");

        int code1 = node1.hashCode();
        int code2 = node2.hashCode();
        int code3 = node3.hashCode();
        int code4 = node4.hashCode();
        System.out.println(Integer.toHexString(code1));
        System.out.println(Integer.toHexString(code2));
        System.out.println(Integer.toHexString(code3));
        System.out.println(Integer.toHexString(code4));
        System.out.println(node1 == node2);
        System.out.println(node3 == node4);
    }
}

class HashSetNode1{
    private String name;
    private String content;

    public HashSetNode1() {
    }

    public HashSetNode1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HashSetNode2{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(this.getClass() != obj.getClass()) {
            return false;
        }

        HashSetNode1 hashSetNode = (HashSetNode1)obj;
        return hashSetNode.toString().equals(this.toString());
    }
}


class HashSetNode2{
    private String name;
    private String content;

    public HashSetNode2() {
    }

    public HashSetNode2(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HashSetNode2{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if(null == obj) {
            return false;
        }
        if(this.getClass() != obj.getClass()) {
            return false;
        }

        HashSetNode2 hashSetNode = (HashSetNode2)obj;
        return hashSetNode.toString().equals(this.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.toString());
    }
}
