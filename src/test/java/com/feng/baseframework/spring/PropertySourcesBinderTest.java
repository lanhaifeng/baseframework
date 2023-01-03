package com.feng.baseframework.spring;

import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.WebDataBinder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * baseframework
 * 2021/4/29 10:34
 * 测试PropertySourcesBinder
 *
 * @author lanhaifeng
 * @since
 **/
public class PropertySourcesBinderTest {

	@Test
	public void testBeanBind() throws IOException {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("classpath:bind1.properties");
		Properties bind1 = new Properties();
		bind1.load(resource.getInputStream());

		resource = resourceLoader.getResource("classpath:bind2.properties");
		Properties bind2 = new Properties();
		bind2.load(resource.getInputStream());

		PropertiesPropertySource propertySource1 = new PropertiesPropertySource("bind1", bind1);
		PropertiesPropertySource propertySource2 = new PropertiesPropertySource("bind2", bind2);

		MutablePropertySources pvs = new MutablePropertySources();
		pvs.addFirst(propertySource1);
		pvs.addAfter("bind1", propertySource2);
		PropertySourcesBinder binder = new PropertySourcesBinder(pvs);

		DataSourceTest dataSource1 = new DataSourceTest();
		binder.bindTo("ds.datasource", dataSource1);

		DataSourceTest dataSource2 = new DataSourceTest();
		binder.bindTo("upgrade.datasource", dataSource2);

		DataSourceTest dataSource3 = new DataSourceTest();
		DataBinder dataBinder = new WebDataBinder(dataSource3);

		MutablePropertyValues mutablePropertyValues = new MutablePropertyValues(propertySource1.getSource());
		mutablePropertyValues.addPropertyValues(propertySource2.getSource());

		dataBinder.bind(mutablePropertyValues);

		System.out.println(dataSource1.toString());
		System.out.println(dataSource2.toString());
		System.out.println(dataSource3.toString());

	}

	public static class DataSourceTest {
		private String userName;
		private String password;
		private Class<? extends DataSource> type;
		private String driverClassName;
		private String url;

		@Override
		public String toString() {
			return "DataSource{" +
					"userName='" + userName + '\'' +
					", password='" + password + '\'' +
					", type='" + type + '\'' +
					", driverClassName='" + driverClassName + '\'' +
					", url='" + url + '\'' +
					'}';
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Class<? extends DataSource> getType() {
			return type;
		}

		public void setType(Class<? extends DataSource> type) {
			this.type = type;
		}

		public String getDriverClassName() {
			return driverClassName;
		}

		public void setDriverClassName(String driverClassName) {
			this.driverClassName = driverClassName;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
}
