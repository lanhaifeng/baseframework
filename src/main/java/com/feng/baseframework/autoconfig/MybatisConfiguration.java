package com.feng.baseframework.autoconfig;

import com.feng.annotation.TableInfo;
import com.feng.baseframework.mapper.CommonMapper;
import com.feng.baseframework.util.ClassLoaderUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 *
 * @ProjectName:    baseframework
 * @Description:    mybatis配置类
 * @Author:         lanhaifeng
 * @CreateDate:     2018/4/29 23:12
 * @UpdateUser:
 * @UpdateDate:     2018/4/29 23:12
 * @UpdateRemark:
 * @Version:        1.0
 */
@Configuration
@MapperScan(basePackages = {"com.feng.baseframework.mapper"})
public class MybatisConfiguration {

    @Configuration
    public static class MyMapperRegister implements ConfigurationCustomizer {

        @Override
        public void customize(org.apache.ibatis.session.Configuration configuration) {
            Collection<Class<?>> clsSet = ClassLoaderUtil.scanClassByPath("com.feng.baseframework.mapper");
            Predicate<Class<?>> filter = CommonMapper.class::isAssignableFrom;
            List<Class<?>> mappers = clsSet.stream().filter(filter).toList();

            mappers.forEach(cls -> {
                System.out.println(cls.getCanonicalName());
                TableInfo tableEntity = cls.getAnnotation(TableInfo.class);
                if(Objects.nonNull(tableEntity)) {
                    for (Method declaredMethod : CommonMapper.class.getDeclaredMethods()) {
                        String id = cls.getCanonicalName() + "." + declaredMethod.getName();
                        StaticSqlSource staticSqlSource = new StaticSqlSource(configuration, String.format("select * from %s", tableEntity.value()));
                        //TODO 调用mapper方法执行失败，需要重新构造，设置其resultMap
                        MappedStatement mappedStatement = new MappedStatement.Builder(configuration, id, staticSqlSource, SqlCommandType.SELECT).build();
                        configuration.addMappedStatement(mappedStatement);
                    }
                }

            });
        }
    }
}
