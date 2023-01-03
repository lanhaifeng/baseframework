package com.feng.baseframework.test;

import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: baseframework
 * @Description: restTemplate单元测试类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/1 23:47
 * @UpdateUser:
 * @UpdateDate: 2018/5/1 23:47
 * @UpdateRemark:
 * @Version: 1.0
 */
@ActiveProfiles("pro,dev1")
public class RestTemplateTest extends BaseFrameworkApplicationTest {

    @Resource(name = "restTemplate")
    private RestTemplate simpleRestTemplate;
	@Resource(name = "sshIgnoreVerificationRestTemplate")
	private RestTemplate ignoreVerificationrestTemplate;
	@Resource(name = "sshIgnoreVerificationRestTemplate2")
	private RestTemplate ignoreVerificationrestTemplate2;
    @Resource(name = "sshRestTemplate")
    private RestTemplate sshRestTemplate;

    //普通http
    @Test
    public void testRestTemplateGet1(){
		String html = simpleRestTemplate.getForObject("https://127.0.0.1:1443/anonymous/redirectMethod/",String.class);
		Assert.state(StringUtils.isNotBlank(html), "无返回结果");
    }

	//https单向忽略证书验证,修改属性server.ssl.client-auth=want，服务端不验证客户端
    @Test
    public void testRestTemplateGet2(){
		String html = ignoreVerificationrestTemplate.getForObject("https://127.0.0.1:1443/anonymous/redirectMethod/",String.class);
		Assert.state(StringUtils.isNotBlank(html), "无返回结果");
    }

	//https单向忽略证书验证,修改属性server.ssl.client-auth=want，服务端不验证客户端
    @Test
    public void testRestTemplateGet3(){
		String html = ignoreVerificationrestTemplate2.getForObject("https://127.0.0.1:1443/anonymous/redirectMethod/",String.class);
		Assert.state(StringUtils.isNotBlank(html), "无返回结果");
    }

    //https双向验证,修改属性server.ssl.client-auth=need，服务端验证客户端
    @Test
    public void testRestTemplateHttpsGet() {
        String html = sshRestTemplate.getForObject("https://127.0.0.1:1443/anonymous/redirectMethod/",String.class);
		Assert.state(StringUtils.isNotBlank(html), "无返回结果");
    }
}
