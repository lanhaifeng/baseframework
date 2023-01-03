package com.feng.baseframework.mapper;

import com.feng.baseframework.model.Menu;
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
public interface MenuMapper {

    @Select("SELECT id, url, seq, parent_id, `name`, icon, `order`, is_leaf, mark FROM base_menu WHERE id = #{id}")
    @Results(id="menuMap" , value = {
            @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "url", property = "url", javaType = String.class),
            @Result(column = "seq", property = "seq", javaType = Integer.class),
            @Result(column = "parent_id", property = "parent_id", javaType = Integer.class),
            @Result(column = "name", property = "name", javaType = String.class),
            @Result(column = "icon", property = "icon", javaType = String.class),
            @Result(column = "order", property = "order", javaType = Integer.class),
            @Result(column = "is_leaf", property = "is_leaf", javaType = Integer.class),
            @Result(column = "mark", property = "mark", javaType = String.class) })
    Menu getMenuById(Integer id);

    @Select("SELECT tab1.id, url, seq, parent_id, `name`, icon, `order`, is_leaf, tab1.mark, role_name FROM base_menu tab1 " +
            "inner join base_role_menu tab2 on tab1.id = tab2.menu_id " +
            "inner join base_role tab3 on tab2.role_id = tab3.id")
    @Results(id="menuWithRoleMap",value = {
            @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "url", property = "url", javaType = String.class),
            @Result(column = "seq", property = "seq", javaType = Integer.class),
            @Result(column = "parent_id", property = "parent_id", javaType = Integer.class),
            @Result(column = "name", property = "name", javaType = String.class),
            @Result(column = "icon", property = "icon", javaType = String.class),
            @Result(column = "order", property = "order", javaType = Integer.class),
            @Result(column = "is_leaf", property = "is_leaf", javaType = Integer.class),
            @Result(column = "role_name", property = "roleName", javaType = String.class),
            @Result(column = "mark", property = "mark", javaType = String.class) })
    List<Menu> getAllMenusWithRole();

    @Select("SELECT id, url, seq, parent_id, `name`, icon, `order`, is_leaf, mark FROM base_menu WHERE id in (" +
            "select menu_id from base_role_menu where role_id in(" +
            "select role_id from base_user_role where user_id = #{userId})" +
            ")")
    @ResultMap(value = "menuMap")
    List<Menu> getMenuByUserId(Integer userId);

    @Select("SELECT id, url, seq, parent_id, `name`, icon, `order`, is_leaf, mark FROM base_menu WHERE id in (" +
            "select menu_id from base_role_menu where role_id = #{roleId}" +
            ")")
    @ResultMap(value = "menuMap")
    List<Menu> getMenuByRoleId(Integer roleId);

    @Select("SELECT id, url, seq, parent_id, `name`, icon, `order`, is_leaf, mark FROM base_menu")
    @ResultMap(value = "menuMap")
    List<Menu> findMenus(Map param);

    @Select("SELECT count(0) FROM base_menu")
    Integer countMenus(Map param);
}
