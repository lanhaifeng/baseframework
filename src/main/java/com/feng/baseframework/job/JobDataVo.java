package com.feng.baseframework.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * job参数信息
 *
 * @author lanhaifeng
 * @version 1.0
 * @apiNote 时间:2022/11/26 0:36创建:JobDataVo
 * @since 1.0
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobDataVo implements Serializable {

    private static final long serialVersionUID = -9005369891083645122L;
    /**
     * job具体执行bean名字
     */
    private String beanName;
    /**
     * job具体执行bean类型
     */
    private String beanCls;
    /**
     * job具体执行方法名
     */
    private String methodName;

    /**
     * 扩展信息
     */
    private Map<String, String> extendParams;

    /**
     * 非空判断
     */
    @JsonIgnore
    private Predicate<String> hasText = StringUtils::isNotBlank;

    @JsonIgnore
    public boolean validate() {
        return (hasText.test(beanName) || Objects.nonNull(beanCls))
                && hasText.test(methodName);
    }
}

