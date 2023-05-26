package com.feng.baseframework.controller;

import com.feng.baseframework.model.JsonpProxy;
import com.feng.baseframework.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * baseframework
 * 2018/9/13 9:24
 * 基础控制器，接口测试用
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@RestController
@Validated
public class BaseController extends ClassFilterController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WebApplicationContext webApplicationContext;

    /**
     * 描述: 返回根目录<br/>
     * 时间:16:00 2022/4/2 <br/>
     *
     * @author lanhaifeng
     * @return java.lang.String 返回根目录绝对路径
     */
	@RequestMapping(value = "/baseManage/getWebRootPath",method=RequestMethod.GET)
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
	public String getWebRootPath() {
    	return System.getProperty("projectRootPath") == null ? "" : System.getProperty("projectRootPath");
	}

    /**
     * 描述: 测试参数验证<br/>
     * @description BindingResult只能用于@RequestPart @RequestBody，并和@Validated成对出现 <br/>
     *
     * 时间:16:01 2022/4/2 <br/>
     *
     * @param user     用户信息
     * @author lanhaifeng
     * @return java.lang.String 返回验证结果
     */
    @RequestMapping(value = "/baseManage/validate1",method=RequestMethod.POST)
    public String validateTest1(@RequestBody @Validated User user, BindingResult result){
        String response = "true";
        if(result.hasErrors()){
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError allError : allErrors) {
                System.out.println(allError.getDefaultMessage());
            }
        }
        return  response;
    }

    @RequestMapping(value = "/baseManage/validate2",method=RequestMethod.POST)
    public String validateTest2(@RequestBody @Valid User user){
        String response = "true";
        return  response;
    }

    @RequestMapping(value = "/baseManage/validate3",method=RequestMethod.GET)
    public String validateTest3(@Valid @NotEmpty String auditId){
        String response = "true";
        return  response;
    }

    @RequestMapping(value = "/baseManage/validate4",method=RequestMethod.GET)
    public String validateTest4(@Validated @NotEmpty String auditId){
        String response = "true";
        System.out.println(auditId);
        return  response;
    }

    @RequestMapping(value = "/baseManage/servletContextTest1",method=RequestMethod.GET)
    public String servletContextTest1(){
        ServletContext servletContext = webApplicationContext.getServletContext();
        String response = servletContext.getInitParameter("ServletContext-test");
        return  response;
    }

    @RequestMapping(value = "/baseManage/servletContextTest2",method=RequestMethod.GET)
    public void servletContextTest2(){
        logger.info(logger.getClass().toGenericString());
        logger.info("test logback log");
        logger.info("test logback log");
    }

    @GetMapping(value = "/baseManage/jsonp1", produces = "application/javascript;charset=UTF-8")
    public JsonpProxy jsonp1(String callback){
        User user = new User();
        user.setName("test");
        JsonpProxy jsonpProxy = new JsonpProxy(user);
        jsonpProxy.setJsonpFunction(callback);

        return jsonpProxy;
    }
    @GetMapping(value = "/baseManage/jsonp2", produces = "application/javascript;charset=UTF-8")
    public User jsonp2(){
        User user = new User();
        user.setName("test");

        return user;
    }

    @GetMapping(value = "/baseManage/newApi")
    public User newApi(){
        com.feng.baseframework.model.User user = new com.feng.baseframework.model.User();
        user.setName("test5");

        return user;
    }
}
