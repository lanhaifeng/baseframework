package com.feng.baseframework.spring;

import com.feng.baseframework.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * spring自定义参数验证器
 *
 * @author lanhaifeng
 * @version v2.0.0
 * @apiNote 时间:2023/1/21 16:55创建:SpUserValidator
 * @since v2.0.0
 */
public class SpUserValidator implements Validator {

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target,@NonNull Errors errors) {
        // 在static rejectIfEmpty(..)方法，ValidationUtils类用于拒绝该userName属性，如果它是null或空字符串
        ValidationUtils.rejectIfEmpty(errors, "userName", "userName不能为空");
        // 校验年龄只能在0-110之间
        User p = (User) target;
        if(StringUtils.equals("test", p.getUserName())) {
            errors.rejectValue("userName", "用户名非法");
        }
    }
}
