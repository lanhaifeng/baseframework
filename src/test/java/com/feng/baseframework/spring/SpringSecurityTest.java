package com.feng.baseframework.spring;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.controller.SecurityController;
import io.jsonwebtoken.lang.Assert;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Jsr250MethodSecurityMetadataSource;
import org.springframework.security.access.annotation.SecuredAnnotationSecurityMetadataSource;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.ExpressionBasedAnnotationAttributeFactory;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PrePostAnnotationSecurityMetadataSource;
import org.springframework.security.util.SimpleMethodInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.spy;

/**
 * baseframework
 * 2020/9/18 15:46
 * spring security test
 *
 * @author lanhaifeng
 * @since
 **/
public class SpringSecurityTest extends MockitoBaseTest {


	private MethodSecurityMetadataSource methodSecurityMetadataSource;
	private MethodInvocation targetMethod;
	private Class<?> targetCls;
	private Object targetObject;

	private List<String> resultRoles;
	private boolean result = true;

	@Before
	public void setUp() throws Exception {
		targetCls = SecurityController.class;
		targetObject = new SecurityController();
		resultRoles = spy(new ArrayList<>());
		resultRoles.add("ROLE_ADMIN");
		resultRoles.add("ROLE_TEST");
	}

	@Test
	public void securedAnnotationTest() {
		methodSecurityMetadataSource = new SecuredAnnotationSecurityMetadataSource();
		targetMethod = new SimpleMethodInvocation(targetObject,
				MethodUtils.getAccessibleMethod(targetCls, "testSession4", HttpServletRequest.class));

		Collection<ConfigAttribute> configAttributes = methodSecurityMetadataSource.getAttributes(targetMethod);
		for (ConfigAttribute configAttribute : configAttributes) {
			logger.info(configAttribute.getAttribute());
			if(!resultRoles.contains(configAttribute.getAttribute())){
				result = false;
				break;
			}
		}

		Assert.state(result, "@Secured读取的角色非期待结果");
		Mockito.verify(resultRoles, atLeastOnce()).contains(any());
	}

	@Test
	public void rolesAllowedTest() {
		methodSecurityMetadataSource = new Jsr250MethodSecurityMetadataSource();
		targetMethod = new SimpleMethodInvocation(targetObject,
				MethodUtils.getAccessibleMethod(targetCls, "testSession3", HttpServletRequest.class));

		Collection<ConfigAttribute> configAttributes = methodSecurityMetadataSource.getAttributes(targetMethod);
		for (ConfigAttribute configAttribute : configAttributes) {
			logger.info(configAttribute.getAttribute());
			if(!resultRoles.contains(configAttribute.getAttribute())){
				result = false;
				break;
			}
		}

		Assert.state(result, "@RolesAllowed读取的角色非期待结果");
	}

	@Test
	public void preAuthorizeTest() {
		DefaultMethodSecurityExpressionHandler defaultMethodExpressionHandler = new DefaultMethodSecurityExpressionHandler();
		ExpressionBasedAnnotationAttributeFactory attributeFactory = new ExpressionBasedAnnotationAttributeFactory(
				defaultMethodExpressionHandler);
		methodSecurityMetadataSource = new PrePostAnnotationSecurityMetadataSource(attributeFactory);
		targetMethod = new SimpleMethodInvocation(targetObject,
				MethodUtils.getAccessibleMethod(targetCls, "testSession2", HttpServletRequest.class));

		Collection<ConfigAttribute> configAttributes = methodSecurityMetadataSource.getAttributes(targetMethod);
		for (ConfigAttribute configAttribute : configAttributes) {
			logger.info(configAttribute.getAttribute());
			if(!resultRoles.contains(configAttribute.getAttribute())){
				result = false;
				break;
			}
		}

		Assert.state(result, "@PreAuthorize读取的角色非期待结果");
	}

}
