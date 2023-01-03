package com.feng.baseframework.service;

import com.feng.baseframework.annotation.ClassLevelAdviceTag;
import com.feng.baseframework.annotation.CustomOnProfileCondition;
import com.feng.baseframework.model.User;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: 用户业务处理类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:39
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:39
 * @UpdateRemark:
 * @Version: 1.0
 */
@CustomOnProfileCondition
@ClassLevelAdviceTag
public interface UserService {
    public User addUser(User user);

    public User updateUser(User user);

    public void deleteUsers(List<Integer> ids);

    public void deleteUser(Integer id);

    public User getUserById(Integer id);

    @CustomOnProfileCondition
    @NotEmpty
    public User getUserByName(String userName);
}
