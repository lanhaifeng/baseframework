package com.feng.baseframework.controller;

import com.feng.baseframework.spring.SpElHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/**
 * 类名:SpElController <br/>
 * 描述:测试spring EL表达式 <br/>
 * 时间:2022/3/14 16:25 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
@RestController
public class SpElController {

    private SpElHandler spElHandler;

    @Autowired
    public void setSpElHandler(SpElHandler spElHandler) {
        this.spElHandler = spElHandler;
    }

    @RequestMapping(value = "/baseManage/testSalt",method= RequestMethod.GET)
    public String testSalt(@Validated @NotEmpty String salt) {
       String template = "#{'${spring.security.salt}' eq '" + salt + "'}";
       return "salt is " + salt + (spElHandler.getValue(template, Boolean.class) ?
               ",equal application config!" : ",not equal application config!");
    }
}
