package com.feng.baseframework.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * baseframework
 * 2018/9/13 9:24
 * 基础控制器，接口测试用
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@RestController
public class BaseController2 {
    @GetMapping(value = "/baseManage/newApi2")
    public String newApi2(){

        return "user";
    }
}
