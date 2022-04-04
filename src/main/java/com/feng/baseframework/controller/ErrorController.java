package com.feng.baseframework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * baseframework
 * 2019/8/16 9:37
 * 错误处理控制器
 *
 * @author lanhaifeng
 * @since 1.0
 **/
@Controller
public class ErrorController {

	@RequestMapping("error-404")
	public String toPage404(){
		return "/error/404";
	}

	@RequestMapping("error-400")
	public String toPage400(){
		return "/error/400";
	}

	@RequestMapping("error-403")
	public String toPage403(){
		return "/error/403";
	}

	@RequestMapping("error-500")
	public String toPage500(){
		return "/error/500";
	}

	@RequestMapping("baseError")
	public String toPage(){
		return "/error/error";
	}
}
