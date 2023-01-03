package com.feng.baseframework.constant;

import com.feng.baseframework.model.EntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * baseframework
 * 2020/2/18 10:11
 * ldap属性配置
 *
 * @author lanhaifeng
 * @since
 **/
@Component
@Getter
@Setter
public class LdapPropertyConfig extends EntityBase implements Cloneable {

    @Value("${spring.ldap.url}")
    @NotEmpty
    private String ldapUrl;
    @Value("${spring.ldap.base.dc}")
    private String baseDc;
    @Value("${spring.ldap.user.name}")
    @NotEmpty
    private String ldapUserName;
    @Value("${spring.ldap.password}")
    @NotEmpty
    private String ldapPassword;
    @Value("${spring.ldap.user.pricipal.name}")
    @NotEmpty
    private String userPricipalName;
    @Value("${spring.ldap.real.user.pricipal.name}")
    @NotEmpty
    private String realUserPricipalName;

    @Override
    public String preErrorDesc() {
        return "ldap配置验证失败：";
    }

    @Override
    public List<String> customValidate(Class groupCls) {
        return null;
    }

    @Override
    public LdapPropertyConfig clone() throws CloneNotSupportedException {
        return (LdapPropertyConfig)super.clone();
    }

}
