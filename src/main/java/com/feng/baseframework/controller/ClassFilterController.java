package com.feng.baseframework.controller;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.util.JacksonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * baseframework
 * 2019/10/18 13:48
 * 测试注解扫描
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@ClassLevelAdviceTag
public class ClassFilterController {

	@RequestMapping(value = "/baseManage/getTestInfo",method= RequestMethod.GET)
	public String baseMethod(){
		Map<String,String> data = new HashMap<>();
		data.put("name","tom");
		data.put("age","12");
		String str = JacksonUtil.mapToJson(data);

		return str;
	}
}
