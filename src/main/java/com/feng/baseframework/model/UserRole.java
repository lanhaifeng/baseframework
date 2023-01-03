package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: 用户角色实体类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:23
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:23
 * @UpdateRemark:
 * @Version: 1.0
 */
@Accessors(chain = true)// set方法返回当前实例，形成链式调用
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")// 生成一个标识了NotNull的变量私有构造函数和一个静态的of方法调用私有的构造方法
@AllArgsConstructor// 生成所有参数的公共构造方法
public class UserRole implements Serializable {

    private static final long serialVersionUID = -6754737052139726895L;
    private Long id;

    @NotNull
    private String roleId;
    private Long userId;

}
