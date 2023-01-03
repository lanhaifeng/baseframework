package com.feng.baseframework.yaml;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import org.ho.yaml.Yaml;
import org.junit.Test;

import java.io.*;

/**
 * @ProjectName: baseframework
 * @Description: yaml测试类
 * @Author: lanhaifeng
 * @CreateDate: 2018/8/17 21:39
 * @UpdateUser:
 * @UpdateDate: 2018/8/17 21:39
 * @UpdateRemark:
 * @Version: 1.0
 */
public class YamlDemoTest{

    @Test
    public void testYamlToBean1() throws YamlException, FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("yaml/yamlDemo.yaml").getPath();
        System.out.println(path);
        YamlReader reader = new YamlReader(new FileReader(path));
        YamlDemo yamlDemo = reader.read(YamlDemo.class);
        System.out.println(yamlDemo);
    }

    @Test
    public void testBeanToYaml1() throws IOException {
        YamlDemo source = new YamlDemo();
        source.setName("demo");
        source.setDesc("this is desc");

        YamlDemo item1 = new YamlDemo();
        item1.setName("item1");
        item1.setDesc("this is item1 desc");

        YamlDemo item2 = new YamlDemo();
        item2.setName("item2");
        item2.setDesc("this is item2 desc");

        source.addItem(item1);
        source.addItem(item2);

        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("").getPath()+"yaml/output.yaml";
        System.out.println(path);
        YamlWriter writer = new YamlWriter(new FileWriter(path));
        writer.write(source);
        writer.close();
    }

    @Test
    public void testYamlToBean2() throws YamlException, FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("yaml/yamlDemo.yaml").getPath();
        System.out.println(path);

        YamlDemo yamlDemo = Yaml.loadType(new File(path), YamlDemo.class);
        System.out.println(yamlDemo);
    }

    @Test
    public void testBeanToYaml2() throws IOException {
        YamlDemo source = new YamlDemo();
        source.setName("demo");
        source.setDesc("this is desc");

        YamlDemo item1 = new YamlDemo();
        item1.setName("item1");
        item1.setDesc("this is item1 desc");

        YamlDemo item2 = new YamlDemo();
        item2.setName("item2");
        item2.setDesc("this is item2 desc");

        source.addItem(item1);
        source.addItem(item2);

        ClassLoader classLoader = getClass().getClassLoader();
        String path = classLoader.getResource("").getPath()+"yaml/output2.yaml";
        System.out.println(path);

        Yaml.dump(source,new File(path));

        String yaml = Yaml.dump(source);
        System.out.println("yaml:"+yaml);
    }
}
