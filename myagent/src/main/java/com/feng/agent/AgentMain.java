package com.feng.agent;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.util.Objects;

/**
 * agent的入口类
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/5/16 14:53创建:AgentMain
 * @since v2.0
 */
public class AgentMain {

    /**
     * 静态修改class
     *
     * @param targetClass           启动参数
     * @param instrumentation       修改class类
     * @author lanhaifeng
     * @since 2.0
     */
    public static void premain(String targetClass, Instrumentation instrumentation) {
        System.out.println("-------------agent加载开始-----------------");
        instrumentation.addTransformer(new AgentTransformer(targetClass), true);
        System.out.println("-------------agent加载完毕-----------------");
    }

    /**
     * 动态修改class
     *
     * @param targetClass           启动参数
     * @param instrumentation       修改class类
     * @author lanhaifeng
     * @since 2.0
     */
    public static void agentmain(String targetClass, Instrumentation instrumentation) {
        System.out.println("-------------agent加载开始-----------------");
        System.out.println("agent传入的参数：" + targetClass);

        instrumentation.addTransformer(new AgentTransformer(targetClass), true);

        Class<?> cls = null;
        //获取jvm中已经加载的class
        for (Class<?> allLoadedClass : instrumentation.getAllLoadedClasses()) {
            if(targetClass.equals(allLoadedClass.getName())) {
                cls = allLoadedClass;
                System.out.println(allLoadedClass.getName());
            }
        }

        try {
            if(Objects.isNull(cls)) {
                //加载agent中新增的class
                cls = Class.forName(targetClass);
            }

            /**
             * 通过这种方法实现热更新需满足条件限制。如存在以下情况其中之一，则热更新会失败：
             *
             * 修改了方法签名：包括增减方法，或者是修改方法的参数列表，也就是说修改只能来自于方法内部。
             * 修改了类中字段：包括增减类中字段，或者修改字段类型。
             * 1.instrumentation.retransformClasses(cls)
             * 2.
             * InputStream stream = cls.getClassLoader().getResourceAsStream(targetClass.replace(".", "/") + ".class");
             *             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             *             int b = 0;
             *             while ((b = stream.read()) != -1) {
             *                 byteArrayOutputStream.write(b);
             *             }
             *             instrumentation.redefineClasses(new ClassDefinition(cls, byteArrayOutputStream.toByteArray()));
             */
            instrumentation.retransformClasses(cls);
        } catch (UnmodifiableClassException | ClassNotFoundException e) {
            System.out.println(CommonUtil.getStackTrace(e));
        }
        System.out.println("-------------agent加载完毕-----------------");
    }

}
