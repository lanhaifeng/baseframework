package com.feng.baseframework.controller;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.annotation.MethodAdvice;
import com.feng.baseframework.annotation.MethodTimeAop;
import com.feng.baseframework.service.RedisService;
import com.feng.baseframework.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * baseframework
 * 2019/7/26 16:54
 * spring security测试
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@RestController
@ClassLevelAdviceTag
public class SecurityController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RedisService redisService;

	@RequestMapping(value = "/baseManage/getInfo",method= RequestMethod.GET)
	@MethodTimeAop
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_TEST')")
	public String baseMethod(){
		logger.info("测试基于内存的简单认证");
		Map<String,String> data = new HashMap<>();
		data.put("name","tom");
		data.put("age","12");
		String str = JacksonUtil.mapToJson(data);
		redisService.set("testAop",str);
		test();
        ((SecurityController)AopContext.currentProxy()).test();
		return str;
	}

	@RequestMapping(value = "/baseManage/testSession1",method= RequestMethod.GET)
	@MethodTimeAop
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@MethodAdvice
	public void testSession1(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	@RequestMapping(value = "/baseManage/testSession2",method= RequestMethod.GET)
	@MethodTimeAop
	@PreAuthorize("authentication.principal.username=='tom'")
	@MethodAdvice
	public void testSession2(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	@RequestMapping(value = "/baseManage/testSession3",method= RequestMethod.GET)
	@MethodTimeAop
	@RolesAllowed({"ADMIN", "TEST"})
	@MethodAdvice
	public void testSession3(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	@RequestMapping(value = "/baseManage/testSession4",method= RequestMethod.GET)
	@MethodTimeAop
	@Secured({"ROLE_ADMIN", "ROLE_TEST"})
	@MethodAdvice
	public void testSession4(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null){
			session.invalidate();
		}
	}

	@RequestMapping(value = {"/anonymous/redirectMethod","/anonymous/redirectMethod/{id}"},method= RequestMethod.GET)
	public String anonymousMethod(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "id", required = false)String id) throws Exception {
		logger.info("测试匿名认证");
		Map<String,String> data = new HashMap<>();
		data.put("name","tom");
		data.put("age","12");
		String str = JacksonUtil.mapToJson(data);
		return str;
	}

    @MethodTimeAop
	public void test(){
		logger.info("测试同类内是否会aop");
	}
}
