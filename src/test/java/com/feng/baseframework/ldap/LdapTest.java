package com.feng.baseframework.ldap;

import com.feng.baseframework.autoconfig.LdapFactory;
import com.feng.baseframework.common.JunitBaseTest;
import com.feng.baseframework.common.MockitoBaseTest;
import com.feng.baseframework.constant.LdapPropertyConfig;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.test.context.ActiveProfiles;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.*;
import java.io.IOException;
import java.util.*;

/**
 * baseframework
 * 2020/2/18 10:22
 * ldap测试
 *
 * @author lanhaifeng
 * @since
 **/
@ActiveProfiles("dev")
public class LdapTest extends MockitoBaseTest {

	@Autowired
	private LdapTemplate ldapTemplate;
	@Autowired
	@Qualifier("ldapFactory")
	private LdapFactory ldapFactory;

	@Test
	public void authenticateLdapTest() {
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("uid", "lhf3"));
		ldapQuery.searchScope(SearchScope.SUBTREE);
		ldapQuery.base("ou=dev,ou=users");
		ldapQuery.filter(filter);
		ldapTemplate.authenticate(ldapQuery,  "test");
	}

	@Test
	public void authenticateLdapForJavaTest() throws NamingException {
		DirContext ctx = null;
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong);
		HashEnv.put(Context.SECURITY_PRINCIPAL, "UID=LHF3,OU=DEV,OU=USERS,DC=MCHZ,DC=COM"); // AD的用户名
		HashEnv.put(Context.SECURITY_CREDENTIALS, "test"); // AD的密码
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");// 连接超时设置为3秒
		HashEnv.put(Context.PROVIDER_URL, "ldap://" + "192.168.230.81" + ":" + "389");// 默认端口389
		ctx = new InitialDirContext(HashEnv);// 初始化上下文
	}

	@Test
	public void authenticateADTest() {
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();
		AndFilter filter = new AndFilter();
		String userPricipalName = "sAMAccountName";
		String userName = "dcadmin";
		String password = "Hzmc321#";
		filter.and(new EqualsFilter(userPricipalName, userName));
		filter.and(new EqualsFilter("objectCategory", "person"));
		filter.and(new EqualsFilter("objectClass", "user"));
		ldapQuery.filter(filter);

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(new String[]{userPricipalName});

		DirContext ctx = ldapTemplate.getContextSource().getContext(userName, password);
		System.out.println(ctx.toString());
		LdapUtils.closeContext(ctx);

		ctx = ldapTemplate.getContextSource().getContext(userName, password);
		System.out.println(ctx.toString());
	}

	@Test
	public void authenticateADForJavaTest() throws NamingException {
		DirContext ctx = null;
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong);
//		HashEnv.put(Context.SECURITY_PRINCIPAL, "dmy"); // AD的用户名
		HashEnv.put(Context.SECURITY_PRINCIPAL, "test555"); // AD的用户名
		HashEnv.put(Context.SECURITY_CREDENTIALS, "Test012345"); // AD的密码hzmcAdmin_12F
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");// 连接超时设置为3秒
		HashEnv.put(Context.PROVIDER_URL, "ldap://" + "192.168.239.90" + ":" + "389");// 默认端口389
		ctx = new InitialDirContext(HashEnv);// 初始化上下文
	}

	@Test
	public void validateLdapForJavaTest() throws NamingException {
		DirContext ctx = null;
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong);
//		HashEnv.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=mchz,dc=com"); // AD的用户名
		HashEnv.put(Context.SECURITY_PRINCIPAL, "uid=lhf3,ou=dev,ou=users,dc=mchz,dc=com"); // AD的用户名
//		HashEnv.put(Context.SECURITY_CREDENTIALS, "hzmcdba"); // AD的密码
		HashEnv.put(Context.SECURITY_CREDENTIALS, "test"); // AD的密码
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");// 连接超时设置为3秒
		HashEnv.put(Context.PROVIDER_URL, "ldap://" + "192.168.230.81" + ":" + "389");// 默认端口389
		ctx = new InitialDirContext(HashEnv);// 初始化上下文
	}

	@Test
	public void searchLdapLdapTemplateTest() {
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();
		ldapQuery.base("ou=dingshi,ou=qa,ou=users");
		AndFilter filter = new AndFilter();
		String userPricipalName = "cn";
		filter.and(new LikeFilter(userPricipalName, "*"));

		ldapQuery.searchScope(SearchScope.SUBTREE);
		ldapQuery.filter(filter);

		List<String> userNames = ldapTemplate.search(ldapQuery, new ContextMapper<String>() {
			@Override
			public String mapFromContext(Object ctx) throws NamingException {
				DirContextOperations adapter = (DirContextOperations)ctx;
				return (String) adapter.getObjectAttribute(userPricipalName);
			}
		});

		System.out.println(userNames);
	}

	@Test
	public void searchADLdapTemplateTest() throws NamingException {
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();
		AndFilter filter = new AndFilter();
		String userPricipalName = "name";
		filter.and(new LikeFilter(userPricipalName, "*"));
		filter.and(new EqualsFilter("objectCategory", "person"));
		filter.and(new EqualsFilter("objectClass", "user"));

		ldapQuery.searchScope(SearchScope.SUBTREE);
		ldapQuery.base("CN=Users");
		ldapQuery.filter(filter);

		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(new String[]{userPricipalName});

		NamingEnumeration<SearchResult> answers = ldapTemplate.getContextSource().getContext("dcadmin", "Hzmc321#").search(ldapQuery.base(), ldapQuery.filter().encode(), controls);
		while (answers.hasMoreElements()){
			System.out.println(answers.nextElement().getAttributes().get(userPricipalName).get().toString());
		}
	}

	@Test
	public void searchLdapTest() throws NamingException {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String user = "cn=admin,dc=mchz,dc=com";
		String password = "hzmcdba";
		String url = "ldap://192.168.230.81:389/";
		String searchBase = "ou=dingshi,ou=qa,ou=users,dc=mchz,dc=com";
		String searchFilter = "(cn=*)";
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, url);
		LdapContext context = new InitialLdapContext(env, null);
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answers = context.search(searchBase, searchFilter, controls);
		boolean result = false;
		Object value;
		while (answers.hasMoreElements()){
			value = answers.nextElement().getAttributes().get("name").get();
			if(value != null && StringUtils.isNotBlank(value.toString())){
				result = true;
				break;
			}
		}
		if(result){
			System.out.println("exists");
		}else {
			System.out.println("not exists");
		}
		while (answers.hasMoreElements()){
			System.out.println(answers.nextElement().getAttributes().get("name"));
		}
		context.close();
	}

	@Test
	public void searchADest() throws NamingException {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		String user = "dcadmin@hzmc.com";
		String password = "Hzmc321#";
		String url = "ldap://192.168.239.90:389/";
		String searchBase = "CN=Users,dc=hzmc,dc=com";
		String searchFilter = "(&(objectCategory=person)(objectClass=user)(name=*))";
		env.put(Context.SECURITY_PRINCIPAL, user);
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.PROVIDER_URL, url);
		LdapContext context = new InitialLdapContext(env, null);
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answers = context.search(searchBase, searchFilter, controls);
		boolean result = false;
		Object value;
		while (answers.hasMoreElements()){
			value = answers.nextElement().getAttributes().get("name").get();
			if(value != null && StringUtils.isNotBlank(value.toString())){
				result = true;
				break;
			}
		}
		if(result){
			System.out.println("exists");
		}else {
			System.out.println("not exists");
		}
		while (answers.hasMoreElements()){
			System.out.println(answers.nextElement().getAttributes().get("name"));
		}
		context.close();
	}
	
	@Test
	public void pageLdapTemplateSearch() throws IOException {
		int pageSize = 2;
		String userPricipalName = "sAMAccountName";
		PagedResultsCookie cookie = null;
		AndFilter filter = new AndFilter();
		filter.and(new LikeFilter(userPricipalName, "*"));
		filter.and(new EqualsFilter("objectCategory", "person"));
		filter.and(new EqualsFilter("objectClass", "user"));
		PagedResultsDirContextProcessor control = new PagedResultsDirContextProcessor (pageSize, cookie);
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchControls.setReturningAttributes(new String[]{userPricipalName});

		ContextMapper<String> contextMapper = new ContextMapper<String>() {
			@Override
			public String mapFromContext(Object ctx) throws NamingException {
				DirContextOperations adapter = (DirContextOperations)ctx;
				return (String) adapter.getObjectAttribute(userPricipalName);
			}
		};
		do {
			List<String> mList = ldapTemplate.search("CN=Users",  filter.encode(),searchControls, contextMapper, control);
			cookie = control.getCookie();
			System.out.println(mList);
		} while (cookie.getCookie() != null);
	}

	@Test
	public void pageLdapContextSearch() throws IOException, NamingException {
		int pageSize = 2;
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();
		String userPricipalName = "sAMAccountName";
		AndFilter filter = new AndFilter();
		filter.and(new LikeFilter(userPricipalName, "*"));
		filter.and(new EqualsFilter("objectCategory", "person"));
		filter.and(new EqualsFilter("objectClass", "user"));
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		searchControls.setReturningAttributes(new String[]{userPricipalName});

		ldapQuery.searchScope(SearchScope.SUBTREE);
		ldapQuery.filter(filter);

		PagedResultsControl page = new PagedResultsControl(pageSize, null, Control.CRITICAL);

		byte[] cookie = null;
		List<String> mList = new ArrayList<>();
		DirContext dirContext = ldapTemplate.getContextSource().getContext("dcadmin", "Hzmc321#");
		LdapContext lctx = (LdapContext)dirContext;
		lctx.setRequestControls(new Control[] {page});
		do {
			NamingEnumeration<SearchResult> answers = lctx.search(ldapQuery.base(), ldapQuery.filter().encode(), searchControls);
			while (answers.hasMoreElements()){
				mList.add(answers.nextElement().getAttributes().get("sAMAccountName").get().toString());
			}
			cookie = parseControls(lctx.getResponseControls());
			lctx.setRequestControls(new Control[] { new PagedResultsControl(pageSize, cookie, Control.CRITICAL) });
		} while ((cookie != null) && (cookie.length != 0));
		System.out.println(mList);
	}

	private static byte[] parseControls(Control[] controls) throws NamingException {
		byte[] cookie = null;
		if (controls != null) {
			for (int i = 0; i < controls.length; i++) {
				if (controls[i] instanceof PagedResultsResponseControl) {
					PagedResultsResponseControl prrc = (PagedResultsResponseControl) controls[i];
					cookie = prrc.getCookie();
				}
			}
		}
		return (cookie == null) ? new byte[0] : cookie;
	}

	@Test
	public void testLdapTemplate(){
		LdapContextSource contextSource = new LdapContextSource();
		Map<String, Object> config = new HashMap<>();
		String ldapUrl = String.format("ldap://%s:%s/", "192.168.230.81", "389");
		String baseDc = "dc=mchz,dc=com";
		String userName = "cn=admin,dc=mchz,dc=com";
		String password = "hzmcdba";

		contextSource.setUrl(ldapUrl);
		if (StringUtils.isNotBlank(baseDc)){
			contextSource.setBase(baseDc);
		}
		contextSource.setUserDn(userName);
		contextSource.setPassword(password);

		config.put("java.naming.ldap.attributes.binary", "objectGUID");
		config.put(Context.SECURITY_AUTHENTICATION, "simple");

		contextSource.setPooled(true);
		contextSource.setBaseEnvironmentProperties(config);
		contextSource.afterPropertiesSet();
		contextSource.getReadOnlyContext();

	}

	@Test
	public void testLdapFactory() throws Exception {
		DirContext dirContext = ldapFactory.getObject();
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		String searchBase = "ou=dev,ou=users,dc=mchz,dc=com";
		String searchFilter = "(&(objectClass=inetOrgPerson)(cn=*))";
		NamingEnumeration<SearchResult> answers = dirContext.search(searchBase, searchFilter, controls);
		boolean result = false;
		Object value;
		while (answers.hasMoreElements()){
			value = answers.nextElement().getAttributes().get("cn").get();
			if(value != null && StringUtils.isNotBlank(value.toString())){
				result = true;
				break;
			}
		}
		if(result){
			System.out.println("exists");
		}else {
			System.out.println("not exists");
		}
		while (answers.hasMoreElements()){
			System.out.println(answers.nextElement().getAttributes().get("cn"));
		}
		dirContext.close();

		LdapPropertyConfig ldapPropertyConfig = new LdapPropertyConfig();
		ldapPropertyConfig.setLdapUrl("ldap://192.168.239.90:389/");
		ldapPropertyConfig.setLdapUserName("dcadmin@hzmc.com");
		ldapPropertyConfig.setLdapPassword("Hzmc321#");

		ldapFactory.setLdapPropertyConfig(ldapPropertyConfig);
		dirContext = ldapFactory.getObject();

		controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		searchBase = "CN=Users,dc=hzmc,dc=com";
		searchFilter = "(&(objectCategory=person)(objectClass=user)(displayname=*))";
		answers = dirContext.search(searchBase, searchFilter, controls);
		result = false;
		while (answers.hasMoreElements()){
			value = answers.nextElement().getAttributes().get("displayname").get();
			if(value != null && StringUtils.isNotBlank(value.toString())){
				result = true;
				break;
			}
		}
		if(result){
			System.out.println("exists");
		}else {
			System.out.println("not exists");
		}
		while (answers.hasMoreElements()){
			System.out.println(answers.nextElement().getAttributes().get("displayname"));
		}
		dirContext.close();
	}
}
