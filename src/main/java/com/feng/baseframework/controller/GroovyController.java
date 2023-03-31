package com.feng.baseframework.controller;

import com.feng.baseframework.groovy.MonitorBean;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * groovy测试
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/3/31 10:09创建:GroovyController
 * @since v1.0.0
 */
@RestController
@RequestMapping("groovyBeanRegistry")
public class GroovyController {

    @RequestMapping(path = "printBeans", method = RequestMethod.GET)
    public void printBeans(){
        MonitorBean.printBeans();
    }

    @RequestMapping(path = "printBeansGroovyClassLoader", method = RequestMethod.GET)
    public void printBeansGroovyClassLoader() throws IllegalAccessException, InstantiationException {
        GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
        String hello = "package com.feng.baseframework.util\n" + "\n" + "class GroovyHello {\n" + "    String sayHello(String name) {\n"
                + "        print 'GroovyHello call '\n" + "        name\n" + "    }\n" + "}";
        Class aClass = groovyClassLoader.parseClass(hello);
        GroovyObject object = (GroovyObject) aClass.newInstance();
        Object o = object.invokeMethod("sayHello", "zhangsan");
        System.out.println(o.toString());
    }
}
