package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: 角色实体类
 * @Author: lanhaifeng
 * @CreateDate: 2019/10/7 17:58
 * @UpdateUser:
 * @UpdateDate: 2019/10/7 17:58
 * @UpdateRemark:
 * @Version: 1.0
 */
@Accessors(chain = true)
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 2433338022056774053L;
    private Integer id;
    private String roleName;
    private String mark;
}
