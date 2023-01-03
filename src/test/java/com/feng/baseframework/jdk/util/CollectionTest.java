package com.feng.baseframework.jdk.util;

import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @ProjectName: baseframework
 * @Description: Collection的测试类
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/24 23:18
 * @UpdateUser:
 * @UpdateDate: 2018/10/24 23:18
 * @UpdateRemark:
 * @Version: 1.0
 */
public class CollectionTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void testMapKeySet(){
        Map<String,String> map = new HashMap<>();
        Set<String> set = map.keySet();
    }

    @Test
    public void testSetSize(){
        Set<String> set = new HashSet<>();
        set.add("a");
        set.add("b");
        set.add("");
        set.add(null);

        logger.info("set actual size:" + set.size());
        logger.info("set to array:" + set.toArray());
    }

    @Test
    public void testArrayList1(){
        String[] strs = new String[]{"aba", "aba", "db"};
        List<String> list = Arrays.asList(strs);
        boolean result = false;
        try {
            list.add("test");
        }catch (Exception e){
            result = true;
        }

        Assert.state(result, "新增成功");
    }
    @Test
    public void testArrayList2(){
        String[] strs = new String[]{"aba", "aba", "db"};
        List<String> list1 = Arrays.asList(strs);
        List<String> list2 = new ArrayList<>();

        boolean result = false;
        try {
            list2.addAll(list1);
            list2.add("test");
        }catch (Exception e){
            result = true;
        }

        Assert.state(!result, "新增失败");
    }

    @Test
    public void sublist() {
        List<String> list = new ArrayList<>();
        int j=1;
        for (;j<100;j++){
            list.add(j + "");
        }
        System.out.println(list);

        int time = j % 20 == 0 ? j/20 : j/20 + 1;
        for (int i=1;i <= time;i++){
            System.out.println(list.subList((i-1)*20, i*20 < j ? i*20 : j - 1));
        }
    }
}
