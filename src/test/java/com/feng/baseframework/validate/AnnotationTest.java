package com.feng.baseframework.validate;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.service.UserService;
import com.feng.baseframework.service.impl.UserServiceImpl;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * baseframework
 * 2021/1/28 15:14
 * 测试接口参数验证
 *
 * @author lanhaifeng
 * @since
 **/
public class AnnotationTest extends MockitoBaseTest {

	/**
	 * 1.`@Inherited` 修饰的注解，继承性只体现在对类的修饰上；如果元注解Inherited修饰的其他注解，修饰了除类之外的其他程序元素，
	 * 		那么继承性将会失效
	 * 2.方法和属性上注解的继承，忠实于方法/属性继承本身，客观反映方法/属性上的注解
	 * 3.注解的继承不能应用在接口上
	 */
	@Test
	public void testMethodAnnotation() {
		Method method = ReflectionUtils.findMethod(UserService.class, "getUserByName", String.class);
		Method implMethod = ReflectionUtils.findMethod(UserServiceImpl.class, "getUserByName", String.class);

		Assert.state(method.getAnnotations().length == 2);
		Assert.state(implMethod.getAnnotations().length == 0);
		/*Assert.state(UserService.class.getAnnotations().length == 1);
		Assert.state(UserServiceImpl.class.getAnnotations().length == 1);*/
		System.out.println(Arrays.toString(UserService.class.getAnnotations()));
		System.out.println(Arrays.toString(UserServiceImpl.class.getAnnotations()));
	}
}
