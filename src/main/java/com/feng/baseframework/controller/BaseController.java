package com.feng.baseframework.controller;

import com.feng.baseframework.model.User;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.util.List;

/**
 * baseframework
 * 2018/9/13 9:24
 * 基础控制器，接口测试用
 *
 * @author lanhaifeng
 * @since
 **/
@RestController
@Validated
public class BaseController extends ClassFilterController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private WebApplicationContext webApplicationContext;

	@RequestMapping(value = "/baseManage/getWebRootPath",method=RequestMethod.GET)
    @PreAuthorize(value = "hasAuthority('ROLE_ADMIN')")
	public String getWebRootPath() {
    	return System.getProperty("projectRootPath") == null ? "" : System.getProperty("projectRootPath");
	}

    //BindingResult只能用于@RequestPart @RequestBody，并和@Validated成对出现
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
}
