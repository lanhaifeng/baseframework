package com.feng.baseframework.spring;

import com.feng.baseframework.common.JunitBaseTest;
import com.feng.baseframework.model.User;
import com.feng.baseframework.util.FileUtils;
import com.feng.baseframework.util.SpringUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.*;

/**
 * @ProjectName: baseframework
 * @Description: spring test
 * @Author: lanhaifeng
 * @CreateDate: 2019/7/6 19:04
 * @UpdateUser:
 * @UpdateDate: 2019/7/6 19:04
 * @UpdateRemark:
 * @Version: 1.0
 */
public class SpringTest extends JunitBaseTest {

    //@Autowired(required = false)
    //会报错，提示没有对应BeanDefinition
    private ApplicationContextAware applicationContextAware;

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testIgnoreDependencyInterface(){
        Assert.assertNull( "该接口未忽略自动注入", applicationContextAware);
        boolean result = false;
        try{
        	SpringUtil.getBeanByName("springUtil");
        }catch(NoSuchBeanDefinitionException e){
            result = true;
        }
        Assert.assertTrue("该接口未忽略自动注入，未报期待的NoSuchBeanDefinitionException异常", result);
    }

    @Test
    public void messageResourceTest(){
        //还未初始化资源，设置默认Locale有效果
        Locale.setDefault(Locale.CANADA_FRENCH);
        Object[] params = {"John", new GregorianCalendar().getTime()};
        String str3 = SpringUtil.getApplicationContext().getMessage("test", params, Locale.CANADA_FRENCH);
        Assert.assertTrue("国际化失败，实际：" + str3, "default".equals(str3));

        //已经初始化资源，设置默认Locale无效
        Locale.setDefault(Locale.CHINA);
        str3 = SpringUtil.getApplicationContext().getMessage("test", params, Locale.CANADA_FRENCH);
        Assert.assertTrue( "国际化失败，实际：" + str3, !"测试".equals(str3));
        Assert.assertTrue("国际化失败，实际：" + Locale.getDefault().toString(), "zh_CN".equals(Locale.getDefault().toString()));

        String str1 = SpringUtil.getApplicationContext().getMessage("test", params, Locale.CHINA);
        String str2 = SpringUtil.getApplicationContext().getMessage("test", params, Locale.US);
        Assert.assertTrue("国际化失败，实际：" + str1, "测试".equals(str1) );
        Assert.assertTrue( "国际化失败，实际：" + str2, "test".equals(str2));

    }

    @Test
    public void testResourcePatternResolver() throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<>();
        String sqlLocation = "classpath*:sql-script/**.sql";
        try {
            Resource[] mappers = resourceResolver.getResources(sqlLocation);
            resources.addAll(Arrays.asList(mappers));
        } catch (IOException e) {
            logger.error("解析xml文件失败，错误：", ExceptionUtils.getFullStackTrace(e));
        }

        Iterator<Resource> iterator = resources.iterator();
        Resource resource;
        while (iterator.hasNext()){
            resource = iterator.next();
            System.out.println(resource.getDescription());
            System.out.println(resource.getURI());
            if(!resource.getFilename().endsWith("sql")){
                iterator.remove();
            }
        }
        resources.sort(Comparator.comparing(o -> FileUtils.getFileName(o.getFilename())));

        StringBuffer stringBuffer = new StringBuffer();
        for (Resource resource1 : resources) {
            FileUtils.readToBuffer(stringBuffer, resource1.getInputStream());
            System.out.println(stringBuffer.toString());
            stringBuffer.setLength(0);
        }

        String fileName = FileUtils.getFileName(resources.get(resources.size() - 1).getFilename());
        System.out.println(fileName.substring(0, fileName.lastIndexOf("sql") - 1));
        System.out.println("0.0.1".compareTo("1.0.0"));
        System.out.println("0.0.2".compareTo("0.0.1"));
    }

    @Test
    public void batchDatas() {
        String sql = "insert into base_user(`name`, `user_name`, `password`) values(?,?,?)";
        List<Object[]> users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = "name" + i;
            for (int j = 0; j < 10000; j++) {
                String userName = "userName" + j;
                Object[] obj = {name, userName, "test"};
                users.add(obj);
            }
        }
        for (int i = 0; i < 10000; i++) {
            jdbcTemplate.batchUpdate(sql, users.subList(i*100, i*100 + 100));
        }
    }
}
