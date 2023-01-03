package com.feng.baseframework.autoconfig;

import com.feng.baseframework.constant.LdapPropertyConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * baseframework
 * 2020/7/13 16:17
 * ldap工厂模式
 *
 * @author lanhaifeng
 * @since
 **/
@Component("ldapFactory")
public class LdapFactory implements FactoryBean<DirContext> {

	private LdapPropertyConfig ldapPropertyConfig;

	@Autowired
	public void setLdapPropertyConfig(LdapPropertyConfig ldapPropertyConfig) {
		this.ldapPropertyConfig = ldapPropertyConfig;
	}

	@Override
	public DirContext getObject() throws Exception {
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		// LDAP访问安全级别(none,simple,strong);
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
		// 用户名
		HashEnv.put(Context.SECURITY_PRINCIPAL, ldapPropertyConfig.getLdapUserName());
		// 密码
		HashEnv.put(Context.SECURITY_CREDENTIALS, ldapPropertyConfig.getLdapPassword());
		// LDAP工厂类
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// 连接超时设置为3秒
		HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");
		// 默认端口389
		HashEnv.put(Context.PROVIDER_URL, ldapPropertyConfig.getLdapUrl());
		// 初始化上下文
		return new InitialDirContext(HashEnv);
	}

	@Override
	public Class<?> getObjectType() {
		return DirContext.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
