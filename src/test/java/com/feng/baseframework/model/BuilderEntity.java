package com.feng.baseframework.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: 用于验证lombok builder注解实体
 * @Author: lanhaifeng
 * @CreateDate: 2019/4/20 10:47
 * @UpdateUser:
 * @UpdateDate: 2019/4/20 10:47
 * @UpdateRemark:
 * @Version: 1.0
 */
@Builder//生成build模式内部类，和全部参数的构造函数与@NoArgsConstructor和无参数构造函数不能同时存在，猜测是生成时对于已有无参数构造函数无法处理
@Getter
@Setter
@Accessors(chain = true)
public class BuilderEntity implements Serializable {

    private static final long serialVersionUID = 2161750160705290001L;

    private String name;
    private int age;

}
