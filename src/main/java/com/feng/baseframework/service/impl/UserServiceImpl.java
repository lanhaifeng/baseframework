package com.feng.baseframework.service.impl;

import com.feng.baseframework.mapper.UserMapper;
import com.feng.baseframework.model.User;
import com.feng.baseframework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: 用户业务实现类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:44
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:44
 * @UpdateRemark:
 * @Version: 1.0
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User addUser(User user) {
        try {
            userMapper.addUser(user);
            return getUserById(user.getId());
        } catch (Exception e) {
            log.error("新增失败错误：" + ExceptionUtils.getFullStackTrace(e));
            //通常情况下，主动回滚事务，可以手动抛异常即可，不抛异常可以如下方式回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUsers(List<Integer> ids) {

    }

    @Override
    public void deleteUser(Integer id) {
        logger.info("deleteUser");
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public User getUserByName(String userName) {
        return null;
    }
}
