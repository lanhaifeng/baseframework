package com.feng.baseframework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ProjectName: svc-search-biz
 * @description: 解析properties工具类
 * @author: lanhaifeng
 * @create: 2018-05-14 11:32
 * @UpdateUser:
 * @UpdateDate: 2018/5/14 11:32
 * @UpdateRemark:
 **/
public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        if(logger.isDebugEnabled()) {
            logger.debug("开始加载system.properties文件内容.......");
        }
        props = new Properties();

        loadPropertiesFile(props, "system.properties");
        if(logger.isDebugEnabled()) {
            logger.debug("加载system.properties文件内容完成...........");
            logger.debug("system.properties文件内容：" + props);
        }
    }

    private static void loadPropertiesFile(Properties props, String propertiesFile) {
        InputStream in = null;
        if(props == null){
            props = new Properties();
        }
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(propertiesFile);
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error(propertiesFile + "文件未找到");
        } catch (IOException e) {
            logger.error("出现IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error(propertiesFile + "文件流关闭出现异常");
            }
        }
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

    public static String getPropertyFromFile(String propertiesFile, String key){
        if(logger.isDebugEnabled()) {
            logger.debug("开始加载"+ propertiesFile +"文件内容.......");
        }
        Properties properties = new Properties();

        loadPropertiesFile(properties, propertiesFile);
        if(logger.isDebugEnabled()) {
            logger.debug("加载"+propertiesFile+"文件内容完成...........");
            logger.debug(propertiesFile+"文件内容：" + props);
        }

        return properties.getProperty(key);
    }
}
