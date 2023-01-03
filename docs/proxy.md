1.设置用于输出cglib动态代理产生的类
```
System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\class");
```

2.设置用于输出jdk动态代理产生的类
```
System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
```

异常处理
1.java.lang.VerifyError: class net.sf.cglib.core.DebuggingClassWriter
```
Exception in thread "main" java.lang.VerifyError: class net.sf.cglib.core.DebuggingClassWriter overrides final method visit.(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V
    at java.lang.ClassLoader.defineClass1(Native Method)
```
包冲突
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>net.minidev</groupId>
            <artifactId>json-smart</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```