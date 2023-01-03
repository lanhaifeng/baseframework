package com.feng.baseframework.security;

import com.feng.baseframework.mapper.MenuMapper;
import com.feng.baseframework.model.Menu;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * baseframework
 * 2019/9/27 17:56
 * 自定义security资源规则处理类
 *
 * @author lanhaifeng
 * @since
 **/
@Component
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource,InitializingBean {

    @Autowired
    private MenuMapper menuMapper;
    private DefaultFilterInvocationSecurityMetadataSource defaultFilterInvocationSecurityMetadataSource;
    private Collection<ConfigAttribute> defaultAttributes;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        Collection<ConfigAttribute> attrs = defaultFilterInvocationSecurityMetadataSource.getAttributes(o);
        if(attrs == null || attrs.isEmpty()){
            return defaultAttributes;
        }
        return attrs.stream().filter(attr->"ROLE_ANONYMOUS".equals(attr.getAttribute())).collect(Collectors.toList()).size() > 0 ? null : attrs;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return defaultFilterInvocationSecurityMetadataSource.getAllConfigAttributes();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

    @Override
    public void afterPropertiesSet() throws Exception {
        defaultFilterInvocationSecurityMetadataSource = new DefaultFilterInvocationSecurityMetadataSource(initSecurityMetadataSource());
        initDefaultAttributes();
    }

    private Map<String, String> getMenusWithRoles(){
        List<Menu> allMenus = menuMapper.getAllMenusWithRole();
        Map<String, String> menusWithRoles = new HashMap<>();
        Optional.ofNullable(allMenus).orElse(new ArrayList<>()).forEach(menu -> {
            if(menusWithRoles.containsKey(menu.getUrl())){
                String temp = menusWithRoles.get(menu.getUrl());
                menusWithRoles.put(menu.getUrl(), temp + "," + menu.getRoleName());
            }else {
                menusWithRoles.put(menu.getUrl(), menu.getRoleName());
            }
        });
        loadPreMenusWithRoles(menusWithRoles);

        return menusWithRoles;
    }

    private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> initSecurityMetadataSource(){
	    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> resources = new LinkedHashMap<>();
        Map<String, String> menusWithRoles = getMenusWithRoles();

		menusWithRoles.forEach((url, roles)->{
			RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
			Collection<ConfigAttribute> atts = new ArrayList<>();
			if(StringUtils.isNotBlank(roles)){
				for (String role : roles.split(",")) {
					atts.add(new SecurityConfig(role));
				}
			}else {
				atts = null;
			}
            resources.put(requestMatcher, atts);
        });

	    return resources;
    }

    private void loadPreMenusWithRoles(Map<String, String> menusWithRoles){
		if(menusWithRoles == null) return;
		menusWithRoles.put("/baseManage/*", "ROLE_ADMIN,ROLE_TEST,ROLE_USER");
		menusWithRoles.put("/anonymous/*", "ROLE_ANONYMOUS");
	}

    //当url未匹配上需要系统管理员权限才能访问
    private void initDefaultAttributes(){
        defaultAttributes = new ArrayList<>();
        defaultAttributes.add(new SecurityConfig("ROLE_ADMIN"));
    }
}
