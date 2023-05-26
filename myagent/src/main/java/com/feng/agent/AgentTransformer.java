package com.feng.agent;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Objects;

/**
 * 修改class文件
 *
 * @author lanhaifeng
 * @version v2.0
 * @apiNote 时间:2023/5/16 14:54创建:AgentTransformer
 * @since v2.0
 */
public class AgentTransformer implements ClassFileTransformer {

    private String targetClssName;

    public AgentTransformer(String targetClssName) {
        this.targetClssName = targetClssName;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        try {
            System.out.println("-------------transform加载开始-----------------");

            if(Objects.isNull(className) || Objects.isNull(this.targetClssName)) {
                return new byte[0];
            }

            className = className.replaceAll("/", ".");
            System.out.println(className);

            if(targetClssName.equals(className)) {
                ClassPool pool = ClassPool.getDefault();
                pool.appendClassPath(new LoaderClassPath(loader));
                CtClass targetClass = pool.get(className);
                CtMethod ctMethod = targetClass.getDeclaredMethod("newApi");
                ctMethod.setBody("{\n" +
                        "        com.feng.baseframework.model.User user = new com.feng.baseframework.model.User();\n" +
                        "        user.setName(\"test6\");\n" +
                        "\n" +
                        "        return user;\n" +
                        "    }");
                byte[] byteCode = targetClass.toBytecode();
                targetClass.detach();
                System.out.println("-------------transform加载结束-----------------");
                return byteCode;
            }
        } catch (NotFoundException | IOException | CannotCompileException e) {
            System.out.println(CommonUtil.getStackTrace(e));
        }
        System.out.println("-------------transform加载结束-----------------");
        return new byte[0];
    }
}
