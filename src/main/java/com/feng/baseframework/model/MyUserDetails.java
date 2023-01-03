package com.feng.baseframework.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @ProjectName: baseframework
 * @Description: 用户认证实体类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:27
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:27
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MyUserDetails extends User implements UserDetails {

    private List<UserRole> roles;

    public MyUserDetails(User user, List<UserRole> roles) {
        super(user);
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null || roles.size() <1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        StringBuilder commaBuilder = new StringBuilder();
        for(UserRole role : roles){
            commaBuilder.append(role.getRoleId()).append(",");
        }
        String authorities = commaBuilder.substring(0,commaBuilder.length()-1);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
