package com.feng.baseframework.autoconfig;

import com.feng.annotation.TableInfo;
import com.feng.baseframework.mapper.CommonMapper;
import com.feng.baseframework.model.OperLog;
import com.feng.baseframework.util.ClassLoaderUtil;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.*;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.*;
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

                        //TODO 构造resultMap
                        List<ResultMapping> resultMappings = new ArrayList<>();
                        resultMappings.add(new ResultMapping.Builder(configuration, "id", "ID", Integer.class).build());
                        resultMappings.add(new ResultMapping.Builder(configuration, "logModule", "log_module", String.class).build());
                        resultMappings.add(new ResultMapping.Builder(configuration, "userId", "user_id", Integer.class).build());
                        resultMappings.add(new ResultMapping.Builder(configuration, "logAction", "log_action", String.class).build());
                        resultMappings.add(new ResultMapping.Builder(configuration, "logResult", "log_result", String.class).build());
                        resultMappings.add(new ResultMapping.Builder(configuration, "logTime", "log_time", Date.class).build());
                        String resultMapId = "myResultMapping." + declaredMethod.getReturnType().getSimpleName();
                        ResultMap resultMap = (new ResultMap.Builder(configuration, resultMapId, OperLog.class,
                                resultMappings)).build();
                        List<ResultMap> resultMaps = new ArrayList<>();
                        resultMaps.add(resultMap);
                        configuration.addResultMap(resultMap);

                        /*
                         *  构造MappedStatement，参考org.apache.ibatis.builder.MapperBuilderAssistant#addMappedStatement
                         *  String resource = cls.getName().replace(".", "/") + ".java (best guess)";
                         *  MapperBuilderAssistant builderAssistant = new MapperBuilderAssistant(configuration, resource);
                         *  builderAssistant.addMappedStatement(id.replace(".", "/"), staticSqlSource, StatementType.PREPARED,
                         *      SqlCommandType.SELECT, (Integer)null, (Integer)null, (String)null, null,
                         *      resultMapId, declaredMethod.getReturnType(), (ResultSetType)null, false, true,
                         *      false, NoKeyGenerator.INSTANCE, null, null,
                         *      configuration.getDatabaseId(), configuration.getDefaultScriptingLanguageInstance(), (String)null);
                         */
                        String resource = cls.getName().replace(".", "/") + ".java (best guess)";
                        MappedStatement.Builder statementBuilder = (new MappedStatement.Builder(
                                configuration, id, staticSqlSource, SqlCommandType.SELECT)).resource(resource)
                                .fetchSize(null).timeout(null).statementType(StatementType.PREPARED)
                                .keyGenerator(NoKeyGenerator.INSTANCE).keyProperty(null).keyColumn(null)
                                .databaseId(configuration.getDatabaseId()).lang(configuration.getDefaultScriptingLanguageInstance())
                                .resultOrdered(false).resultSets(null).resultMaps(resultMaps)
                                .resultSetType(null).flushCacheRequired(false).useCache(false).cache(null).dirtySelect(false);

                        //TODO 这里是构造参数，这里测试的无参方法，所以可以注释掉
                        /*ParameterMap statementParameterMap = configuration.getParameterMap(parameterMapName);
                        if (statementParameterMap != null) {
                            statementBuilder.parameterMap(statementParameterMap);
                        }*/

                        MappedStatement statement = statementBuilder.build();
                        configuration.addMappedStatement(statement);
                    }
                }

            });
        }
    }
}
