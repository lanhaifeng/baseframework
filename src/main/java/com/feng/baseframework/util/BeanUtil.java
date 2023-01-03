package com.feng.baseframework.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ProjectName: baseframework
 * @Description: bean工具类
 * @Author: lanhaifeng
 * @CreateDate: 2018/10/24 8:14
 * @UpdateUser:
 * @UpdateDate: 2018/10/24 8:14
 * @UpdateRemark:
 * @Version: 1.0
 */
public class BeanUtil {

    private static Logger logger = LoggerFactory.getLogger(BeanUtil.class);

    /**
     * 对象的深度克隆，此处的对象涉及Collection接口和Map接口下对象的深度克隆
     * 利用序列化和反序列化的方式进行深度克隆对象
     *
     * @author lanhaifeng
     * @param object 待克隆的对象
     * @param <T> 待克隆对象的数据类型
     * @date 2018/10/24 8:17
     * @return 已经深度克隆过的对象
     */
    public static <T extends Serializable> T deepCloneObject(T object) {
        T deepClone = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            bais = new ByteArrayInputStream(baos
                    .toByteArray());
            ois = new ObjectInputStream(bais);
            deepClone = (T)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                if(bais != null) {
                    bais.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                if(ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return deepClone;
    }

	/**
	 * 2020/10/30 17:14
	 * 查找所有注解cls的类实例
	 *
	 * @param packages
	 * @param cls
	 * @author lanhaifeng
	 * @return java.util.List<T>
	 */
    public static <T> List<T> getAnnotationInstances(String packages, Class<? extends Annotation> cls) {
        List<T> annotationList = new ArrayList<>();
        //通过注解扫描指定的包
        Reflections reflections = new Reflections(packages);
        //如果该包下面有被EnableFilter注解修饰的类，那么将该类的实例加入到列表中，并最终返回
        Set<Class<?>> annotations = reflections.getTypesAnnotatedWith(cls);
        for (Class annotation : annotations) {
            try {
                annotationList.add((T)annotation.newInstance());
            } catch (Exception e) {
                logger.error("获取实例失败，错误：" + ExceptionUtils.getFullStackTrace(e));
            }
        }

        return annotationList;
    }
}
