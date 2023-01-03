# 多数据库支持
## 基于springboot+mybatis
1. 配置DatabaseIdProvider
```java
    import java.util.Properties;
     
    import org.apache.ibatis.mapping.DatabaseIdProvider;
    import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
     
    @Configuration
    public class BeanConfig {
     
    @Bean
    public DatabaseIdProvider getDatabaseIdProvider() {
    DatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
    Properties p = new Properties();
    p.setProperty("DM DBMS", "dm7");
    p.setProperty("MySQL", "mysql");
    databaseIdProvider.setProperties(p);
    return databaseIdProvider;
    }
    }
```

2. xml使用databaseId区分数据库
```xml
<?xml version="1.0" encoding="UTF-8" ?>
    <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     
    <mapper namespace="com.freddy.mfd7.test.UserMapper">
     
    <select id="findUsers" resultType="com.freddy.mfd7.test.UserEntity" databaseId="dm7">
    select * from "demo";
    </select>
     
    <select id="findUsers" resultType="com.freddy.mfd7.test.UserEntity" databaseId="mysql">
    select * from demo;
    </select>
    </mapper>
```
>mybatis在进行mapper注入时，会出现3种情况，该sql的databaseId和当前数据源的databaseId一样，该sql的databaseId和当前数据源的databaseId不一样，或者该sql没有配置databaseId。mybatis装配时，当同一方法被找到多个sql时，会优先使用databaseId相同的sql，如果没有databaseId相同的sql，再使用没有配置databaseId的sql。databaseId不对应的sql是不会使用的。