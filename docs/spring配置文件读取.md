## 读取配置文件
### YamlPropertiesFactoryBean
```text
YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
yamlPropertiesFactoryBean.setResources(new ClassPathResource("/application-quartz.yml"));
yamlPropertiesFactoryBean.afterPropertiesSet();
properties = yamlPropertiesFactoryBean.getObject();
```


### PropertiesFactoryBean
```text
PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
propertiesFactoryBean.setLocation(new ClassPathResource("/application-quartz.yml"));
//在quartz.properties中的属性被读取并注入后再初始化对象
propertiesFactoryBean.afterPropertiesSet();
properties = propertiesFactoryBean.getObject();
```

### PropertiesPropertySourceLoader
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();
List<PropertySource<?>> propertySources = propertiesPropertySourceLoader.load("dev", resource);
```

### YamlPropertySourceLoader
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();
List<PropertySource<?>> propertySources = yamlPropertySourceLoader.load("dev", resource);
```

### PropertiesPropertySource
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:bind1.properties");
Properties bind1 = new Properties();
bind1.load(resource.getInputStream());
PropertiesPropertySource propertySource1 = new PropertiesPropertySource("bind1", bind1);
```

### MutablePropertySources
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:bind1.properties");
Properties bind1 = new Properties();
bind1.load(resource.getInputStream());
PropertiesPropertySource propertySource1 = new PropertiesPropertySource("bind1", bind1);
MutablePropertySources pvs = new MutablePropertySources();
pvs.addFirst(propertySource1);
```

## 数据解析
解析${}
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:bind1.properties");
Properties bind1 = new Properties();
bind1.load(resource.getInputStream());
PropertiesPropertySource propertySource1 = new PropertiesPropertySource("bind1", bind1);
//替换${}
Properties source = new Properties();
PropertiesPropertySource mergeSource = new PropertiesPropertySource("mergeSource", source);
PropertySourcesPropertyResolver propertySourcesPropertyResolver = new PropertySourcesPropertyResolver(pvs);
for (String propertyName : propertySource1.getPropertyNames()) {
    source.setProperty(propertyName, propertySourcesPropertyResolver.getProperty(propertyName));
}
```

## 数据绑定
### Binder#ofInstance
```text
KeyStoreParam keyStoreParam = new KeyStoreParam();
keyStoreParam.setCity("city");
keyStoreParam.setFilePath("file");

ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

new Binder(ConfigurationPropertySources.from(
        propertiesPropertySourceLoader.load("dev", resource)))
        .bind("base.test", Bindable.ofInstance(keyStoreParam));
```

### Binder#Class
```text
ResourceLoader resourceLoader = new DefaultResourceLoader();
Resource resource = resourceLoader.getResource("classpath:application-dev1.properties");
PropertiesPropertySourceLoader propertiesPropertySourceLoader = new PropertiesPropertySourceLoader();

BindResult<Teacher> bindResult = new Binder(ConfigurationPropertySources.from(
        propertiesPropertySourceLoader.load("dev", resource)))
        .bind("teacher.test", Teacher.class);
Teacher teacher = bindResult.get();
```