package com.feng.baseframework.hot;

import java.util.Random;

/**
 * Api测试类
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/5/11 15:49创建:ApiTest
 * @since v2.0
 */
public class ApiTest {
    public String queryGirlfriendCount(String boyfriendName) {
        return boyfriendName + "的前女友数量" + (new Random().nextInt(10) + 1) + "个";
    }

}
