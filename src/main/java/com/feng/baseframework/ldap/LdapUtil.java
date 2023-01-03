package com.feng.baseframework.ldap;

import com.feng.baseframework.constant.LdapPropertyConfig;
import com.feng.baseframework.exception.BusinessException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.LikeFilter;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.query.SearchScope;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.PagedResultsControl;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2020/10/12 14:14
 * ldap工具类
 *
 * @author lanhaifeng
 * @since
 **/
public class LdapUtil {
	
	private static Logger logger = LoggerFactory.getLogger(LdapUtil.class);

	/**
	 * 2020/8/20 10:00
	 * 构建ldap需要的配置map
	 *
	 * @param ldapPropertyConfig
	 * @author lanhaifeng
	 * @return java.util.Hashtable<java.lang.String,java.lang.String>
	 */
	public static Hashtable<String, String> buildLdapConfig(LdapPropertyConfig ldapPropertyConfig) {
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
		return HashEnv;
	}

	/**
	 * 2020/10/12 15:52
	 * 构建DirContext
	 *
	 * @param ldapPropertyConfig
	 * @author lanhaifeng
	 * @return javax.naming.directory.DirContext
	 */
	public static DirContext buildDirContext(LdapPropertyConfig ldapPropertyConfig){
		try {
			if(Objects.nonNull(ldapPropertyConfig)) ldapPropertyConfig.validate();
			Hashtable<String, String> HashEnv = LdapUtil.buildLdapConfig(ldapPropertyConfig);

			return new InitialDirContext(HashEnv);
		} catch (Exception e) {
			logger.error("ldap连接失败，错误：" + ExceptionUtils.getFullStackTrace(e));
		}

		return null;
	}

	/**
	 * 2020/10/12 9:55
	 * 测试连接
	 *
	 * @param ldapPropertyConfig
	 * @author lanhaifeng
	 * @return boolean
	 */
	public static boolean checkConnect(LdapPropertyConfig ldapPropertyConfig){
		DirContext dirContext = null;
		try{
			dirContext = LdapUtil.buildDirContext(ldapPropertyConfig);
			dirContext.close();
		}catch(Exception e){
		    logger.error("checkConnect错误：" + ExceptionUtils.getFullStackTrace(e));
		}
		return Objects.nonNull(dirContext);
	}

	/**
	 * 2020/10/12 14:13
	 * 构建查询对象
	 *
	  * @param attribute
	  * @param attributePricipalName
	  * @param organizationName
	 * @author lanhaifeng
	 * @return org.springframework.ldap.query.LdapQueryBuilder
	 */
	public static LdapQueryBuilder buildLdapQuery(String organizationName, String attributePricipalName, String attribute){
		//构建查询类
		LdapQueryBuilder ldapQuery = LdapQueryBuilder.query();

		//构建过滤器
		AndFilter filter = new AndFilter();
		if(StringUtils.isBlank(attribute)){
			attribute = "*";
		}

		//用户名对应属性名过滤
		filter.and(new LikeFilter(attributePricipalName, attribute));
		ldapQuery.searchScope(SearchScope.SUBTREE);
		if (StringUtils.isNotBlank(organizationName)) {
			ldapQuery.base(organizationName);
		}
		ldapQuery.filter(filter);

		return ldapQuery;
	}

	/**
	 * 构造查询控制器
	 * @param userPricipalName
	 * @return
	 */
	public static SearchControls buildSearchControls(String userPricipalName){
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(new String[]{userPricipalName});

		return controls;
	}

	/**
	 * 构造查询分页查询控制器
	 * @param cookie
	 * @param pageSize
	 * @return
	 */
	public static PagedResultsControl buildSearchControls(byte[] cookie, int pageSize){
		try {
			PagedResultsControl control = new PagedResultsControl(pageSize, cookie, Control.CRITICAL);
			return control;
		} catch (Exception e) {
			logger.error("" + ExceptionUtils.getFullStackTrace(e));
		}

		return null;
	}

	/**
	 * 2020/10/12 15:32
	 * 构造查询结果谋一属性列列表
	 *
	 * @param attributePricipalName
	 * @param searchResult
	 * @author lanhaifeng
	 * @return java.util.List<java.lang.String>
	 */
	public static List<String> buildSearchResultAttributes(String attributePricipalName, NamingEnumeration<SearchResult> searchResult) {
		List<String> results = new ArrayList<>();

		if (StringUtils.isBlank(attributePricipalName) || Objects.isNull(searchResult)) return results;
		try {
			while (searchResult.hasMoreElements()) {
				String result = searchResult.nextElement().getAttributes().get(attributePricipalName).get().toString();
				if (StringUtils.isNotBlank(result)) {
					results.add(result);
				}
			}
		} catch (Exception e) {
			logger.error("buildSearchResultAttributes错误：" + ExceptionUtils.getFullStackTrace(e));
			throw new BusinessException("buildSearchResultAttributes错误");
		}

		return results;
	}

	/**
	 * 2020/10/12 19:05
	 * 从用户名中提取组织机构名
	 *
	 * @param ldapUserName
	 * @author lanhaifeng
	 * @return java.lang.String
	 */
	public static String analysisOrganizationName(String ldapUserName){
		if (StringUtils.isBlank(ldapUserName)) return "";
		if (ldapUserName.contains("@")) {
			StringBuilder targetDc = new StringBuilder();
			String dcStr = ldapUserName.substring(ldapUserName.lastIndexOf("@") + 1);
			if (StringUtils.isNotBlank(dcStr)) {
				String[] dcs = dcStr.split("\\.");
				for (int i = 0; i < dcs.length; i++) {
					targetDc.append("dc=").append(dcs[i]);
					if (i != dcs.length - 1) {
						targetDc.append(",");
					}
				}
			}
			return targetDc.toString();
		}
		if (ldapUserName.contains("dc=")) {
			return ldapUserName.substring(ldapUserName.indexOf(",") + 1);
		}

		return "";
	}
}
