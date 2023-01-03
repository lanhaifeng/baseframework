package com.feng.baseframework.mapper;

import com.feng.baseframework.model.Role;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: baseframework
 * @Description: 用户操作数据接口
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:45
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:45
 * @UpdateRemark:
 * @Version: 1.0
 */
@Component
public interface RoleMapper {

    @Select("SELECT id, role_name, desc, mark FROM base_role WHERE id = #{id}")
    @Results(id="roleMap" , value = {
            @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "role_name", property = "roleName", javaType = String.class),
            @Result(column = "desc", property = "desc", javaType = String.class),
            @Result(column = "mark", property = "mark", javaType = String.class) })
    Role getRoleById(Integer id);

    @Select("SELECT id, role_name, desc, mark FROM base_role WHERE id in (select role_id from base_user_role where user_id = #{userId})")
    @ResultMap(value = "roleMap")
    Role getRoleByUserId(Integer userId);

    @Select("SELECT id, role_name, desc, mark FROM base_role")
    @ResultMap(value = "roleMap")
    List<Role> findRoles(Map param);

    @Select("SELECT count(0) FROM base_role")
    Integer countRoles(Map param);
}
