package com.feng.baseframework.ldap;

import com.feng.baseframework.constant.LdapPropertyConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LdapStatementTest {

	private LdapPropertyConfig ldapPropertyConfig;
	private LdapStatement ldapStatement;

	@Before
	public void setUp() {
		ldapPropertyConfig = new LdapPropertyConfig();
		ldapPropertyConfig.setLdapUrl("ldap://192.168.230.81:389/");
		ldapPropertyConfig.setLdapUserName("cn=admin,dc=mchz,dc=com");
		ldapPropertyConfig.setLdapPassword("hzmcdba");

		ldapPropertyConfig.setUserPricipalName("cn");
		ldapPropertyConfig.setRealUserPricipalName("cn");

		ldapStatement = new LdapStatement(ldapPropertyConfig);
		ldapStatement.init();
	}

	@Test
	public void checkConnect() {
		Assert.assertTrue("测试连接失败", ldapStatement.checkConnect());
		Assert.assertTrue("关闭连接失败", ldapStatement.close());
	}

	@Test
	public void authenticate() {
		//cn=lisi,ou=dev,ou=users,dc=mchz,dc=com
		ldapStatement.setOrganizationName("ou=dev,ou=users,dc=mchz,dc=com");
		Assert.assertTrue("认证失败", ldapStatement.authenticate("lisi", "lisi"));
		Assert.assertTrue("关闭连接失败", ldapStatement.close());
	}

}