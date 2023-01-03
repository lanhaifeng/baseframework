package com.feng.baseframework.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserTest {

    @Test
    public void nestedClass() {
        User user = new User.Builder().withUserName("tom").withName("张三").build();
        Assert.assertEquals("User{id=, userName='tom', name='张三', password=''}", user.toBeanString());
    }

    @Test
    public void testSort(){
        List<User> list = new ArrayList<>();
        User user = new User.Builder().withId(1).withUserName("tom").withName("张三").build();
        list.add(user);
        user = new User.Builder().withId(123).withUserName("tom").withName("张三").build();
        list.add(user);
        user = new User.Builder().withId(23).withUserName("tom").withName("张三").build();
        list.add(user);
        user = new User.Builder().withUserName("tom").withName("张三").build();
        list.add(user);

        Collections.sort(list);
        for (User user1 : list) {
            System.out.println(user1.getId());
        }
    }
}