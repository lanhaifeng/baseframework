package com.feng.baseframework.autoconfig;

import com.feng.baseframework.constant.GlobalPropertyConfig;
import com.feng.baseframework.constant.LdapPropertyConfig;
import com.feng.baseframework.constant.SecurityModeEnum;
import com.feng.baseframework.security.MyAuthenticationProvider;
import com.feng.baseframework.security.MySecurityMetadataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.Resource;
import javax.naming.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: baseframework
 * @Description: 安全验证配置类
 * @Author: lanhaifeng
 * @CreateDate: 2018/5/21 23:14
 * @UpdateUser:
 * @UpdateDate: 2018/5/21 23:14
 * @UpdateRemark:
 * @Version: 1.0
 */
@Configuration
@EnableWebSecurity
//prePostEnabled=true启用注解@PreAuthorize
//jsr250Enabled=true启用注解@RolesAllowed
//securedEnabled=true启用注解@Secured
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationProvider myAuthenticationProvider;
    @Autowired
    private GlobalPropertyConfig globalPropertyConfig;
    @Autowired
    private MySecurityMetadataSource mySecurityMetadataSource;
    @Resource(name = "myUserDetailsService")
    private UserDetailsService userService;
    @Autowired
    private PasswordEncoder myPasswordEncoder;
    @Autowired
    private LdapPropertyConfig ldapPropertyConfig;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义认证
        if (SecurityModeEnum.CUSTOM_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            auth.authenticationProvider(myAuthenticationProvider);
        }
        //默认认证
        if (SecurityModeEnum.DEFAULT_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            auth.inMemoryAuthentication().withUser("admin").password("admin").
                    roles("ADMIN").and().withUser("user").password("user").roles("USER").
                    and().withUser("test").password("test").roles("TEST");
        }
        //无认证
        if (SecurityModeEnum.NO_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode()));
    }

    //SimpleUrlHandlerMapping UrlFilenameViewController
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //自定义认证
        if (SecurityModeEnum.CUSTOM_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            http
                    .authorizeRequests()
                    //.antMatchers("/", "/static/index.html").permitAll()
                    .antMatchers("/", "/static/index.html").authenticated()
                    .antMatchers("/anonymous/**").anonymous()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/static/hello.html")
                    .and()
                    .logout()
                    .logoutSuccessUrl("/logout")
                    .permitAll()
                    .invalidateHttpSession(true)
                    .and()
                    .rememberMe()
                    .tokenValiditySeconds(1209600);
        }
        //默认认证
        if (SecurityModeEnum.DEFAULT_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())) {
            http.csrf().disable()
                    .formLogin()          // 定义当需要用户登录时候，转到的登录页面。
                    .defaultSuccessUrl("/static/hello.html", true)
                    .and()
                    .authorizeRequests()    // 定义哪些URL需要被保护、哪些不需要被保护
//                    .antMatchers("/anonymous/**").anonymous()  //定义那些url匿名认证
//                    .antMatchers("/baseManage/getInfo").hasAnyRole("ADMIN", "TEST")  //使用自定义资源类后，不支持antMatchers方式配置的权限
                    .anyRequest()        // 任何请求,登录后可以访问
                    .authenticated()
                    .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                            //利用后置类为FilterSecurityInterceptor配置自定义资源类
                            fsi.setSecurityMetadataSource(mySecurityMetadataSource);
                            //AffirmativeBased中Voter默认使用的是WebExpressionVoter，
                            // 而WebExpressionVoter中ConfigAttribute使用的是WebExpressionConfigAttribute对象，该类是包内可见，
                            // 因此自定义MySecurityMetadataSource无法WebExpressionConfigAttribute对象，导致无法验证，替换AffirmativeBased中Voter为RoleVoter
                            fsi.setAccessDecisionManager(getAccessDecisionManager());
                            return fsi;
                        }
                    });
        }
        //无认证
        if (SecurityModeEnum.NO_AUTHENTICATION.toString().equals(globalPropertyConfig.getSecurityMode())){
            http.csrf().disable();
        }
    }

    private AccessDecisionManager getAccessDecisionManager(){
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters(){
        List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(new RoleVoter());

        return accessDecisionVoters;
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
