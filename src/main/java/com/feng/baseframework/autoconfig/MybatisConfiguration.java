package com.feng.baseframework.autoconfig;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

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
@MapperScan(basePackages = {"com.feng.baseframework.mapper.**"})
public class MybatisConfiguration {
}
