package com.feng.baseframework.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.GenericTypeResolver;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * 查询目标类父类上的泛型
     * @param clazz             目标类
     * @param genericIfc        父类
     * @param index             索引
     * @return {@link Class}<{@link ?}>
     */
    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(clazz, genericIfc);
        return null == typeArguments ? null : typeArguments[index];
    }

    /**
     * 下划线命名转驼峰
     * @param str               原始字符串
     * @param ch                如果是下划线转驼峰,那么ch就是_
     * @return {@link String}   返回转换后的字符串
     */
    public static String toCamel(String str,String ch){
        if (!str.contains(ch))
            return str;
        String[] strings = str.split(ch);
        StringBuilder stringBuffer = new StringBuilder();
        for(int i = 0;i < strings.length;i++){
            if (i == 0)
                stringBuffer.append(strings[i].toLowerCase());
            else
                stringBuffer.append(strings[i].substring(0,1).toUpperCase()).append(strings[i].substring(1).toLowerCase());
        }
        return stringBuffer.toString();
    }

    /**
     * 驼峰转下划线
     * @param str               原始字符串
     * @return {@link String}   返回驼峰转下划线后的字符串
     */
    public static String camelToUnderLine(String str){
        Pattern compile = Pattern.compile("[A-Z]");
        Matcher matcher = compile.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()) {
            //将匹配到的大写字符转换成小写，并且在前面添加下划线然后添加到缓冲区。group(0)在没有分配组的时候匹配所有符合的
            matcher.appendReplacement(sb,  "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
