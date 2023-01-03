package com.feng.baseframework.aop;

import com.feng.baseframework.messageConverter.JavaScriptMessageConverter;
import com.feng.baseframework.model.JsonpProxy;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 类名:JsonpAdvice <br/>
 * 描述:jsonp切面处理 <br/>
 * 时间:2022/1/11 18:49 <br/>
 *
 * @author lanhaifeng
 * @version 1.0
 */
@ControllerAdvice
public class JsonpAdvice implements ResponseBodyAdvice<Object>, InitializingBean {

    private List<String> jsonpParams;
    private static final String DEFAULT_JSONP_PRAMS = "callback";

    public void setJsonpParams(List<String> jsonpParams) {
        this.jsonpParams = jsonpParams;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return JavaScriptMessageConverter.class.isAssignableFrom(converterType);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String functionName = jsonpParams.stream().filter(param -> StringUtils.isNotBlank(servletRequest.getParameter(param))).findFirst().map(servletRequest::getParameter).orElse("");

        if(StringUtils.isNotBlank(functionName) && (o instanceof MappingJacksonValue)){
            JsonpProxy jsonpProxy = new JsonpProxy(o);
            jsonpProxy.setJsonpFunction(functionName);
            return jsonpProxy;
        }

        return o;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        jsonpParams = Optional.ofNullable(jsonpParams).orElse(new ArrayList<>());
        jsonpParams.add(DEFAULT_JSONP_PRAMS);
    }
}
