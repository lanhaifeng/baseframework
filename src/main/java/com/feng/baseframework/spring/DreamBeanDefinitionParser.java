package com.feng.baseframework.spring;

import com.feng.baseframework.model.Version;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * baseframework
 * 2022/8/7 22:36
 * 自定义bean解析器，解析xml
 *
 * @author lanhaifeng
 * @since
 **/
public class DreamBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        //beanDefinition
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(Version.class);
        beanDefinition.setLazyInit(false);
        //解析name
        String name = element.getAttribute("name");
        beanDefinition.getPropertyValues().add("name",
                name);
        //解析version
        beanDefinition.getPropertyValues().add("version",
                element.getAttribute("version"));
        //commit
        beanDefinition.getPropertyValues().add("commit",
                element.getAttribute("commit"));
        //buildTime
        beanDefinition.getPropertyValues().add("buildTime",
                element.getAttribute("buildTime"));
        //branch
        beanDefinition.getPropertyValues().add("branch",
                element.getAttribute("branch"));
        //buildTime
        beanDefinition.getPropertyValues().add("buildVersion",
                element.getAttribute("buildVersion"));

        parserContext.getRegistry().registerBeanDefinition(name, beanDefinition);
        return beanDefinition;
    }
}
