package com.feng.baseframework.ldap;

import com.feng.baseframework.constant.LdapPropertyConfig;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.query.LdapQueryBuilder;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.List;
import java.util.Objects;

/**
 * baseframework
 * 2020/8/19 19:20
 * ldap操作句柄
 *
 * @author lanhaifeng
 * @since
 **/
public class LdapStatement {
	private static Logger logger = LoggerFactory.getLogger(LdapStatement.class);

	public final static String DEFAULT_USER_PRICIPAL_NAME = "displayName";
	private LdapPropertyConfig ldapPropertyConfig;
	private DirContext dirContext;
	private String userPricipalName;
	private String realUserPricipalName;
	private String organizationName;


	public LdapStatement(LdapPropertyConfig ldapPropertyConfig) {
		this.ldapPropertyConfig = ldapPropertyConfig;
	}

	public void setLdapPropertyConfig(LdapPropertyConfig ldapPropertyConfig) {
		this.ldapPropertyConfig = ldapPropertyConfig;
	}

	public void setUserPricipalName(String userPricipalName) {
		this.userPricipalName = userPricipalName;
	}

	public void setRealUserPricipalName(String realUserPricipalName) {
		this.realUserPricipalName = realUserPricipalName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public LdapStatement() {
	}

	/**
	 * 2020/8/19 19:42
	 * 初始化工作
	 *
	 * @author lanhaifeng
	 * @return void
	 */
	public void init() {
		if(Objects.nonNull(ldapPropertyConfig)) ldapPropertyConfig.validate();
		dirContext = LdapUtil.buildDirContext(ldapPropertyConfig);
		if(StringUtils.isBlank(userPricipalName)) {
			userPricipalName = ldapPropertyConfig.getUserPricipalName();
		}
		if(StringUtils.isBlank(realUserPricipalName)) {
			realUserPricipalName = ldapPropertyConfig.getRealUserPricipalName();
		}
		if(StringUtils.isBlank(realUserPricipalName)){
			realUserPricipalName = DEFAULT_USER_PRICIPAL_NAME;
		}
	}

	/**
	 * 2020/8/19 19:36
	 * 校验是否初始化
	 *
	 * @param
	 * @author lanhaifeng
	 * @return void
	 */
	private void checkInit(){
		if(Objects.isNull(dirContext) || StringUtils.isBlank(userPricipalName) || StringUtils.isBlank(realUserPricipalName)
				|| StringUtils.isBlank(organizationName)){
			throw new RuntimeException("LdapStatement not init");
		}
	}

	/**
	 * 2020/10/12 9:55
	 * 测试连接，初始化DirContext成功表示能正常连接
	 *
	 * @author lanhaifeng
	 * @return boolean
	 */
	public boolean checkConnect(){
		return LdapUtil.checkConnect(ldapPropertyConfig);
	}

	/**
	 * 2020/10/12 17:18
	 * 认证方法
	 *
	 * @param userName
	 * @param password
	 * @author lanhaifeng
	 * @return boolean
	 */
	public boolean authenticate(String userName, String password) {
		boolean result = false;
		try {
			checkInit();

			LdapQueryBuilder ldapQueryBuilder = LdapUtil.buildLdapQuery(organizationName, userPricipalName, userName);
			SearchControls searchControls = LdapUtil.buildSearchControls(realUserPricipalName);
			NamingEnumeration<SearchResult> searchResults = dirContext.search(ldapQueryBuilder.base(), ldapQueryBuilder.filter().encode(), searchControls);
			List<String> userNames = LdapUtil.buildSearchResultAttributes(realUserPricipalName, searchResults);
			if(Objects.nonNull(userNames) && userNames.size() > 0){
				LdapPropertyConfig ldapPropertyConfig = this.ldapPropertyConfig.clone();

				ldapPropertyConfig.setLdapUserName(realUserPricipalName + "=" + userNames.get(0) + "," + organizationName);
				ldapPropertyConfig.setLdapPassword(password);
				DirContext dirContext = LdapUtil.buildDirContext(ldapPropertyConfig);
				if(Objects.nonNull(dirContext)){
					result = true;
					dirContext.close();
				}
			}
		} catch (Exception e) {
			logger.error("authenticate错误：" + ExceptionUtils.getFullStackTrace(e));
			result = false;
		}

		return result;
	}

	/**
	 * 2020/10/12 19:30
	 * 释放连接
	 *
	 * @param
	 * @author lanhaifeng
	 * @return boolean
	 */
	public boolean close(){
		try {
			if(Objects.nonNull(dirContext)) {
				dirContext.close();
			}
		} catch (NamingException e) {
			logger.error("关闭连接失败，错误：" + ExceptionUtils.getFullStackTrace(e));
			return false;
		}

		return true;
	}
}
