package com.feng.baseframework.controller;

import cn.hutool.core.date.DateUtil;
import com.feng.baseframework.model.User;
import com.feng.baseframework.spring.SpElHandler;
import com.feng.baseframework.spring.SpUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotEmpty;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类名:SpElController <br/>
 * 描述:测试spring <br/>
 *  1.测试spring EL表达式 <br/>
 *  2.测试API参数处理 <br/>
 * 时间:2022/3/14 16:25 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
@RestController
public class SpController {

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

    private int type = 1;

    /**
     * 1.方式一：post请求json传参只能传yyyy-MM-dd或yyyy-MM-ddTHH:mm:ss.SSSX，如果传其他格式，连这个方法都进不来就400异常了
     * GET不支持特定时间字符串
     * 即：2023-01-01、2023-01-01T14:00:01、2023-01-01T14:00:01.333、2023-01-01T14:00:01.333+08
     *
     * 2.方式二：特定时间字符串+时间处理注解
     *  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
     *  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
     *
     * 3.方式二：CustomDateEditor
     *
     * 4.方式三：PropertyEditorSupport
     *
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        /**
         * 格式化date方式一：post请求json传参只能传yyyy-MM-dd或yyyy-MM-ddTHH:mm:ss.SSSX，如果传其他格式，连这个方法都进不来就400异常了
         * 即：2023-01-01、2023-01-01T14:00:01、2023-01-01T14:00:01.333、2023-01-01T14:00:01.333+08
         */
        if(type == 1) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CustomDateEditor dateEditor = new CustomDateEditor(df, true);
            binder.registerCustomEditor(Date.class, dateEditor);
        }

        // 格式化date方式二，自定义PropertyEditorSupport，然后利用hutool的格式化，DateUtil.parse支持的格式有很多种,这里支持很多种是可以传入任何格式，他都会给你格式化成yyyy-MM-dd HH:mm:ss
        // 日期没有时分秒的时候格式化出来的是2022-10-11 00:00:00
        // 自定义的这种方式对于json传参方式没有效果，压根连方法都不会进入
        if(type == 2) {
            binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
                @Override
                public void setAsText(String text) {
                    System.out.println("1111");
                    // DateUtil.parse是hutool当中的方法，hutool是一个Java工具包
                    setValue(DateUtil.parse(text));
                }
            });
        }

        // 格式化string：如果是字符串类型，就去除字符串的前后空格
        binder.registerCustomEditor(String.class,
                new StringTrimmerEditor(true));

        //注册自定义校验器
        binder.addValidators(new SpUserValidator());
    }

    @RequestMapping(value = "/baseManage/testDataBind", method=RequestMethod.POST)
    public void testDataBind(@Validated @RequestBody User user){
        System.out.println(user.toBeanString());
    }

    @RequestMapping(value = "/baseManage/testDataBind2", method=RequestMethod.GET)
    public void testDataBind2(@Validated User user){
        System.out.println(user.toBeanString());
    }
}
