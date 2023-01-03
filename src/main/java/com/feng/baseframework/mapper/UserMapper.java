package com.feng.baseframework.mapper;

import com.feng.baseframework.SQLDriver.SimpleLangDriver;
import com.feng.baseframework.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

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
public interface UserMapper {

    @Insert("insert into base_user(id,name,user_name,password) values(#{id},#{name},#{userName},#{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addUser(User user);

    @Update("update base_user set name=#{name},password=#{password} where id=#{id}")
    public void updateUser(User user);

    @Delete("delete from base_user where id in(#{ids})")
    @Lang(SimpleLangDriver.class)
    public void deleteUsers(List<Integer> ids);

    @Delete("delete from base_user where id in(#{id})")
    public void deleteUser(Integer id);

    @Select("SELECT id, name, user_name, password FROM base_user WHERE id = #{id}")
    @Results(id="userMap" , value = { @Result(column = "id", property = "id", javaType = Integer.class),
            @Result(column = "name", property = "name", javaType = String.class),
            @Result(column = "user_name", property = "userName", javaType = String.class),
            @Result(column = "password", property = "password", javaType = String.class) })
    public User getUserById(Integer id);

    @Select("SELECT id, name, user_name, password FROM base_user WHERE user_name = #{userName}")
    @ResultMap(value = "userMap")
    public User getUserByName(String userName);
}
