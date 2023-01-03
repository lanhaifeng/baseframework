package com.feng.baseframework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }
}
