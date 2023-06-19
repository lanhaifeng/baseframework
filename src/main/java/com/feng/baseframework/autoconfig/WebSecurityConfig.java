package com.feng.baseframework.autoconfig;

import com.feng.baseframework.constant.GlobalPropertyConfig;
import com.feng.baseframework.constant.LdapPropertyConfig;
import com.feng.baseframework.constant.SecurityModeEnum;
import com.feng.baseframework.security.MyAuthenticationProvider;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.security.access.AccessDecisionVoter.ACCESS_DENIED;
import static org.springframework.security.access.AccessDecisionVoter.ACCESS_GRANTED;

/**
 * 网络安全配置
 *
 * @author lanhaifeng
 * @date 2023/06/29
 */
@Configuration
@EnableWebSecurity
//prePostEnabled=true启用注解@PreAuthorize
//jsr250Enabled=true启用注解@RolesAllowed
//securedEnabled=true启用注解@Secured
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class WebSecurityConfig {
    @Resource
    private MyAuthenticationProvider myAuthenticationProvider;
    @Resource
    private GlobalPropertyConfig globalPropertyConfig;
    @Resource(name = "myUserDetailsService")
    private UserDetailsService userService;
    @Resource
    private PasswordEncoder myPasswordEncoder;
    @Resource
    private LdapPropertyConfig ldapPropertyConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //自定义认证
        if (SecurityModeEnum.CUSTOM_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            http
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/", "/static/index.html").permitAll()
                        .requestMatchers("/", "/static/index.html").authenticated()
                        .requestMatchers("/anonymous/**").anonymous()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/static/hello.html")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/logout")
                        .permitAll()
                        .invalidateHttpSession(true)
                )
                .authenticationProvider(myAuthenticationProvider);
        }
        //自定义认证
        if (SecurityModeEnum.DEFAULT_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/anonymous/**").anonymous()  //定义那些url匿名认证
                        //.requestMatchers("/baseManage/getInfo").hasAnyRole("ADMIN", "TEST")  //使用自定义资源类后，不支持antMatchers方式配置的权限
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        // 定义当需要用户登录时候，转到的登录页面。
                        .defaultSuccessUrl("/static/hello.html")
                )
                .userDetailsService(getInMemoryUserDetailsManager());
        }
        //无认证
        if (SecurityModeEnum.NO_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            http.csrf(AbstractHttpConfigurer::disable);
        }

        return http.build();
    }

    private UserDetailsService getInMemoryUserDetailsManager(){
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("admin").password("admin").roles("ADMIN").build());
        manager.createUser(users.username("user").password("user").roles("USER").build());
        manager.createUser(users.username("test").password("test").roles("TEST").build());

        return manager;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider bean = new DaoAuthenticationProvider();
        //返回错误信息提示，而不是Bad Credential
        bean.setHideUserNotFoundExceptions(true);
        //覆盖UserDetailsService类
        bean.setUserDetailsService(userService);
        //覆盖默认的密码验证类
        bean.setPasswordEncoder(myPasswordEncoder);
        return bean;
    }

    @Bean
    public LdapContextSource ldapContextSource(){
        LdapContextSource contextSource = new LdapContextSource();
        Map<String, Object> config = new HashMap<>();

        contextSource.setUrl(ldapPropertyConfig.getLdapUrl());
        contextSource.setBase(ldapPropertyConfig.getBaseDc());
        contextSource.setUserDn(ldapPropertyConfig.getLdapUserName());
        contextSource.setPassword(ldapPropertyConfig.getLdapPassword());

        config.put("java.naming.ldap.attributes.binary", "objectGUID");
        config.put(Context.SECURITY_AUTHENTICATION, "simple");

        contextSource.setPooled(true);
        contextSource.setBaseEnvironmentProperties(config);
        contextSource.afterPropertiesSet();

        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate(){
        return new LdapTemplate(ldapContextSource());
    }
}

@Component
class MyAccessAuthenticationManager implements AuthorizationManager<Object> {
    private final AccessDecisionVoter accessDecisionVoter;
    private final SecurityMetadataSource securityMetadataSource;
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public MyAccessAuthenticationManager(SecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
        this.accessDecisionVoter = new RoleVoter();
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, Object object) {
        Collection<ConfigAttribute> attributes = this.securityMetadataSource.getAttributes(object);
        int decision = accessDecisionVoter.vote(authentication.get(), object, attributes);
        switch (decision) {
            case ACCESS_GRANTED -> {
                return new AuthorizationDecision(true);
            }
            case ACCESS_DENIED -> {
                return new AuthorizationDecision(false);
            }
        }
        throw new AccessDeniedException(
                this.messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
    }

}