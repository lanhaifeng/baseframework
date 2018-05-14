package com.feng.baseframework.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ProjectName: baseframework
 * @Description: 设置请求头
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/14 22:10
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 22:10
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SimpleCORSFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
