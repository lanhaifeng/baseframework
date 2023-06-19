package com.feng.baseframework.filter;

import com.feng.baseframework.constant.ResultEnum;
import com.feng.baseframework.exception.BusinessException;
import com.feng.baseframework.util.IPUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * baseframework
 * 2020/8/24 16:44
 * referer安全验证，防盗链
 *
 * @author lanhaifeng
 * @since
 **/
@WebFilter(urlPatterns = { "/*" }, filterName = "refererFilter")
public class RefererFilter implements Filter, InitializingBean {

	private List<String> refererWhiteListUrl;
	private static final String http = "http://";
	private static final String httpPort = "80";
	private static final String https = "https://";
	private static final String httpsPort = "443";

	@Override
	public void afterPropertiesSet() throws Exception {
		refererWhiteListUrl = new ArrayList<>();
		refererWhiteListUrl.add("/login");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		if (!checkReferer(IPUtil.getLocalIp(), String.valueOf(request.getServerPort()),
				request.getRequestURI(), request.getHeader("Referer"))) {
			throw new BusinessException(ResultEnum.REFERER_ERROR);
		}else {
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}

	private boolean checkReferer(String host, String port, String url, String referer){
		boolean validateResult = true;
		if(!refererWhiteListUrl.contains(url)){
			if(StringUtils.isBlank(referer)){
				validateResult = true;
			}else {
				if(httpPort.equals(port) && referer.startsWith(http)
						|| httpsPort.equals(port) && referer.startsWith(https)){
					port = "/";
				}else {
					port = ":" + port + "/";
				}
				referer = referer.replace(http, "").replace(https, "");
				if(!referer.startsWith(host)
						&& !referer.startsWith("127.0.0.1" + port)
						&& !referer.startsWith("localhost" + port)){
					validateResult = false;
				}
			}
		}
		return validateResult;
	}

	@Override
	public void destroy() {

	}


}
