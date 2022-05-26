package com.feng.baseframework.spring;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feng.baseframework.constant.IpType;
import com.feng.baseframework.model.User;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * baseframework
 * 2022/5/3 18:19
 * 测试自定义HandlerMapping的控制器
 *
 * @author lanhaifeng
 * @since
 **/
public class MyController {

    @ResponseBody
    public User about() {
        User user = new User();
        user.setName("about");
        return user;
    }

    @ResponseBody
    public JSONObject dict() {
        JSONObject dict = new JSONObject();

        JSONArray ipTypeJson = new JSONArray();
        for (IpType value : IpType.values()) {
            JSONObject type = new JSONObject();
            type.put(value.name(), value.getValue());
            ipTypeJson.add(type);
        }

        dict.put("IpType", ipTypeJson);
        return dict;
    }
}
