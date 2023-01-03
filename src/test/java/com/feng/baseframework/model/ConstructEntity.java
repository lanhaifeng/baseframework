package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: lombok construct
 * @Author: lanhaifeng
 * @CreateDate: 2019/4/20 11:15
 * @UpdateUser:
 * @UpdateDate: 2019/4/20 11:15
 * @UpdateRemark:
 * @Version: 1.0
 */
@NoArgsConstructor//与Builder注解不能同时使用
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class ConstructEntity implements Serializable {

    private static final long serialVersionUID = -7436158332275705062L;

    private String name;
    private int age;
}
