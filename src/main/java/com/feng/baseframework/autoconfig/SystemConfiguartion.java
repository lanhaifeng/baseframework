package com.feng.baseframework.autoconfig;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.interceptor.MyMethodInterceptor;
import com.feng.baseframework.listener.ApplicationEventListener;
import com.feng.baseframework.util.SpringUtil;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @ProjectName:    baseframework
 * @Description:    系统配置类
 * @Author:         lanhaifeng
 * @CreateDate:     2018/4/29 23:12
 * @UpdateUser:
 * @UpdateDate:     2018/4/29 23:12
 * @UpdateRemark:
 * @Version:        1.0
 */
@Configuration
@Import({MybatisConfiguration.class, RedisConfiguration.class, RestTemplateConfiguration.class, WebSecurityConfig.class, WebServletConfig.class,
        DroolsConfig.class, DatasourceConfiguration.class, MessageSourceConfiguration.class, KafkaConfiguration.class})
//EnableAspectJAutoProxy注解有两个属性proxyTargetClass、exposeProxy，
// 其中proxyTargetClass为true表示强制使用cglib动态代理，当一个类至少实现了一个接口，默认使用jdk动态代理
// 其中exposeProxy为true，并在调用处使用类似((SecurityController)AopContext.currentProxy()).test()的方式，可以支持自调用的切面增强
@EnableAspectJAutoProxy(exposeProxy = true)
// 启动注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@EnableTransactionManagement
@ComponentScan("com.feng.baseframework")
@EnableConfigurationProperties({QuartzProperties.class})
public class SystemConfiguartion {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    @Bean
    public ApplicationEventListener applicationEventListener(){
        return new ApplicationEventListener();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.https", name = "redirectPort", havingValue = "true")
    public Connector connector(){
        Connector connector=new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8088);
        connector.setSecure(false);
        connector.setRedirectPort(1443);
        return connector;
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.https", name = "redirectPort", havingValue = "true")
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(Connector connector){
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint=new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection=new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(connector);
        return tomcat;
    }

    /*@Bean
    @ConditionalOnProperty(prefix = "spring.https", name = "redirectPort", havingValue = "true")
    public EmbeddedServletContainerFactory servletContainerFactory(){
        TomcatEmbeddedServletContainerFactory containerFactory = new TomcatEmbeddedServletContainerFactory(){

            @Override
            protected void postProcessContext(Context context) {
                super.postProcessContext(context);
                SecurityConstraint constraint = new SecurityConstraint();
                constraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }
        };
        containerFactory.addAdditionalTomcatConnectors(connector());
        return containerFactory;
    }*/

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor(){
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(ClassLevelAdviceTag.class, true);

		/*注解切点
		//等价于AnnotationMatchingPointcut(ClassLevelAdviceTag.class, false)
        pointcut = new AnnotationMatchingPointcut(ClassLevelAdviceTag.class);

        //第二参数默认为false，为true表示会检测父类和父接口的注解,为false不检测父类和父接口的注解
        pointcut = new AnnotationMatchingPointcut(ClassLevelAdviceTag.class, true);
        //第一个参数是类注解，第二个参数是方法级别注解，当只传类注解时，此时方法匹配器为MethodMatcher.TRUE，默认所有的方法都拦截
        //当类注解为null，而方法级别注解不为null时，只拦截有方法级别注解的方法
        //当类注解和方法级别注解都不为null时，只拦截类上有注解并且方法上也有注解的方法
        pointcut = new AnnotationMatchingPointcut(null, MethodLevelAdviceTag.class);
        pointcut = new AnnotationMatchingPointcut(ClassLevelAdviceTag.class, MethodLevelAdviceTag.class);
        */

        /*正则切点
        JdkRegexpMethodPointcut pointcut5 = new JdkRegexpMethodPointcut();
        pointcut2.setPatterns("com.feng.baseframework.*");*/

        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new MyMethodInterceptor());
        return advisor;
    }

}
