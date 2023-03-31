package com.feng.baseframework.spring;

import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.util.YamlParser;
import org.codehaus.groovy.util.ListHashMap;
import org.junit.Test;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 测试yaml解析
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/3/31 10:03创建:YamlPropertySourceLoaderTest
 * @since v2.0.0
 */
public class YamlPropertySourceLoaderTest  extends MockitoBaseTest {

    @Test
    public void yamlLoadSpringTest() throws IOException {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(new ClassPathResource("yaml/yamlDemo.yaml"));
        yamlPropertiesFactoryBean.afterPropertiesSet();
        Properties properties = yamlPropertiesFactoryBean.getObject();
        properties.stringPropertyNames().forEach(key-> System.out.println(key + "=" + properties.getProperty(key)));
    }


    @Test
    public void yamlLoadTest() {
        Yaml yaml=new Yaml();
        Map<String, Object> map = yaml.load(getClass().getClassLoader().getResourceAsStream("yaml/yamlDemo.yaml"));
        Map<String, Object> flattenedMap = YamlParser.getFlattenedMap(map);
        flattenedMap.forEach((key,value)-> System.out.println(key + "=" + value));
    }
}
