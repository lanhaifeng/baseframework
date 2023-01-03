package com.feng.baseframework.autoconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

/**
 * @ProjectName: baseframework
 * @Description: 资源配置类
 * @Author: lanhaifeng
 * @CreateDate: 2019/7/8 22:29
 * @UpdateUser:
 * @UpdateDate: 2019/7/8 22:29
 * @UpdateRemark:
 * @Version: 1.0
 */
@Configuration
public class MessageSourceConfiguration {

    @Value("${spring.messages.basename}")
    private String baseName;

    //@Bean("messageSource")
    //MessageSourceAutoConfiguration已经对国际化有支持，只需要在application.properties中配置即可
    public MessageSource  messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        //当【ApplicationContext】getMessage方法传入Locale，没有对应国际化资源文件，则会去默认Locale对应的资源文件中查找属性
        //默认Locale对应无后缀的属性配置文件(因为baseName为i18n/messages，故此处默认资源文件为messages.properties)
        Locale.setDefault(Locale.US);
        messageSource.setBasenames(baseName);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
