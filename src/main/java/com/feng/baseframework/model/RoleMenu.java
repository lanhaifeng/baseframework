package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ProjectName: baseframework
 * @Description: 角色菜单表
 * @Author: lanhaifeng
 * @CreateDate: 2019/10/7 18:06
 * @UpdateUser:
 * @UpdateDate: 2019/10/7 18:06
 * @UpdateRemark:
 * @Version: 1.0
 */
@Accessors(chain = true)
@Setter
@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor
public class RoleMenu implements Serializable{

    private static final long serialVersionUID = -6398742678049021215L;
    private Integer id;
    private Integer menuId;
    private Integer roleId;
}
