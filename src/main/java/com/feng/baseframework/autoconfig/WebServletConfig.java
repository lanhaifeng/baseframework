package com.feng.baseframework.autoconfig;

import com.feng.baseframework.interceptor.SimpleHandlerInterceptor;
import com.feng.baseframework.listener.OnlineUserListener;
import com.feng.baseframework.messageConverter.JavaScriptMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.InitParameterConfiguringServletContextInitializer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * baseframework
 * 2018/9/13 17:27
 * servlet配置
 * 配置Servlet、Filter、Listener、interceptor
 *
 * @author lanhaifeng
 * @since
 **/
@Configuration
@ServletComponentScan("com.feng.baseframework")
public class WebServletConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private WebApplicationContext webApplicationConnect;

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加自定义拦截器和拦截路径，此处对所有请求进行拦截，除了登录界面和登录接口
        registry.addInterceptor(new SimpleHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/user/login");
    }

    //配置消息转换器
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FormHttpMessageConverter());
        converters.add(new JavaScriptMessageConverter());
    }

    //配置监听器用于获取当前项目路径
    @Bean
    public WebAppRootListener webAppRootListener(){
        //设置环境上下文
        //项目根路径
        ServletContext servletContext = webApplicationConnect.getServletContext();
        servletContext.setInitParameter("webAppRootKey","projectRootPath");
        return new WebAppRootListener();
    }

    //配置监听器用户监听session创建
    @Bean
    public OnlineUserListener onlineUserListener(){
        return new OnlineUserListener();
    }

    //配置错误页
    @Bean
    public EmbeddedServletContainerCustomizer webServerFactoryCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
                // 对嵌入式servlet容器的配置
                // factory.setPort(8081);
                /* 注意：new ErrorPage(stat, path);中path必须是页面名称，并且必须“/”开始。
                    底层调用了String.java中如下方法：
                    public boolean startsWith(String prefix) {
                        return startsWith(prefix, 0);
                    }*/
                ErrorPage errorPage400 = new ErrorPage(HttpStatus.BAD_REQUEST,
                        "/error-400");
                ErrorPage errorPage403 = new ErrorPage(HttpStatus.FORBIDDEN,
                        "/error-403");
                ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND,
                        "/error-404");
                ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
                        "/error-500");
                ErrorPage errorPageNull = new ErrorPage(NullPointerException.class,
                        "/baseError");
                configurableEmbeddedServletContainer.addErrorPages(errorPage400, errorPage403, errorPage404,
                        errorPage500, errorPageNull);
                configurableEmbeddedServletContainer.addInitializers(servletContextInitializer());
            }
        };
    }

    //静态资源映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/img/");
    }

    /**
     * 配置context-param
     * 类似web.xml中：
     * <context-param>
     *      <param-name>log4jRefreshInterval</param-name>
     *      <param-value>600000</param-value>
     * </context-param>
     */
    public ServletContextInitializer servletContextInitializer(){
        Map<String, String> contextParams = new HashMap<>();
        contextParams.put("ServletContext-test", "ServletContext-test");
        return new InitParameterConfiguringServletContextInitializer(contextParams);
    }
}
